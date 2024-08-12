package settlement.room.service.hygine.well;

import game.GAME;
import init.resources.RBIT;
import init.resources.RESOURCES;
import init.resources.ResG;
import init.resources.ResGEat;
import settlement.main.SETT;
import settlement.misc.job.JOBMANAGER_HASER;
import settlement.misc.job.JOB_MANAGER;
import settlement.misc.job.SETT_JOB;
import settlement.misc.util.RESOURCE_TILE;
import settlement.room.industry.module.Industry;
import settlement.room.industry.module.ROOM_PRODUCER;
import settlement.room.main.RoomInstance;
import settlement.room.main.TmpArea;
import settlement.room.main.job.JobPositions;
import settlement.room.main.util.RoomInit;
import settlement.room.service.module.ROOM_SERVICER;
import settlement.room.service.module.RoomServiceInstance;
import snake2d.Renderer;
import snake2d.util.datatypes.COORDINATE;
import snake2d.util.rnd.RND;
import util.rendering.RenderData;
import util.rendering.ShadowBatch;

final class WellInstance extends RoomInstance implements ROOM_SERVICER, JOBMANAGER_HASER, ROOM_PRODUCER {

    final JobPositions<WellInstance> jobs;
    final RoomServiceInstance service;
    public boolean hasStorage = true;
    private static final long serialVersionUID = 1L;
    public long maxAmount;
    private long[] pData;
    final short sx,sy;
    int workage = 0;


    private int[] amounts = new int[RESOURCES.EDI().all().size()];
    private int[] amountIncoming = new int[RESOURCES.EDI().all().size()];
    private int amountTotal = 0;


    private final RBIT.RBITImp fetchMask = new RBIT.RBITImp().or(RESOURCES.EDI().mask);
    private final RBIT.RBITImp useMask = new RBIT.RBITImp();

    WellInstance(ROOM_WELL b, TmpArea area, RoomInit init) {
        super(b, area, init);

        int x = -1;
        int y = -1;
        int w = 0;


            for (COORDINATE c : body()) {
                if (is(c)) {
                    if (SETT.ROOMS().fData.tileData.get(c) == Constructor.B_WORK) {

                    } else if (SETT.ROOMS().fData.tileData.get(c) == Constructor.B_STORAGE) {
                        if (x == -1) {
                            x = c.x();
                            y = c.y();
                        }
                    } else if (SETT.TERRAIN().TREES.isTree(c.x(), c.y())) {
                        if (w == 0) {
                            SETT.ROOMS().data.set(this, c, settlement.room.service.hygine.well.Job.isWork.set(0));
                            w += RND.rInt(4);
                        } else {
                            w--;
                        }
                    }
                }
            }


        if (x == -1 || y == -1)
            GAME.Error(x + " " + y);
        sx = (short) x;
        sy = (short) y;


        pData = b.productionData.makeData();
        jobs = new Jobs(this);

        jobs.randomize();

        employees().maxSet(jobs.size());
        employees().neededSet((int)Math.ceil(jobs.size()/1.5));

        int am = 0;
        for (COORDINATE c : body())
            if (is(c) && b.bed.get(c.x(), c.y()) != null)
                am ++;
        service = new RoomServiceInstance(am, blueprintI().data);
        for (COORDINATE c : body())
            if (is(c))
                b.bed.init(c.x(), c.y());

        activate();

        for (ResGEat e : RESOURCES.EDI().all()) {
            if (e.serve) {
                useMask.or(e.resource);
            }
        }

        fetchMask.and(useMask);
    }

    @Override
    protected void loadFix() {
        pData = industry().makeDataFix(pData);
        if (amounts.length != RESOURCES.EDI().all().size()) {
            int[] ams = new int[RESOURCES.EDI().all().size()];
            int[] amsI = new int[RESOURCES.EDI().all().size()];
            amountTotal = 0;
            fetchMask.clear();
            fetchMask.or(RESOURCES.EDI().mask);
            useMask.clear();
            for (int i = 0; i < ams.length; i++) {
                ams[i] = amounts[i%amounts.length];
                amsI[i] = amountIncoming[i%amounts.length];
                amountTotal += ams[i];
            }
            this.amounts = ams;
            this.amountIncoming = amsI;
        }
    }

    public int amountTotal() {
        return amountTotal;
    }


    public boolean uses(ResG e) {
        return useMask.has(e.resource.bit);
    }

    public void usesToggle(ResG e) {
        useMask.toggle(e.resource);
    }

    @Override
    public JOB_MANAGER getWork() {
        return jobs;
    }

    @Override
    protected boolean render(Renderer r, ShadowBatch shadowBatch, RenderData.RenderIterator it) {
        return super.render(r, shadowBatch, it);
    }

    @Override
    protected void activateAction() {

    }

    @Override
    protected void deactivateAction() {

    }

    @Override
    protected void updateAction(double updateInterval, boolean day, int daycount) {
        if (day)
            service.updateDay();

        blueprintI().productionData.updateRoom(this);

        if (!active())
            return;
        jobs.searchAgain();
    }

    @Override
    protected void dispose() {
        for (COORDINATE c : body()) {
            if (is(c)) {
                Wash t = blueprintI().bed.get(c.x(), c.y());
                if (t != null)
                    t.dispose();
            }
            if (blueprintI().job.storage.get(c.x(), c.y(), this) != null)
                blueprintI().job.storage.dispose();
        }

        service.dispose(blueprintI().data);

    }

    @Override
    public RESOURCE_TILE resourceTile(int tx, int ty) {
        return blueprintI().job.storage.get(tx, ty, this);
    }

    @Override
    public ROOM_WELL blueprintI() {
        return (ROOM_WELL) blueprint();
    }

    @Override
    public RoomServiceInstance service() {
        return service;
    }

    @Override
    public double quality() {
        return ROOM_SERVICER.defQuality(this, 1);
    }

    @Override
    public long[] productionData() {
        return pData;
    }

    @Override
    public Industry industry() {
        return blueprintI().industries().get(0);
    }

    public long amount(ResG e) {
        return amounts[e.index()];
    }


    private static class Jobs extends JobPositions<WellInstance>{

        public Jobs(WellInstance ins) {
            super(ins);
            // TODO Auto-generated constructor stub
        }
        private static final long serialVersionUID = 8423260307910904017l;
        @Override
        protected boolean isAndInit(int tx, int ty) {
            return get(tx, ty) != null;
        }

        @Override
        protected SETT_JOB get(int tx, int ty) {
            return ins.blueprintI().job.init(tx, ty, ins);
        }


    }

    @Override
    public int industryI() {
        // TODO Auto-generated method stub
        return 0;
    }

}

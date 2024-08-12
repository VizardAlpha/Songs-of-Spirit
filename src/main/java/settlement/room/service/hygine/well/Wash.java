package settlement.room.service.hygine.well;

import static settlement.main.SETT.ROOMS;

import settlement.main.SETT;
import settlement.misc.util.FSERVICE;
import snake2d.util.datatypes.Coo;

final class Wash implements FSERVICE{


    private static final int AVAILABLE = 0;
    private static final int RESERVED = 1;
    private static final int USED = 2;
    private int data;
    private final Coo coo = new Coo();
    private WellInstance ins;
    private final ROOM_WELL blue;


    Wash(ROOM_WELL blue) {
        this.blue = blue;
    }

    Wash get(int tx, int ty) {
        if (blue.is(tx, ty)) {
            if (ROOMS().fData.tileData.get(tx, ty) == Constructor.codeService) {
                this.data = ROOMS().data.get(tx, ty);
                this.coo.set(tx, ty);
                this.ins = blue.get(tx, ty);
                return this;
            }
        }
        return null;
    }

    private void save() {

        int old = ROOMS().data.get(coo);

        if (old != data) {
            int current = data;
            data = old;
            ins.service.report(this, ins.blueprintI().data, -1);
            data = current;
            ROOMS().data.set(ins, coo, data);
            ins.service.report(this, ins.blueprintI().data, 1);

        }
    }

    private int state() {
        return data & 0b01111;
    }

    private void stateSet(int state) {
        data &= ~0b01111;
        data |= state;
        save();
    }

    @Override
    public void consume() {
        stateSet(AVAILABLE);
    }

    @Override
    public void startUsing() {
        stateSet(USED);
    }

    @Override
    public int x() {
        return coo.x();
    }

    @Override
    public int y() {
        return coo.y();
    }

    @Override
    public boolean findableReservedCanBe() {
        return state() == AVAILABLE;
    }

    @Override
    public void findableReserve() {
        if (state() != AVAILABLE)
            throw new RuntimeException();
        stateSet(RESERVED);

    }

    @Override
    public boolean findableReservedIs() {
        return state() == RESERVED || state() == USED;
    }

    @Override
    public void findableReserveCancel() {
        if (state() == RESERVED || state() == USED)
            stateSet(AVAILABLE);

    }

    void dispose(){
        if (findableReservedCanBe())
            findableReserve();
    }

    void init(int tx, int ty) {
        if (get(tx, ty) != null)
            ins.service.report(this, ins.blueprintI().data, 1);
    }

    public boolean isUsed(int tile) {
        data = SETT.ROOMS().data.get(tile);
        return state() == USED;
    }



}

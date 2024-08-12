package settlement.room.service.hygine.well;

import java.io.IOException;

import game.time.TIME;
import init.C;
import settlement.main.SETT;
import settlement.path.AVAILABILITY;
import settlement.room.main.Room;
import settlement.room.main.RoomBlueprintImp;
import settlement.room.main.TmpArea;
import settlement.room.main.furnisher.Furnisher;
import settlement.room.main.furnisher.FurnisherItem;
import settlement.room.main.furnisher.FurnisherItemTile;
import settlement.room.main.furnisher.FurnisherStat;
import settlement.room.main.util.RoomInit;
import settlement.room.main.util.RoomInitData;
import settlement.room.sprite.RoomSprite;
import settlement.room.sprite.RoomSprite1x1;
import settlement.room.sprite.RoomSpriteCombo;
import snake2d.CORE;
import snake2d.SPRITE_RENDERER;
import snake2d.util.color.COLOR;
import snake2d.util.color.ColorImp;
import snake2d.util.datatypes.AREA;
import snake2d.util.datatypes.COORDINATE;
import snake2d.util.datatypes.DIR;
import snake2d.util.file.Json;
import snake2d.util.rnd.RND;
import util.rendering.RenderData.RenderIterator;
import util.rendering.ShadowBatch;

final class Constructor extends Furnisher{

    final FurnisherStat workers;
    final FurnisherStat efficiency;
    final FurnisherStat output;
    private final ROOM_WELL blue;
    final static int B_WORK = 1;
    final static int B_FETCH = 3;
    final static int B_STORAGE = 2;

    final FurnisherStat services = new FurnisherStat.FurnisherStatI(this);
    private static final Founatain fountain = new Founatain();
    static final int codeService = 1;

    protected Constructor(ROOM_WELL blue, RoomInitData init)
            throws IOException {
        super(init, 1, 4, 88, 44);
        this.blue = blue;

        workers = new FurnisherStat.FurnisherStatEmployees(this) {
            @Override
            public double get(AREA area, double acc) {
                int am = 0;
                for (COORDINATE c : area.body()) {
                    if (area.is(c) && (SETT.ROOMS().fData.tileData.get(c) == B_WORK || SETT.ROOMS().fData.tileData.get(c) == B_FETCH))
                        am++;
                }
                return am;
            }
        };
        efficiency = new FurnisherStat.FurnisherStatEfficiency(this, workers, 2);
        output = new FurnisherStat.FurnisherStatProduction2(this, blue) {
            @Override
            protected double getBase(AREA area, double[] fromItems) {
                return workers.get(area, fromItems)*efficiency.get(area, fromItems);
            }
        };

        Json sp = init.data().json("SPRITES");

        RoomSpriteCombo sStencil = new RoomSpriteCombo(sp, "STONE_RING_STENCIL_COMBO");
        RoomSprite sRoof = new RoomSprite1x1(sp, "ROOF_EDGE_1X1") {

            @Override
            public boolean render(SPRITE_RENDERER r, ShadowBatch s, int data, RenderIterator it, double degrade,
                                  boolean isCandle) {

                FurnisherItem i = SETT.ROOMS().fData.item.get(it.tile());
                if ((i.width()&1) == 0)
                    it.setOff(0, -C.TILE_SIZEH);
                return super.render(r, s, data, it, degrade, isCandle);
            }

            @Override
            protected boolean joins(int tx, int ty, int rx, int ry, DIR d, FurnisherItem item) {
                if (rx-d.x() >= item.width()/2)
                    return d.x() < 0;
                return d.x() > 0;
            }
        };

        RoomSprite sRoofMid = new RoomSprite1x1(sp, "ROOF_MID_1X1") {

            @Override
            public boolean render(SPRITE_RENDERER r, ShadowBatch s, int data, RenderIterator it, double degrade,
                                  boolean isCandle) {
                FurnisherItem i = SETT.ROOMS().fData.item.get(it.tile());
                if ((i.width()&1) == 0)
                    it.setOff(0, -C.TILE_SIZEH);
                return super.render(r, s, data, it, degrade, isCandle);
            }

            @Override
            protected boolean joins(int tx, int ty, int rx, int ry, DIR d, FurnisherItem item) {
                return d.x() > 0;
            }
        };

        RoomSprite sFountain = new RoomSprite1x1(sp, "FOUNTAIN_1X1") {

            @Override
            public boolean render(SPRITE_RENDERER r, ShadowBatch s, int data, RenderIterator it, double degrade,
                                  boolean isCandle) {
                fountain.render(r, s, it.x()+C.TILE_SIZEH, it.y()+C.TILE_SIZEH);
                return super.render(r, s, data, it, degrade, isCandle);
            }

        };

        RoomSprite sWellR = new RoomSpriteCombo(sp, "STONE_RING_COMBO") {



            @Override
            public boolean render(SPRITE_RENDERER r, ShadowBatch s, int data, RenderIterator it, double degrade,
                                  boolean isCandle) {
                if (blue.is(it.tile())) {
                    sStencil.render(r, s, data, it, degrade, false);
                    SETT.TERRAIN().WATER.renderOverlayed(it);
                }
                it.countWater();
                it.countWater();
                return false;
            }

            @Override
            public void renderAbove(SPRITE_RENDERER r, ShadowBatch s, int data, RenderIterator it, double degrade) {
                super.render(r, s, data, it, degrade, false);
                int up = blue.is(it.tile()) && blue.getter.get(it.tile()).upgrade() > 0 ? 1 : 0;
                RoomSprite roo = eSprite(SETT.ROOMS().fData.item.get(it.tile()), data, up);
                if (roo != null) {
                    roo.render(r, s, getData2(it), it, degrade, false);
                }
            }

            @Override
            public byte getData2(int tx, int ty, int rx, int ry, FurnisherItem item, int itemRan) {
                RoomSprite roo = eSprite(item, getData(tx, ty, rx, ry, item, itemRan), 0);
                if (roo != null)
                    return roo.getData(tx, ty, rx, ry, item, itemRan);
                return 0;
            }

            private RoomSprite eSprite(FurnisherItem item, int data, int up) {
                if (item.width() == 4) {
                    if ((data & DIR.S.mask()) == 0) {
                        return sRoof;
                    }
                }else if (item.width() == 5) {
                    if (up > 0) {
                        if ((data&0x0F) == 0x0F)
                            return sFountain;
                    }else {
                        if ((data & DIR.S.mask()) != 0 && (data & DIR.N.mask()) != 0) {
                            if ((data&0x0F) == 0x0F)
                                return sRoofMid;
                            return sRoof;
                        }
                    }


                }

                return null;


            }

        };

        final RoomSprite sService = new RoomSprite1x1(sp, "BUCKET_1X1") {

            @Override
            protected boolean joins(int tx, int ty, int rx, int ry, DIR d, FurnisherItem item) {
                rx -= d.x()*2;
                ry -= d.y()*2;
                return item.get(rx, ry) == null;
            }

            @Override
            public boolean render(SPRITE_RENDERER r, ShadowBatch s, int data, RenderIterator it, double degrade,
                                  boolean isCandle) {
                if (blue.is(it.tile())) {
                    if (blue.bed.isUsed(it.tile()))
                        return super.render(r, s, data, it, degrade, isCandle);
                }
                return false;
            }

        };

        final FurnisherItemTile ww = new FurnisherItemTile(
                this,
                sWellR,
                AVAILABILITY.SOLID,
                false);
        final FurnisherItemTile ss = new FurnisherItemTile(
                this,
                false,
                sService,
                AVAILABILITY.ROOM,
                false).setData(codeService);

        final FurnisherItemTile __ = new FurnisherItemTile(
                this,
                false,
                null,
                AVAILABILITY.ROOM,
                false);

        final FurnisherItemTile sk = new FurnisherItemTile(
                this,
                true,
                null,
                AVAILABILITY.ROOM,
                false)
                .setData(B_STORAGE);


        new FurnisherItem(new FurnisherItemTile[][] {
                {sk,ss,__},
                {ss,ww,ss},
                {__,ss,__},
        }, 1, 1);

        new FurnisherItem(new FurnisherItemTile[][] {
                {sk,ss,ss,__},
                {ss,ww,ww,ss},
                {ss,ww,ww,ss},
                {__,ss,ss,__},
        }, 4, 2);

        new FurnisherItem(new FurnisherItemTile[][] {
                {sk,ss,ss,ss,__},
                {ss,ww,ww,ww,ss},
                {ss,ww,ww,ww,ss},
                {ss,ww,ww,ww,ss},
                {__,ss,ss,ss,__},
        }, 10, 3);

        flush(1, 1);

    }

    @Override
    public boolean usesArea() {
        return false;
    }

    @Override
    public boolean mustBeIndoors() {
        return false;
    }

    @Override
    public Room create(TmpArea area, RoomInit init) {
        return new WellInstance(blue, area, init);
    }

    @Override
    public RoomBlueprintImp blue() {
        return blue;
    }


//	private final FurnisherMinimapColor miniC = new FurnisherMinimapColor(new byte[][] {
//		{0,0,0,0,0,0,0,0},
//		{0,0,0,0,0,0,0,0},
//		{0,0,0,1,1,0,0,0},
//		{0,1,1,1,1,1,1,0},
//		{0,1,1,1,1,1,1,0},
//		{0,1,1,1,1,1,1,0},
//		{0,0,0,0,0,0,0,0},
//		{0,0,0,0,0,0,0,0},
//		},
//		miniColor
//	);
//
//	@Override
//	public COLOR miniColor(int tx, int ty) {
//		return miniC.get(tx, ty);
//	}

    private static class Founatain {

        private final int AM = 64;

        private byte[] xs = new byte[AM];
        private byte[] ys = new byte[AM];
        private double[] rans = new double[AM];
        private COLOR[] cols = new COLOR[AM];


        Founatain(){


            for (int i = 0; i < AM; i++) {
                double rad = RND.rFloat()*Math.PI*0.5;
                double dx = Math.cos(rad);
                double dy = Math.sin(rad);
                xs[i] = (byte) (dx*(C.TILE_SIZEH/2+RND.rFloat()*C.TILE_SIZE));
                ys[i] = (byte) (dy*(C.TILE_SIZEH/2+RND.rFloat()*C.TILE_SIZE));
                rans[i] = RND.rInt(128) + RND.rFloat();

            }
            cols = COLOR.interpolate(new ColorImp(20, 60, 127), COLOR.WHITE100, AM);

        }

        void render(SPRITE_RENDERER r, ShadowBatch s, int cx, int cy) {
            double time = TIME.currentSecond()*1.5;
            render(r, s, cx, cy, time, 1, 1);
            time += 0.3;
            render(r, s, cx-C.SCALE, cy, time, -1, 1);
            time += 0.3;
            render(r, s, cx, cy-C.SCALE, time, 1, -1);
            time += 0.3;
            render(r, s, cx-C.SCALE, cy-C.SCALE, time, -1, -1);
        }
        void render(SPRITE_RENDERER r, ShadowBatch s, int cx, int cy, double time, int dx, int dy) {
            int a = AM;
            if (TIME.light().nightIs()) {
                a *= 1.0-TIME.light().partOf()*10;
            }
            for (int i = 0; i < a; i++) {
                double d = rans[i] + time;
                int k = (int)d;
                d = d -k;
                int x = (int) (xs[i]*d);
                int y = (int) (ys[i]*d);
                cols[k&(AM-1)].bind();
                CORE.renderer().renderParticle(cx+x*dx, cy+y*dy);
            }
        }

    }

}

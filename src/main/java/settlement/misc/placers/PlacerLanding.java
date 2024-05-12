package settlement.misc.placers;

import static settlement.main.SETT.FLOOR;
import static settlement.main.SETT.HUMANOIDS;
import static settlement.main.SETT.PATH;
import static settlement.main.SETT.ROOMS;
import static settlement.main.SETT.TERRAIN;
import static settlement.misc.placers.Tiles.__;

import game.boosting.BOOSTABLES;
import game.faction.FACTIONS;
import game.faction.FCredits;
import init.paths.PATHS;
import init.resources.RESOURCE;
import init.resources.RESOURCES;
import init.sprite.SPRITES;
import init.type.CAUSE_ARRIVES;
import init.type.HTYPES;
import init.type.POP_CL;
import settlement.entity.humanoid.Humanoid;
import settlement.main.SETT;
import settlement.misc.placers.Tiles.Conpound;
import settlement.room.food.farm.ROOM_FARM;
import settlement.stats.STATS;
import settlement.stats.standing.STANDINGS;
import settlement.tilemap.terrain.TBuilding;
import snake2d.util.file.Json;
import snake2d.util.rnd.RND;
import snake2d.util.sets.LIST;
import snake2d.util.sprite.SPRITE;
import view.main.VIEW;

final class PlacerLanding{


    static Placer get() {

        TBuilding b = SETT.TERRAIN().BUILDINGS.MUD;
        if (b == null)
            b = TERRAIN().BUILDINGS.all().get(0);

        Tile ww = new Tiles.Terrain(b.wall) {
            @Override
            public void place(int tx, int ty, TileGrid grid, int rx, int ry) {
                if (TERRAIN().CAVE.is(tx, ty)) {
                    TERRAIN().MOUNTAIN.placeFixed(tx, ty);
                }else
                    super.place(tx, ty, grid, rx, ry);
            }
        };
        Tile roof = new Tiles.Terrain(b.roof) {
            @Override
            public void place(int tx, int ty, TileGrid grid, int rx, int ry) {
                if (TERRAIN().CAVE.is(tx, ty))
                    return;
                super.place(tx, ty, grid, rx, ry);
            }
        };
        Tile cb = new Tiles.Floor(FLOOR().roads.get(4));
        Tile ff = new Tiles.Floor(FLOOR().defaultRoad);
        Tile rr = new Tiles.Conpound(roof, ff);

        Tile throne = new Tile() {
            @Override
            public SPRITE sprite(TileGrid grid, int rx, int ry, int mask) {
                return SPRITES.cons().ICO.cancel;
            }

            @Override
            public void place(int tx, int ty, TileGrid grid, int rx, int ry) {
                ROOMS().THRONE.init.place(tx, ty, 2);

                FACTIONS.player().credits().inc((int) (5000*BOOSTABLES.CIVICS().LANDING.get(POP_CL.clP(null, null))), FCredits.CTYPE.MISC);

            }

            @Override
            public boolean placable(int tx, int ty, TileGrid grid, int rx, int ry) {
                return ROOMS().THRONE.init.placebleWhole(tx, ty, 2);
            }
        };
        Tile th = new Conpound(roof,throne);

        Json j = new Json(PATHS.INIT().getFolder("config").get("LandingParty")).json("RESOURCES");
        LIST<String> keys = j.keys();

        Tile ST = new Conpound(rr, new Resource(RESOURCES.map().get(keys.get(0), j), j.i(keys.get(0), 1, 500)));
        Tile WW = new Conpound(rr, new Resource(RESOURCES.map().get(keys.get(1), j), j.i(keys.get(1), 1, 500)));
        Tile LS = new Conpound(rr, new Resource(RESOURCES.map().get(keys.get(2), j), j.i(keys.get(2), 1, 500)));
        Tile RA = new Conpound(rr, new Resource(RESOURCES.map().get(keys.get(3), j), j.i(keys.get(3), 1, 500)));
		Tile CL = new Conpound(rr, new Resource(RESOURCES.map().get(keys.get(4), j), j.i(keys.get(4), 1, 500)));
        Tile TO = new Conpound(rr, new Resource(RESOURCES.map().get(keys.get(5), j), j.i(keys.get(5), 1, 500)));
        Tile PK = new Conpound(rr, new Resource(RESOURCES.map().get(keys.get(6), j), j.i(keys.get(6), 1, 500)));
        RESOURCE grain = RESOURCES.map().tryGet("GRAIN");
        if (grain == null)
            grain = growable(3);

        Tile GR = new Conpound(rr, new Resource(grain, 40));


        Tile dd = new Tile() {

            private int am = -1;
            private int ePerTile = -1;
            @Override
            public SPRITE sprite(TileGrid grid, int rx, int ry, int mask) {
                return SPRITES.cons().ICO.cancel;
            }

            @Override
            public void place(int tx, int ty, TileGrid grid, int rx, int ry) {
                if (STATS.POP().POP.data(null).get(null) == 0) {
                    am = 12 + (int) (12*BOOSTABLES.CIVICS().LANDING.get(POP_CL.clP(null, null)));

                    ePerTile = (int) Math.ceil((am)/12.0);
                }
                for (int i = 0; i < ePerTile; i++) {
                    if (am <= 0)
                        continue;
                    Humanoid h = HUMANOIDS().create(FACTIONS.player().race(), tx, ty, HTYPES.SUBJECT(), CAUSE_ARRIVES.IMMIGRATED());
                    STATS.POP().TYPE.NATIVE.set(h.indu());
                    am--;
                }
                VIEW.messages().hide();

            }

            @Override
            public boolean placable(int tx, int ty, TileGrid grid, int rx, int ry) {
                return !PATH().solidity.is(tx, ty);
            }
        };


        Tile[][] grid = new Tile[][] {
                {ww,ww,ww,ww,ww,ww,ww,ww,ww,ww,ww,ww,ww,ww,ww},
                {ww,rr,rr,rr,rr,th,rr,rr,rr,rr,rr,rr,rr,rr,ww},
                {ww,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,ww},
                {ww,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,ww},
                {ww,rr,ST,LS,RA,rr,rr,rr,rr,rr,WW,GR,CL,rr,ww},
                {ww,rr,rr,TO,rr,rr,rr,rr,rr,rr,rr,PK,rr,rr,ww},
                {ww,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,rr,ww},
                {ww,ww,ww,ww,ww,ww,rr,rr,rr,ww,ww,ww,ww,ww,ww},
                {__,__,__,__,__,dd,cb,cb,cb,dd,__,__,__,__,__},
                {__,__,__,__,dd,dd,cb,cb,cb,dd,dd,__,__,__,__},
                {__,__,__,__,dd,dd,cb,__,cb,dd,dd,__,__,__,__},
                {__,__,__,__,__,dd,__,__,__,dd,__,__,__,__,__},
        };

        return new Placer("Landing Party", new TileGrid(grid)) {

            @Override
            public void afterPlaced(int tx1, int ty1) {
                STANDINGS.initAll();
            };
        };


    }

    private static RESOURCE growable(int bb) {

        int ri = RND.rInt(RESOURCES.ALL().size());

        for (int i = 0; i < RESOURCES.ALL().size(); i++) {
            RESOURCE r = RESOURCES.ALL().get((i+ri)%RESOURCES.ALL().size());
            if (RESOURCES.EDI().is(r)) {
                for (ROOM_FARM f : SETT.ROOMS().FARMS) {
                    if (f.crop.resource == r && f.isAvailable(SETT.ENV().climate()))
                        return r;
                }
            }
        }

        return RESOURCES.EDI().all().rnd().resource;
    }

    private static class Resource implements Tile {

        final RESOURCE r;
        final int amount;

        Resource(RESOURCE r, int amount){
            this.r = r;
            this.amount = amount;
        }


        @Override
        public boolean placable(int tx, int ty, TileGrid grid, int rx, int ry) {
            return true;
        }

        @Override
        public void place(int tx, int ty, TileGrid grid, int rx, int ry) {
            SETT.THINGS().resources.createPrecise(tx, ty, r, amount + (int)(amount*BOOSTABLES.CIVICS().LANDING.get(POP_CL.clP(null, null))));
        }

        @Override
        public SPRITE sprite(TileGrid grid, int rx, int ry, int mask) {
            return SPRITES.cons().ICO.clear;
        }

    }

}

package view.world.ui.army;

import game.GAME;
import game.faction.FACTIONS;
import game.time.TIME;
import init.D;
import init.boostable.*;
import init.config.Config;
import init.race.RACES;
import init.race.Race;
import init.sprite.SPRITES;
import settlement.main.SETT;
import settlement.stats.STATS;
import settlement.stats.StatsBattle.StatTraining;
import settlement.stats.StatsEquippables.EQUIPPABLE_MILITARY;
import snake2d.SPRITE_RENDERER;
import snake2d.util.color.COLOR;
import snake2d.util.color.ColorImp;
import snake2d.util.datatypes.DIR;
import snake2d.util.gui.GUI_BOX;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.clickable.CLICKABLE;
import snake2d.util.gui.renderable.RENDEROBJ;
import snake2d.util.sprite.SPRITE;
import util.data.DOUBLE;
import util.data.INT.IntImp;
import util.dic.DicArmy;
import util.dic.DicTime;
import util.gui.common.BitmapSpriteEditor;
import util.gui.misc.*;
import util.gui.misc.GMeter.GMeterSprite;
import util.gui.slider.GAllocator;
import util.info.GFORMAT;
import view.interrupter.ISidePanel;
import view.main.VIEW;
import world.WORLD;
import world.army.WARMYD;
import world.army.WDivRegional;

class UIArmyRecruit extends ISidePanel{

    private static CharSequence ¤¤NoMapnpower = "¤Insufficient Manpower!";
    private static CharSequence ¤¤Note = "¤NOTE: once a division has finished training, it will need to be supplied from your army supply depot. Without supplies, the men will desert you.";

    {
        D.ts(this.getClass());
        titleSet(DicArmy.¤¤Recruit);
    }

    private Race race = GAME.player().race();
    private final IntImp amount = new IntImp(0, 9);
    private final IntImp[] training = new IntImp[STATS.BATTLE().TRAINING_ALL.size()];

    private final IntImp[] gear = new IntImp[STATS.EQUIP().military_all().size()];
    private final BannerEditor ee = new BannerEditor();

    public int men() {

        return (int) Math.ceil(Config.BATTLE.MEN_PER_DIVISION*(amount.get()+1)/10.0);
    }

    UIArmyRecruit() {

        for (int i = 0; i < gear.length; i++) {
            gear[i] = new IntImp(0, STATS.EQUIP().military_all().get(i).max());
        }

        for (int i = 0; i < training.length; i++) {
            training[i] = new IntImp(0, STATS.EQUIP().military_all().get(i).max()) {
                @Override
                public int max() {
                    int am = 5;
                    for(IntImp ii : training)
                        if (ii != this)
                            am -= ii.get();
                    return am;
                };
            };
        }

        for (int ri = 0; ri < RACES.all().size(); ri++) {

            Race r = RACES.all().get(ri);

            if (r.population().max <= 0)
                continue;

            GButt.BSection c = new GButt.BSection() {

                @Override
                protected void clickA() {
                    race = r;
                }

                @Override
                protected void renAction() {
                    selectedSet(race == r);
                }

                @Override
                public void hoverInfoGet(GUI_BOX text) {
                    GBox b = (GBox) text;
                    b.title(r.info.names);
                    b.textL(DicArmy.¤¤Conscriptable);
                    b.add(GFORMAT.i(b.text(), WARMYD.conscriptable(r).get(FACTIONS.player()) - WARMYD.conscriptableInService(r).get(FACTIONS.player())));
                    b.NL(8);
                    b.text(r.info.desc);
                    b.NL(8);

                    for (BOOSTABLE bo : BOOSTABLES.BATTLE().all()) {
                        b.add(bo.icon());
                        b.textL(bo.name);
                        b.tab(6);
                        b.add(GFORMAT.f(b.text(), bo.race(r)));
                        b.NL();
                    }

                }


            };


            c.add(r.appearance().iconBig, 0 , 0);
            c.addDownC(4, new GStat() {

                @Override
                public void update(GText text) {
                    GFORMAT.i(text,  WARMYD.conscriptable(r).get(FACTIONS.player()) - WARMYD.conscriptableInService(r).get(FACTIONS.player()));
                }
            }.r(DIR.C));
            c.pad(10, 4);

            section.add(c, (ri%6)*(c.body().width()+2), (ri/6)*(c.body().height()+2));

        }

        {
            GuiSection row = new GuiSection();
            GuiSection s = new GuiSection() {
                @Override
                public void hoverInfoGet(GUI_BOX text) {
                    GBox b = (GBox) text;
                    b.title(DicArmy.¤¤SoldiersTarget);
                    b.add(GFORMAT.i(b.text(), men()));
                }
            };
            s.add(SPRITES.icons().m.citizen, 0, 0);
            s.addRightC(4, new GAllocator(COLOR.GREEN100.makeSaturated(0.7), amount, 6, 12));
            row.add(s);

            for (StatTraining t : STATS.BATTLE().TRAINING_ALL) {
                s = new GuiSection() {
                    @Override
                    public void hoverInfoGet(GUI_BOX text) {
                        GBox b = (GBox) text;
                        b.title(t.info().name);
                        b.add(GFORMAT.perc(b.text(), training[t.tIndex].get()/15.0));
                        b.NL();
                        b.text(t.info().desc);
                    }
                };
                s.add(t.icon, 0, 0);
                s.addRightC(4, new GAllocator(COLOR.RED100.makeSaturated(0.7), training[t.tIndex], 6, 12));

                row.addRightC(16, s);
            }





            s = new GuiSection();

            for (EQUIPPABLE_MILITARY m : STATS.EQUIP().military_all()) {
                GAllocator g = new GAllocator(COLOR.ORANGE100.makeSaturated(0.7), gear[m.indexMilitary()], 6, 12);
                RENDEROBJ ss = new GStat() {

                    @Override
                    public void update(GText text) {
                        GFORMAT.i(text, men()*gear[m.indexMilitary()].get());
                    }

                    @Override
                    public void hoverInfoGet(GBox b) {
                        m.hover(b);
                    };

                }.hh(m.resource().icon());

                CLICKABLE c = new CLICKABLE.Pair(ss, g, DIR.S, 4);
                s.addC(c, (m.indexMilitary()%12)*100, (m.indexMilitary()/14)*100);
            }
            row.addRelBody(8, DIR.S, s);

            {
                s = new GuiSection();
                for (BOOSTABLE b : BOOSTABLES.military()) {
                    s.addDown(2, new Boost(b));
                }
                row.addRelBody(8, DIR.S, s);
            }



            section.addRelBody(8, DIR.S, row);
            row = new GuiSection();

            {


                SPRITE sp = new SPRITE.Imp(32) {

                    @Override
                    public void render(SPRITE_RENDERER r, int X1, int X2, int Y1, int Y2) {
                        SETT.ARMIES().banners.get(ee.bannerI).render(r, X1+2, Y1+2);

                    }
                };


                row.addRightC(0, new GButt.ButtPanel(sp) {

                    @Override
                    protected void clickA() {
                        VIEW.inters().popup.show(ee.view(), this);
                    }

                }.hoverTitleSet(DicArmy.¤¤Banner));
            }

            GStat ss = new GStat() {

                @Override
                public void update(GText text) {

                    GFORMAT.i(text, WDivRegional.trainingDays(training[0].get(), training[1].get(), (int) (amount.getD()*Config.BATTLE.MEN_PER_DIVISION)));
                }

                @Override
                public void hoverInfoGet(GBox b) {
                    b.title(DicArmy.¤¤RecruitmentTime);
                    GText t = b.text();

                    DicTime.setTime(t, WDivRegional.trainingDays(training[0].get(), training[1].get(), (int) (amount.getD()*Config.BATTLE.MEN_PER_DIVISION))*TIME.secondsPerDay);
                    b.add(t);
                }


            };

            row.addRightC(48, ss.hh(SPRITES.icons().m.time));

            GButt b = new GButt.ButtPanel(DicArmy.¤¤Recruit) {

                @Override
                protected void clickA() {
                    if (UIArmy.army.divs().canAdd()) {
                        WDivRegional d = WORLD.ARMIES().regional().create(race, (1 + amount.get())/10.0, training[0].get(), training[1].get(), UIArmy.army);
                        d.bannerSet(ee.bannerI);
                        for (int i = 0; i < gear.length; i++)
                            d.equipTargetset(STATS.EQUIP().military_all().get(i), gear[i].get());
                    }
                }

                @Override
                protected void renAction() {
                    activeSet(men() <= WARMYD.conscriptable(race).get(FACTIONS.player()) - WARMYD.conscriptableInService(race).get(FACTIONS.player()));
                }

                @Override
                public void hoverInfoGet(GUI_BOX text) {
                    GBox b = (GBox) text;
                    if (men() > WARMYD.conscriptable(race).get(FACTIONS.player()) -WARMYD.conscriptableInService(race).get(FACTIONS.player())) {
                        b.error(¤¤NoMapnpower);
                    }else {
                        b.text(¤¤Note);
                    }


                }

            };

            row.addRightC(48, b);

            section.addRelBody(8, DIR.S, row);

        }




    }

    static class BannerEditor {

        private int bannerI = 0;
        GuiSection pop = new GuiSection();

        BannerEditor(){


            BitmapSpriteEditor ee = new BitmapSpriteEditor();
            ee.spriteSet(SETT.ARMIES().banners.get(0).sprite);

            for (int i = 0; i < SETT.ARMIES().banners.size(); i++) {
                final int k = i;
                pop.add(new GButt.ButtPanel(SETT.ARMIES().banners.get(i)){

                    @Override
                    protected void clickA() {
                        bannerI = k;
                        bannerSet(k);
                        ee.spriteSet(SETT.ARMIES().banners.get(k).sprite);
                    }

                    @Override
                    protected void renAction() {
                        selectedSet(bannerI == k);
                    }

                }, (i%8)*40, (i/8)*40);
            }

            pop.addRelBody(8, DIR.S, ee);

            pop.addRelBody(8, DIR.S, new GColorPicker(false) {

                @Override
                public ColorImp color() {
                    return SETT.ARMIES().banners.get(bannerI).col;
                }
            });

            pop.addRelBody(8, DIR.S, new GColorPicker(false) {

                @Override
                public ColorImp color() {
                    return SETT.ARMIES().banners.get(bannerI).bg;
                }
            });
        }

        public int bannerI() {
            return bannerI;
        }

        public void bannerISet(int i) {
            bannerI = i;
        }

        public RENDEROBJ view() {
            return pop;
        }

        public void bannerSet(int i) {

        }

    }

    private class Boost extends GuiSection {

        private final double max;
        private final BOOSTABLE b;
        Boost(BOOSTABLE b){
            max = BOOSTABLES.player().max(b);
            this.b = b;
            add(b.icon(), 0, 0);
            addRightC(8, new GHeader(b.name));
            addRightCAbs(200, new GStat() {

                @Override
                public void update(GText text) {
                    GFORMAT.f0(text, get());
                }
            });
            DOUBLE d = new DOUBLE() {

                @Override
                public double getD() {
                    double d = get();
                    if (max > 0)
                        return d/max;
                    return 0;
                }
            };
            GMeterSprite g = new GMeter.GMeterSprite(GMeter.C_BLUE, d, 100, 16);
            addRightC(64, g);
        }

        private double get() {
            double mul = race.bonus().mul(b);
            double add = race.bonus().add(b);

            mul *= FACTIONS.player().bonus().mul(b);
            add += FACTIONS.player().bonus().add(b);

            for (EQUIPPABLE_MILITARY mi : STATS.EQUIP().military_all()) {
                for (BBooster bb : mi.boosts()) {
                    if (bb.boost.boostable == b) {
                        if (bb.boost.isMul())
                            mul *= 1 + (bb.boost.value()-1)*(double)gear[mi.indexMilitary()].get()/mi.max();
                        else
                            add += bb.boost.value()*(double)gear[mi.indexMilitary()].get()/mi.max();


                    }
                }
            }
            for (int i = 0; i < STATS.BATTLE().TRAINING_ALL.size(); i++) {
                StatTraining t = STATS.BATTLE().TRAINING_ALL.get(i);
                for (int bi = 0; bi < t.boosts().size(); bi++) {
                    BBooster bb = t.boosts().get(bi);
                    if (bb.boost.boostable == b) {
                        if (bb.boost.isMul())
                            mul *= 1 + (bb.boost.value()-1)*(double)training[t.tIndex].get()/t.indu().max(null);
                        else
                            add += bb.boost.value()*(double)training[t.tIndex].get()/t.indu().max(null);

                    }
                }
            }
            return add*mul;
        }

        @Override
        public void hoverInfoGet(GUI_BOX text) {
            text.title(b.name);
            text.text(b.desc);
        }


    }

}

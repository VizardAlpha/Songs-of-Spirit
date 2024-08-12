package settlement.room.service.hygine.well;

import init.D;
import init.resources.RESOURCES;
import init.resources.ResG;
import snake2d.util.datatypes.DIR;
import snake2d.util.gui.GUI_BOX;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.renderable.RENDEROBJ;
import util.data.GETTER;
import util.gui.misc.GBox;
import util.gui.misc.GButt;
import util.gui.misc.GGrid;
import util.gui.misc.GStat;
import util.gui.misc.GText;
import util.info.GFORMAT;
import view.sett.ui.room.UIRoomModule.UIRoomModuleImp;

class Gui extends UIRoomModuleImp<WellInstance, ROOM_WELL> {

    private final CharSequence ¤¤Water = "¤Water";

    Gui(ROOM_WELL s) {
        super(s);
        D.t(this);
    }

    @Override
    protected void appendPanel(GuiSection section, GGrid grid, GETTER<WellInstance> g, int x1, int y1) {

        GuiSection s = new GuiSection();
        int i = 0;
        for (ResG e : RESOURCES.EDI().all()) {

            GButt.BSection ss = new GButt.BSection() {

                @Override
                public void hoverInfoGet(GUI_BOX text) {
                    GBox b = (GBox) text;
                    b.title(e.resource.name);
                    b.textLL(¤¤Water).add(GFORMAT.i(b.text(), g.get().amount(e)));
                }

                @Override
                protected void renAction() {
                    selectedSet(g.get().uses(e));
                }

                @Override
                protected void clickA() {
                    g.get().usesToggle(e);
                }
            };


            ss.addRightC(4, e.resource.icon());

            ss.addRightC(4, new GStat() {

                @Override
                public void update(GText text) {
                    GFORMAT.i(text, g.get().amount(e));
                }

                @Override
                public void hoverInfoGet(GBox b) {
                    b.title(e.resource.name);
                    b.textLL(¤¤Water).add(GFORMAT.i(b.text(), g.get().amount(e)));
                };
            });

            ss.body().incrW(48);
            ss.pad(4);

            s.add(ss, (i%3)*ss.body().width(), (i/3)*ss.body().height());
            i++;

        }

        s.addRelBody(2, DIR.N, new GStat() {

            @Override
            public void update(GText text) {
                GFORMAT.iofk(text, g.get().amountTotal(), g.get().maxAmount*RESOURCES.EDI().all().size());

            }
        }.hh(¤¤Water));

        section.addRelBody(8, DIR.S, s);

    }

    @Override
    protected void hover(GBox b, WellInstance i) {
        b.NL();
        b.textLL(¤¤Water).add(GFORMAT.i(b.text(), i.amountTotal()));
        b.NL();
    }

    @Override
    protected void appendMain(GGrid gg, GGrid text, GuiSection sExtra) {
        GuiSection s = new GuiSection();
        int i = 0;

        text.add(s);

    }

}

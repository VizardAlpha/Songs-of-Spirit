package vizardalpha.songsofspirit.ui.info;

import init.C;
import snake2d.MButt;
import snake2d.Renderer;
import snake2d.util.datatypes.COORDINATE;
import snake2d.util.datatypes.DIR;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.clickable.CLICKABLE;
import util.gui.misc.GBox;
import util.gui.misc.GButt;
import util.gui.panel.GPanelL;
import view.interrupter.Interrupter;
import view.main.VIEW;
import vizardalpha.songsofspirit.ui.info.model.ChangelogsStore;

import java.util.LinkedHashMap;
import java.util.Map;

public class InfoModal extends Interrupter {

    private final GuiSection section;

    private final CLICKABLE.Switcher switcher;

    private final Map<String, GuiSection> panels = new LinkedHashMap<>();

    public final static int WIDTH = 800;
    public final static int HEIGHT = 600;

    public InfoModal(ChangelogsStore changelogsStore) {
        GPanelL pan = new GPanelL();
        pan.body.setDim(WIDTH, HEIGHT);
//        D.t(this);
        pan.setTitle("Songs of Spirit Info");
        pan.setCloseAction(this::hide);

        pan.body().centerIn(C.DIM());

        this.section = new GuiSection();
        section.add(pan);

        Credits credits = new Credits();
        Changelogs changelogs = new Changelogs(changelogsStore, WIDTH, HEIGHT - 50);

        panels.put("Changelog", changelogs);
        panels.put("Credits", credits);

        GuiSection s = picker();
        s.body().moveY1(pan.getInnerArea().y1());
        s.body().centerX(pan);
        section.add(s);

        changelogs.body().moveY1(s.body().y2() + 16);
        changelogs.body().centerX(s);

        switcher = new CLICKABLE.Switcher(changelogs);
        section.add(switcher);
    }

    @Override
    protected boolean hover(COORDINATE mCoo, boolean mouseHasMoved) {
        return section.hover(mCoo);
    }

    @Override
    protected void mouseClick(MButt button) {
        if (button == MButt.RIGHT)
            hide();
        else if (button == MButt.LEFT)
            section.click();
    }

    @Override
    protected void hoverTimer(GBox text) {
        section.hoverInfoGet(text);
    }

    @Override
    protected boolean render(Renderer r, float ds) {
        section.render(r, ds);
        return true;
    }

    @Override
    protected boolean update(float ds) {
        return true;
    }

    @Override
    protected boolean otherClick(MButt button) {
        hide();
        return true;
    }

    public void activate() {
        show(VIEW.inters().manager);
    }

    private GuiSection picker() {
        GuiSection section = new GuiSection();

        panels.forEach((title, panel) -> {
            section.addRightC(0, new GButt.ButtPanel(title) {
                @Override
                protected void clickA() {
                    switcher.set(panel, DIR.N);
                }

                @Override
                protected void renAction() {
                    selectedSet(switcher.get() == panel);
                }
            }.setDim(130, 32));
        });

        return section;
    }
}

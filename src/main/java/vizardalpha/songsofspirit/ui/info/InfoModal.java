package vizardalpha.songsofspirit.ui.info;

import game.VERSION;
import init.C;
import init.paths.ModInfo;
import init.paths.PATHS;
import init.sprite.UI.UI;
import snake2d.MButt;
import snake2d.Renderer;
import snake2d.util.color.COLOR;
import snake2d.util.datatypes.COORDINATE;
import snake2d.util.datatypes.DIR;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.clickable.CLICKABLE;
import util.gui.misc.GBox;
import util.gui.misc.GButt;
import util.gui.misc.GText;
import util.gui.panel.GPanelL;
import view.interrupter.Interrupter;
import view.main.VIEW;
import vizardalpha.songsofspirit.SongsofSpirit;
import vizardalpha.songsofspirit.ui.info.model.ChangelogsStore;
import vizardalpha.songsofspirit.ui.info.model.CreditsStore;

import java.util.LinkedHashMap;
import java.util.Map;

public class InfoModal extends Interrupter {

    private final GuiSection section;

    private final CLICKABLE.Switcher switcher;

    private final Map<String, GuiSection> panels = new LinkedHashMap<>();

    public final static int WIDTH = 800;
    public final static int HEIGHT = 600;

    public InfoModal(ChangelogsStore changelogsStore, CreditsStore creditsStore) {
        GPanelL pan = new GPanelL();
        pan.body.setDim(WIDTH, HEIGHT);
//        D.t(this);
        pan.setTitle("Songs of Spirit Info");
        pan.setCloseAction(this::hide);

        pan.body().centerIn(C.DIM());

        this.section = new GuiSection();
        section.add(pan);

        Credits credits = new Credits(creditsStore, WIDTH, HEIGHT - 50);
        Changelogs changelogs = new Changelogs(changelogsStore, WIDTH, HEIGHT - 50);

        panels.put("Changelog", changelogs);
        panels.put("Credits", credits);

        GuiSection picker = picker();
        picker.body().moveY1(pan.getInnerArea().y1());
        picker.body().centerX(pan);
        section.add(picker);

        changelogs.body().moveY1(picker.body().y2() + 16);
        changelogs.body().centerX(picker);

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

        GuiSection versions = versions();
        int space = WIDTH - section.body().width() - versions.body().width();
        section.addRightC(space, versions);

        return section;
    }

    private GuiSection versions() {
        COLOR color = COLOR.WHITE50;
        GuiSection versions = new GuiSection();

        GText gameVersion = new GText(UI.FONT().S, "Game Version: " + VERSION.VERSION_STRING);
        gameVersion.color(color);

        String modVersionString = "0.0.0";
        for (ModInfo currentMod : PATHS.currentMods()) {
            if (SongsofSpirit.MOD_INFO.name.equals(currentMod.name)) {
                modVersionString = currentMod.version;
            }
        }

        GText modVersion = new GText(UI.FONT().S, "Mod Version: " + modVersionString);
        modVersion.color(color);

        versions.addDown(0, gameVersion);
        versions.addDown(2, modVersion);

        return versions;
    }
}

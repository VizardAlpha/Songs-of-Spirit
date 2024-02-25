package vizardalpha.songsofspirit.ui.info;

import game.VERSION;
import init.C;
import init.paths.ModInfo;
import init.sprite.UI.UI;
import lombok.Getter;
import snake2d.MButt;
import snake2d.Renderer;
import snake2d.util.color.COLOR;
import snake2d.util.datatypes.COORDINATE;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.clickable.CLICKABLE;
import util.gui.misc.GBox;
import util.gui.misc.GButt;
import util.gui.misc.GText;
import util.gui.panel.GPanel;
import view.interrupter.Interrupter;
import view.main.VIEW;
import vizardalpha.songsofspirit.ui.info.store.ChangelogsStore;
import vizardalpha.songsofspirit.ui.info.store.CreditsStore;

import java.util.LinkedHashMap;
import java.util.Map;

public class InfoModal extends Interrupter {

    private final GuiSection section;

    private final CLICKABLE.ClickSwitch switcher;

    private final Map<String, GuiSection> panels = new LinkedHashMap<>();

    private final ModInfo modInfo;

    public final static int WIDTH = 800;
    public final static int HEIGHT = 600;
    @Getter
    private GButt.ButtPanel discordButton;
    @Getter
    private GButt.ButtPanel githubButton;

    public InfoModal(ChangelogsStore changelogsStore, CreditsStore creditsStore, ModInfo modInfo) {
        this.modInfo = modInfo;
        GPanel pan = new GPanel();
        pan.body.setDim(WIDTH, HEIGHT);
        pan.setTitle("Songs of Spirit Info");
        pan.setCloseAction(this::hide);

        pan.body().centerIn(C.DIM());

        this.section = new GuiSection();
        section.add(pan);

        Credits credits = new Credits(creditsStore, WIDTH, HEIGHT - 75);
        Changelogs changelogs = new Changelogs(changelogsStore, WIDTH, HEIGHT - 75);

        panels.put("Changelogs", changelogs);
        panels.put("Credits", credits);

        GuiSection header = header();
        header.body().moveY1(pan.inner().y1());
        header.body().centerX(pan);
        section.add(header);

        changelogs.body().moveY1(header.body().y2() + 16);
        changelogs.body().centerX(header);

        switcher = new CLICKABLE.ClickSwitch(changelogs);
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

    private GuiSection header() {
        GuiSection section = new GuiSection();

        panels.forEach((title, panel) -> {
            section.addRightC(0, new GButt.ButtPanel(title) {
                @Override
                protected void clickA() {
                    switcher.set(panel);
                }

                @Override
                protected void renAction() {
                    selectedSet(switcher.current() == panel);
                }
            }.setDim(136, 32));
        });

        githubButton = new GButt.ButtPanel("Translation");
        discordButton = new GButt.ButtPanel("Discord");
        GuiSection versions = versions();
        int space = WIDTH - section.body().width() - versions.body().width() - discordButton.body().width() - 155;

        section.addRightC(space, discordButton);
        section.addRightC(0, githubButton);
        section.addRightC(10, versions);

        return section;
    }

    private GuiSection versions() {
        COLOR color = COLOR.WHITE50;
        GuiSection versions = new GuiSection();

        GText gameVersion = new GText(UI.FONT().S, "Game Version: " + VERSION.VERSION_STRING);
        gameVersion.color(color);

        String modVersionString = "0.0.0";
        if (modInfo != null) {
            modVersionString = modInfo.version;
        }

        GText modVersion = new GText(UI.FONT().S, "Mod Version: " + modVersionString);
        modVersion.color(color);

        versions.addDown(0, gameVersion);
        versions.addDown(2, modVersion);

        return versions;
    }
}

package vizardalpha.songsofspirit.ui;

import init.sprite.SPRITES;
import lombok.RequiredArgsConstructor;
import snake2d.util.datatypes.DIR;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.clickable.CLICKABLE;
import snake2d.util.sprite.SPRITE;
import util.gui.misc.GButt;
import view.ui.top.UIPanelTop;
import vizardalpha.songsofspirit.game.api.GameUiApi;
import vizardalpha.songsofspirit.log.Logger;
import vizardalpha.songsofspirit.log.Loggers;
import vizardalpha.songsofspirit.ui.info.InfoModal;
import vizardalpha.songsofspirit.util.ReflectionUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static vizardalpha.songsofspirit.SongsofSpirit.MOD_INFO;

@RequiredArgsConstructor
public class UIGameConfig {

    private final static Logger log = Loggers.getLogger(GameUiApi.class);

    private final GameUiApi gameUiApi;

    private final InfoModal infoModal;

    public final static String SONGS_OF_SPIRIT_BUTTON_KEY = "songs-of-spirit";

    public void init() {
        log.debug("Initializing UI");

        CLICKABLE settlementButton = new SpiritInfoButton(SPRITES.icons().s.question, 32, UIPanelTop.HEIGHT) {
            @Override
            protected void clickA() {
                infoModal.activate();
            }
        }.hoverInfoSet(MOD_INFO.name);

        // needed for highlighting the button with a red line in the top navigation bar
        UISettMap.add(settlementButton, SONGS_OF_SPIRIT_BUTTON_KEY);

        gameUiApi.findUIElementInSettlementView(UIPanelTop.class)
            .flatMap(uiPanelTop -> ReflectionUtil.getDeclaredField("right", uiPanelTop))
            .ifPresent(o -> {
                log.debug("Injecting into UIPanelTop#right in settlement view");
                GuiSection right = (GuiSection) o;
                right.addRelBody(8, DIR.W, settlementButton);
            });

        infoModal.getDiscordButton().clickActionSet(() -> {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI url;
            try {
                url = new URI("https://discord.gg/KCarMbDtJz");
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            try {
                desktop.browse(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        infoModal.getGithubButton().clickActionSet(() -> {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI url;
            try {
                url = new URI("https://github.com/VizardAlpha/Songs-of-Spirit-Translation");
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            try {
                desktop.browse(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static class SpiritInfoButton extends GButt.ButtPanel{

        public SpiritInfoButton(SPRITE label, int width, int height) {
            super(label);
            body.setHeight(height);
            body.setWidth(width);
        }

        public SpiritInfoButton(SPRITE label) {
            super(label);
        }
    }
}
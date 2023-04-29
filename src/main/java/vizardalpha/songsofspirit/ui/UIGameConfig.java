package vizardalpha.songsofspirit.ui;

import init.sprite.SPRITES;
import lombok.RequiredArgsConstructor;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.clickable.CLICKABLE;
import util.gui.misc.GButt;
import view.ui.UIPanelTop;
import vizardalpha.songsofspirit.game.api.GameUiApi;
import vizardalpha.songsofspirit.log.Logger;
import vizardalpha.songsofspirit.log.Loggers;

import static vizardalpha.songsofspirit.SongsofSpirit.MOD_INFO;

@RequiredArgsConstructor
public class UIGameConfig {

    private final static Logger log = Loggers.getLogger(GameUiApi.class);

    private final GameUiApi gameUiApi;

    public void init() {
        log.debug("Initializing UI");
        gameUiApi.findUIElement(UIPanelTop.class).ifPresent(uiPanelTop -> {
            log.debug("Injecting into UIPanelTop in settlement view");

            CLICKABLE button = new GButt.ButtPanel(SPRITES.icons().m.heart) {
                @Override
                protected void clickA() {
//                    gameUiApi.showPanel(panel, false);
                }
            }.hoverInfoSet(MOD_INFO.name);

            uiPanelTop.addRight(new GuiSection().add(button));


        });
    }
}

package vizardalpha.songsofspirit.ui;

import init.sprite.SPRITES;
import lombok.RequiredArgsConstructor;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.clickable.CLICKABLE;
import util.gui.misc.GButt;
import view.ui.UIPanelTop;
import view.world.WorldIIMinimap;
import vizardalpha.songsofspirit.game.api.GameUiApi;
import vizardalpha.songsofspirit.log.Logger;
import vizardalpha.songsofspirit.log.Loggers;
import vizardalpha.songsofspirit.ui.info.InfoModal;
import vizardalpha.songsofspirit.util.ReflectionUtil;

import static vizardalpha.songsofspirit.SongsofSpirit.MOD_INFO;

@RequiredArgsConstructor
public class UIGameConfig {

    private final static Logger log = Loggers.getLogger(GameUiApi.class);

    private final GameUiApi gameUiApi;

    private final InfoModal infoModal;

    public void init() {
        log.debug("Initializing UI");

        CLICKABLE button = new GButt.ButtPanel(SPRITES.icons().m.heart) {
            @Override
            protected void clickA() {
                infoModal.activate();
            }
        }.hoverInfoSet(MOD_INFO.name);

        gameUiApi.findUIElementInSettlementView(UIPanelTop.class).ifPresent(uiPanelTop -> {
                log.debug("Injecting into UIPanelTop in settlement view");
                uiPanelTop.addRight(new GuiSection().add(button));
            });

        gameUiApi.findUIElementInWorldGeneratorView(WorldIIMinimap.class)
            .flatMap(worldIIMinimap -> ReflectionUtil.getDeclaredField("buttons", worldIIMinimap))
            .ifPresent(o -> {
                GuiSection buttons = (GuiSection) o;
                log.debug("Injecting into WorldIIMinimap#buttons");

                buttons.addDown(1, button);
             });
    }
}

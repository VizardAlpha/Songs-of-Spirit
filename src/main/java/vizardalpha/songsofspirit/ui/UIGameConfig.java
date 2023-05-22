package vizardalpha.songsofspirit.ui;

import init.sprite.SPRITES;
import lombok.RequiredArgsConstructor;
import snake2d.util.datatypes.DIR;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.clickable.CLICKABLE;
import snake2d.util.sprite.SPRITE;
import util.gui.misc.GButt;
import view.ui.UIPanelTop;
import view.world.WorldIIMinimap;
import vizardalpha.songsofspirit.game.api.GameUiApi;
import vizardalpha.songsofspirit.log.Logger;
import vizardalpha.songsofspirit.log.Loggers;
import vizardalpha.songsofspirit.ui.info.InfoModal;
import vizardalpha.songsofspirit.ui.quest.UIQuestLog;
import vizardalpha.songsofspirit.util.ReflectionUtil;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static vizardalpha.songsofspirit.SongsofSpirit.MOD_INFO;

@RequiredArgsConstructor
public class UIGameConfig {

    private final static Logger log = Loggers.getLogger(GameUiApi.class);

    private final GameUiApi gameUiApi;

    private final InfoModal infoModal;

    private final UIQuestLog questLog;

    public void init() {
        log.debug("Initializing UI");

        initInfoButton();
        initQuestButton();
        initDiscordButton();
    }

    private void initQuestButton() {
        CLICKABLE questButton = new SpiritButton(SPRITES.icons().s.star, 32, UIPanelTop.HEIGHT) {
            @Override
            protected void clickA() {
                gameUiApi.showPanel(questLog, true);
            }
        }.hoverInfoSet("Quests");
        gameUiApi.findUIElementInSettlementView(UIPanelTop.class)
            .flatMap(uiPanelTop -> ReflectionUtil.getDeclaredField("right", uiPanelTop))
            .ifPresent(o -> {
                log.debug("Injecting Quest button into UIPanelTop#right in settlement view");
                GuiSection right = (GuiSection) o;
                right.addRelBody(0, DIR.W, questButton);
            });
    }

    private void initDiscordButton() {
        CLICKABLE Dis = new SpiritButton(SPRITES.icons().m.plus, 32, UIPanelTop.HEIGHT) {
            @Override
            protected void clickA() {
                Desktop desktop = Desktop.getDesktop();
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
            }
        }.hoverInfoSet("Discord Songs of Spirit");
        gameUiApi.findUIElementInSettlementView(UIPanelTop.class)
            .flatMap(uiPanelTop -> ReflectionUtil.getDeclaredField("right", uiPanelTop))
            .ifPresent(o -> {
                 log.debug("Injecting Discord button into UIPanelTop#right in settlement view");
                 GuiSection right = (GuiSection) o;
                 right.addRelBody(0, DIR.W, Dis);
            });
    }

    private void initInfoButton() {
        CLICKABLE settlementButton = new SpiritButton(SPRITES.icons().s.question, 32, UIPanelTop.HEIGHT) {
            @Override
            protected void clickA() {
                infoModal.activate();
            }
        }.hoverInfoSet(MOD_INFO.name);
        gameUiApi.findUIElementInSettlementView(UIPanelTop.class)
            .flatMap(uiPanelTop -> ReflectionUtil.getDeclaredField("right", uiPanelTop))
            .ifPresent(o -> {
                log.debug("Injecting into UIPanelTop#right in settlement view");
                GuiSection right = (GuiSection) o;
                right.addRelBody(8, DIR.W, settlementButton);
            });

        CLICKABLE worldButton = new SpiritButton(SPRITES.icons().s.menu) {
            @Override
            protected void clickA() {
                infoModal.activate();
            }
        }.hoverInfoSet(MOD_INFO.name);
        gameUiApi.findUIElementInWorldGeneratorView(WorldIIMinimap.class)
            .flatMap(worldIIMinimap -> ReflectionUtil.getDeclaredField("buttons", worldIIMinimap))
            .ifPresent(o -> {
                GuiSection buttons = (GuiSection) o;
                log.debug("Injecting Info button into WorldIIMinimap#buttons in world generator view");

                buttons.add(worldButton, buttons.body().x1() + 130, buttons.body().y1() + 4);
            });
    }

    private static class SpiritButton extends GButt.ButtPanel{

        public SpiritButton(SPRITE label, int width, int height) {
            super(label);
            body.setHeight(height);
            body.setWidth(width);
        }

        public SpiritButton(SPRITE label) {
            super(label);
        }
    }
}
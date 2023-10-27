package vizardalpha.songsofspirit;

import init.paths.ModInfo;
import init.paths.PATHS;
import snake2d.util.file.Json;
import util.info.INFO;
import view.interrupter.IDebugPanel;
import view.ui.message.MessageText;
import vizardalpha.songsofspirit.game.MessageHighlight;
import vizardalpha.songsofspirit.game.SCRIPT;
import vizardalpha.songsofspirit.game.api.GameModApi;
import vizardalpha.songsofspirit.game.api.GameUiApi;
import vizardalpha.songsofspirit.log.Logger;
import vizardalpha.songsofspirit.log.Loggers;
import vizardalpha.songsofspirit.ui.UIGameConfig;
import vizardalpha.songsofspirit.ui.info.InfoModal;
import vizardalpha.songsofspirit.ui.info.store.ChangelogsStore;
import vizardalpha.songsofspirit.ui.info.store.CreditsStore;

import java.util.logging.Level;

public final class SongsofSpirit implements SCRIPT<Instance.State> {
    private static final Logger log = Loggers.getLogger(SongsofSpirit.class);

    private UIGameConfig uiGameConfig;

    private Instance instance;

    private final Json welcomeJson = new Json(PATHS.SCRIPT().text.get("SONGS_OF_SPIRIT_START"));
    private final Json updateJson = new Json(PATHS.SCRIPT().text.get("SONGS_OF_SPIRIT_UPDATE"));

    public final static INFO MOD_INFO = new INFO((new Json((PATHS.SCRIPT()).text.get("SONGS_OF_SPIRIT"))).json("SONGS_OF_SPIRIT_INFO"));

    @Override
    public CharSequence desc() {
        return MOD_INFO.desc;
    }

    @Override
    public CharSequence name() {
        return MOD_INFO.name;
    }

    @Override
    public void initBeforeGameCreated() {
        log.debug("PHASE: initBeforeGameCreated");
        IDebugPanel.add("Songs of Spirit: Welcome Message", this::showWelcomeMessage);
    }

    @Override
    public SCRIPT_INSTANCE createInstance() {
        log.debug("PHASE: createInstance");
        Loggers.setLevels(Level.FINEST);
        Instance instance = new Instance(this);
        this.instance = instance;

        return instance;
    }

    @Override
    public void initGameRunning() {
    }

    @Override
    public void initGamePresent() {
    }

    @Override
    public void initSettlementViewPresent() {
        ChangelogsStore changelogsStore = ChangelogsStore.load()
            .orElse(ChangelogsStore.builder().line("No changelogs :(").build());
        CreditsStore creditsStore = CreditsStore.load()
            .orElse(CreditsStore.builder().line("No credits :(").build());

        GameModApi gameModApi = GameModApi.getInstance();
        ModInfo modInfo = gameModApi.getCurrentMod().orElse(null);

        if (modInfo != null && instance != null) {
            instance.getState().setModVersion(SemVersion.from(modInfo.version));
        }

        this.uiGameConfig = new UIGameConfig(
            GameUiApi.getInstance(),
            new InfoModal(changelogsStore, creditsStore, modInfo)
        );
        uiGameConfig.init();

        if (instance != null) {
            Instance.State state = instance.getState();
            if (state.isNewGame()) {
                showWelcomeMessage();
                state.setNewGame(false);
            } else {
                showModUpdateMessage(state);
            }
        }
    }

    private void showWelcomeMessage(){
        new MessageText(welcomeJson.json("SONGS_OF_SPIRIT_NEW_GAME")).send();
    }

    private void showModUpdateMessage(Instance.State state) {
        SemVersion modVersion = state.getModVersion();
        SemVersion savedModVersion = state.getSavedModVersion();
        boolean newGame = state.isNewGame();
        log.trace("Saved Version: %s; Mod Version: %s; New Game: %s", savedModVersion, modVersion, newGame);
        // did the mod had an update?
        if (modVersion.isNewer(savedModVersion)) {
            Json songsOfSpiritUpdate = updateJson.json("SONGS_OF_SPIRIT_UPDATE");

            MessageHighlight messageHighlight = new MessageHighlight(
                songsOfSpiritUpdate.text("NAME"),
                String.format(songsOfSpiritUpdate.text("DESC"), state.getModVersion()),
                uiGameConfig.getSettlementButton());
            messageHighlight.send();
        }
    }

    @Override
    public void initGameSaveLoaded(Instance.State state) {

    }


}

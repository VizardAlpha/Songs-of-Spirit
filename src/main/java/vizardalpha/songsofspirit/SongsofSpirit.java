package vizardalpha.songsofspirit;

import init.paths.ModInfo;
import init.paths.PATHS;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import snake2d.util.file.Json;
import util.info.INFO;
import vizardalpha.songsofspirit.game.MessageHighlight;
import vizardalpha.songsofspirit.game.SCRIPT;
import vizardalpha.songsofspirit.game.api.GameModApi;
import vizardalpha.songsofspirit.game.api.GameUiApi;
import vizardalpha.songsofspirit.log.Loggers;
import vizardalpha.songsofspirit.ui.UIGameConfig;
import vizardalpha.songsofspirit.ui.info.InfoModal;
import vizardalpha.songsofspirit.ui.info.store.ChangelogsStore;
import vizardalpha.songsofspirit.ui.info.store.CreditsStore;

import java.util.logging.Level;

public final class SongsofSpirit implements SCRIPT<SongsofSpirit.State> {
    private UIGameConfig uiGameConfig;

    public final static INFO MOD_INFO = new INFO((new Json((PATHS.SCRIPT()).text.get("SONGS_OF_SPIRIT"))).json("SONGS_OF_SPIRIT_INFO"));

    @Getter
    private final State state;

    public SongsofSpirit() {
        state = State.builder().build();
    }

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

    }

    @Override
    public SCRIPT_INSTANCE createInstance() {
        Loggers.setLevels(Level.FINEST);
        return new Instance(this);
    }

    @Override
    public void initGameRunning() {

    }

    @Override
    public void initGamePresent() {
        ChangelogsStore changelogsStore = ChangelogsStore.load()
            .orElse(ChangelogsStore.builder().line("No changelogs :(").build());
        CreditsStore creditsStore = CreditsStore.load()
            .orElse(CreditsStore.builder().line("No credits :(").build());

        GameModApi gameModApi = GameModApi.getInstance();
        ModInfo modInfo = gameModApi.getCurrentMod().orElse(null);

        if (modInfo != null) {
            getState().setModVersion(SemVersion.from(modInfo.version));
        }

        this.uiGameConfig = new UIGameConfig(
            GameUiApi.getInstance(),
            new InfoModal(changelogsStore, creditsStore, modInfo)
        );
        uiGameConfig.init();
    }

    @Override
    public void initSettlementViewPresent() {
        showModUpdateMessage();
    }

    private void showModUpdateMessage() {
        SemVersion modVersion = state.getModVersion();
        SemVersion savedModVersion = state.getSavedModVersion();

        // don't show for new game
        if (state.isNewGame()) {
            return;
        }

        // did the mod had an update?
        if (modVersion.isNewer(savedModVersion)) {
            MessageHighlight messageHighlight = new MessageHighlight(
                MOD_INFO.name,
                String.format("%s has a new update %s! Check the changelogs.", MOD_INFO.name, state.getModVersion()),
                uiGameConfig.getSettlementButton());
            messageHighlight.send();
        }
    }

    @Override
    public void initGameSaveLoaded(State state) {

    }

    @Setter
    @Getter
    @Builder
    public static class State {
        @Builder.Default
        private boolean newGame = true;

        @Builder.Default
        private SemVersion savedModVersion = SemVersion.from("0.0.0");
        @Builder.Default
        private SemVersion modVersion = SemVersion.from("0.0.0");
    }
}

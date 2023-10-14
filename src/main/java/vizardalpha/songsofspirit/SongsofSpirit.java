package vizardalpha.songsofspirit;

import init.paths.ModInfo;
import init.paths.PATHS;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import snake2d.util.file.Json;
import util.info.INFO;
import vizardalpha.songsofspirit.game.SCRIPT;
import vizardalpha.songsofspirit.game.api.GameModApi;
import vizardalpha.songsofspirit.game.api.GameUiApi;
import vizardalpha.songsofspirit.log.Loggers;
import vizardalpha.songsofspirit.ui.UIGameConfig;
import vizardalpha.songsofspirit.ui.info.InfoModal;
import vizardalpha.songsofspirit.ui.info.store.ChangelogsStore;
import vizardalpha.songsofspirit.ui.info.store.CreditsStore;

import java.util.logging.Level;

public final class SongsofSpirit implements SCRIPT<Void> {
    private UIGameConfig uiGameConfig;

    public final static INFO MOD_INFO = new INFO((new Json((PATHS.SCRIPT()).text.get("SONGS_OF_SPIRIT"))).json("SONGS_OF_SPIRIT_INFO"));

    @Getter
    private State state;

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
            getState().setModVersion(modInfo.version);
        }

        this.uiGameConfig = new UIGameConfig(
            GameUiApi.getInstance(),
            new InfoModal(changelogsStore, creditsStore, modInfo)
        );
        uiGameConfig.init();
    }

    @Override
    public void initGameSaveLoaded(Void config) {

    }

    @Setter
    @Getter
    @Builder
    public static class State {
        @Builder.Default
        private boolean newGame = true;

        @Builder.Default
        private String savedModVersion = "0.0.0";
        @Builder.Default
        private String modVersion = "0.0.0";
    }
}

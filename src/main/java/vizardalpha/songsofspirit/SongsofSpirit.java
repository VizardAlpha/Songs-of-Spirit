package vizardalpha.songsofspirit;

import init.paths.ModInfo;
import init.paths.PATHS;
import settlement.room.main.RoomBlueprint;
import settlement.room.main.RoomCreator;
import settlement.room.main.util.RoomInitData;
import snake2d.util.file.Json;
import util.info.INFO;
import vizardalpha.songsofspirit.game.SCRIPT;
import vizardalpha.songsofspirit.game.api.GameModApi;
import vizardalpha.songsofspirit.game.api.GameUiApi;
import vizardalpha.songsofspirit.log.Loggers;
import vizardalpha.songsofspirit.room.rice.ROOM_RICE;
import vizardalpha.songsofspirit.room.wine.ROOM_WINE;
import vizardalpha.songsofspirit.ui.UIGameConfig;
import vizardalpha.songsofspirit.ui.info.InfoModal;
import vizardalpha.songsofspirit.ui.info.store.ChangelogsStore;
import vizardalpha.songsofspirit.ui.info.store.CreditsStore;

import java.io.IOException;
import java.util.logging.Level;

public final class SongsofSpirit implements SCRIPT<Void> {
    private UIGameConfig uiGameConfig;

    public final static INFO MOD_INFO = new INFO((new Json((PATHS.SCRIPT()).text.get("SONGS_OF_SPIRIT"))).json("SONGS_OF_SPIRIT_INFO"));

    public SongsofSpirit() {

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
       new RoomCreator() {
            @Override
            public RoomBlueprint createBlueprint(RoomInitData init) throws IOException {
                return new ROOM_WINE(init, null);
            }
        };
        new RoomCreator() {
            @Override
            public RoomBlueprint createBlueprint(RoomInitData init) throws IOException {
                return new ROOM_RICE(init, null);
            }
        };
    }
    
    @Override
	public SCRIPT_INSTANCE initAfterGameCreated() {


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

        this.uiGameConfig = new UIGameConfig(
            GameUiApi.getInstance(),
            new InfoModal(changelogsStore, creditsStore, modInfo)
        );
        uiGameConfig.init();
    }

    @Override
    public void initGameSaveLoaded(Void config) {

    }
}

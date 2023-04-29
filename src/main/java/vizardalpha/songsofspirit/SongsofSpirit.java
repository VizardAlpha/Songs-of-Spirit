package vizardalpha.songsofspirit;

import java.io.IOException;
import java.util.logging.Level;

import init.paths.PATHS;

import settlement.room.main.RoomBlueprint;
import settlement.room.main.RoomCreator;
import settlement.room.main.util.RoomInitData;
import snake2d.util.file.Json;
import util.info.INFO;
import vizardalpha.songsofspirit.game.SCRIPT;
import vizardalpha.songsofspirit.game.api.GameUiApi;
import vizardalpha.songsofspirit.log.Loggers;
import vizardalpha.songsofspirit.room.rice.ROOM_RICE;
import vizardalpha.songsofspirit.room.wine.ROOM_WINE;
import vizardalpha.songsofspirit.ui.UIGameConfig;

public final class SongsofSpirit implements SCRIPT<Void> {

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


        Loggers.setLevels(Level.FINER);
        return new Instance(this);
	}

    @Override
    public void initGameRunning() {

    }

    @Override
    public void initGamePresent() {
        UIGameConfig uiGameConfig = new UIGameConfig(GameUiApi.getInstance());
        uiGameConfig.init();
    }

    @Override
    public void initGameSaveLoaded(Void config) {

    }
}

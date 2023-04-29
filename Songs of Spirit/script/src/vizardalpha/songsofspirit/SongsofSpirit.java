package vizardalpha.songsofspirit;

import java.io.IOException;
import init.paths.PATHS;
import script.SCRIPT;

import settlement.room.main.RoomBlueprint;
import settlement.room.main.RoomCreator;
import settlement.room.main.util.RoomInitData;
import snake2d.util.file.Json;
import util.info.INFO;
import vizardalpha.songsofspirit.room.rice.ROOM_RICE;
import vizardalpha.songsofspirit.room.wine.ROOM_WINE;

public final class SongsofSpirit implements SCRIPT {

    private final INFO info = new INFO((new Json((PATHS.SCRIPT()).text.get("SONGS_OF_SPIRIT"))).json("SONGS_OF_SPIRIT_INFO"));

    @Override
    public CharSequence desc() {
        return info.desc;
    }

    @Override
    public CharSequence name() {
        return info.name;
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
		return new Instance();
	}

}

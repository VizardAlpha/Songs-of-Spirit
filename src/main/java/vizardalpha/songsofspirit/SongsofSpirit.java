package vizardalpha.songsofspirit;

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
import vizardalpha.songsofspirit.ui.info.InfoModal;
import vizardalpha.songsofspirit.ui.info.model.ChangelogsStore;
import vizardalpha.songsofspirit.ui.info.model.CreditsStore;

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


        Loggers.setLevels(Level.FINER);
        return new Instance(this);
	}

    @Override
    public void initGameRunning() {

    }

    @Override
    public void initGamePresent() {

        ChangelogsStore changelogsStore = ChangelogsStore.builder()
            .line("#2023-30-04 0.4.0 Release foo")
            .line("* added stuff")
            .line("* fixed other stuff")
            .line("")
            .line("##2023-11-01 0.3.0 Release muh")
            .line("- added some stuff")
            .line("- fixed some other stuff")
            .line("")
            .line("###2023-09-23 0.2.0 Release blubb")
            .line("This is a description of the update. It is very good. So much fun. You won't believe it. China has it too.")
            .line("* added some stuff")
            .line("* added some other stuff")
            .line("---")
            .line("###2023-07-12 0.1.0 Release bla")
            .line("* added some v" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "ry long stuff ")
            .line("* fixed some other stuff")

            .build();

        CreditsStore creditsStore = CreditsStore.builder()
            .line("#Creators")
            .line("VizardAlpha")
            .line("4rg0n")
            .line("")
            .line("---")
            .line("")
            .line("##Bug Reporters")
            .line("IFindBugs9000")
            .line("Darklord69")
            .line("")
            .line("---")
            .line("")
            .line("##Translators")
            .line("###Chinese")
            .line("ITranslateChineseStuff9000")
            .line("Lightlord42CH")
            .line("###French")
            .line("ITranslateFrenchStuff9000")
            .line("Lightlord42FR")
            .build();

        this.uiGameConfig = new UIGameConfig(
            GameUiApi.getInstance(),
            new InfoModal(changelogsStore, creditsStore)
        );
        uiGameConfig.init();
    }

    @Override
    public void initGameSaveLoaded(Void config) {

    }
}

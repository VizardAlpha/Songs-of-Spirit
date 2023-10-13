package vizardalpha.songsofspirit;

import java.io.IOException;

import init.paths.PATHS;
import script.SCRIPT;
import settlement.stats.STATS;
import snake2d.util.file.FileGetter;
import snake2d.util.file.FilePutter;
import snake2d.util.file.Json;
import view.interrupter.IDebugPanel;
import view.ui.message.MessageText;
import view.main.VIEW;
import vizardalpha.songsofspirit.log.Logger;
import vizardalpha.songsofspirit.log.Loggers;


public class Instance implements SCRIPT.SCRIPT_INSTANCE {

	private final static Logger log = Loggers.getLogger(Instance.class);

	private boolean init = false;

	private boolean initGamePresent = false;

	public boolean newgame = false;

	private final SongsofSpirit script;

	public Instance(SongsofSpirit script) {
		this.script = script;
		
		IDebugPanel.add("Songs of Spirit: Welcome Message", this::Message);
	}

	public void Message(){
		Json json = new Json(PATHS.SCRIPT().text.get("SONGS_OF_SPIRIT_START"));
		new MessageText(json.json("SONGS_OF_SPIRIT_NEW_GAME")).send();
	}

	@Override
	public void save(FilePutter file) {
		file.bool(newgame);
	}

	@Override
	public void load(FileGetter file) throws IOException {
		newgame = file.bool();
	}

	@Override
	public void update(double ds) {
		if (!init) {
			log.debug("initGameRunning");
			script.initGameRunning();
			init = true;
		}

		if (!initGamePresent && !VIEW.inters().load.isActivated()) {
			log.debug("initGamePresent");
			script.initGamePresent();
			initGamePresent = true;
		}

		if (STATS.POP().POP.data().get(null) > 0 && (!newgame)) {
			Message();
			newgame = true;
			log.debug("New game");
		}
	}

	public boolean handleBrokenSavedState() {
		return true;
	}

}
package vizardalpha.songsofspirit;

import java.io.IOException;

import init.paths.PATHS;
import script.SCRIPT;
import settlement.stats.STATS;
import snake2d.util.file.FileGetter;
import snake2d.util.file.FilePutter;
import snake2d.util.file.Json;
import snake2d.util.misc.ACTION;
import view.interrupter.IDebugPanel;
import view.main.MessageText;


public class Instance implements SCRIPT.SCRIPT_INSTANCE {

	public boolean states = false;
	public boolean hasRun = false;
	public int UpdateNew = 1;
	public int storedValue;

	public Instance() {
		IDebugPanel.add("Update Songs of Spirit", new ACTION() {

			@Override
			public void exe() {
				MessageVersion();	
			}
		});
		
		IDebugPanel.add("Welcome Message Songs of Spirit", new ACTION() {

			@Override
			public void exe() {
				Message();
			}
		});
	}

	public void Message(){
		Json json = new Json(PATHS.SCRIPT().text.get("SONGS_OF_SPIRIT_START"));
		new MessageText(json.json("SONGS_OF_SPIRIT_NEW_GAME")).send();
	}

	public void MessageVersion() {
		Json json = new Json(PATHS.SCRIPT().text.get("SONGS_OF_SPIRIT_NEWS"));
		new MessageText(json.json("VNews")).send();
	}

	@Override
	public void update(double ds) {

	}

	@Override
	public void save(FilePutter file) {
		file.bool(states);
		file.bool(hasRun);
		file.i(storedValue);
	}

	@Override
	public void load(FileGetter file) throws IOException {
		states = file.bool();
		storedValue = file.i();
		hasRun = file.bool();
	}

	public void NewGame() {
		if (STATS.POP().POP.data().get(null) > 0 && (!states)) {
			Message();
			states = true;
			}
	}

	public void NewUpdate(int UpdateNew, int storedValue) {
			if (!hasRun && UpdateNew > storedValue) {
				storedValue++;
				MessageVersion();
				hasRun = true;
				}
	}
	public void UpdateFar() {
		if (hasRun && UpdateNew > storedValue) {
			MessageVersion();
			storedValue++;
			hasRun = false;
		}
	}

}
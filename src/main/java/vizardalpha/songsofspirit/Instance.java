package vizardalpha.songsofspirit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import script.SCRIPT;
import snake2d.util.file.FileGetter;
import snake2d.util.file.FilePutter;
import view.main.VIEW;
import vizardalpha.songsofspirit.log.Logger;
import vizardalpha.songsofspirit.log.Loggers;

import java.io.IOException;


public class Instance implements SCRIPT.SCRIPT_INSTANCE {

	private final static Logger log = Loggers.getLogger(Instance.class);

	private boolean initGameRunning = false;

	private boolean initGamePresent = false;
	private boolean initSettlementViewPresent = false;

	private final SongsofSpirit script;

	@Getter
	private final State state = State.builder().build();

	public Instance(SongsofSpirit script) {
		this.script = script;
	}

	@Override
	public void save(FilePutter file) {
		log.debug("PHASE: save");
		file.bool(false); // newGame
		log.trace("Saving mod version: %s", state.getModVersion());
		file.chars(state.getModVersion().toString());
	}

	@Override
	public void load(FileGetter file) throws IOException {
		log.debug("PHASE: initGameSaveLoaded");
		state.reset();
		int position = file.getPosition();
		try {
			boolean newGame = file.bool();
			state.setNewGame(newGame);
		} catch (Exception e) {
			log.debug("Could not load newGame from save", e);
			file.setPosition(position);
		}

		position = file.getPosition();
		try {
			String modVersion = file.chars();
			state.setSavedModVersion(SemVersion.from(modVersion));
		} catch (Exception e) {
			log.debug("Could not load modVersion from save", e);
			file.setPosition(position);
		}

		script.initGameSaveLoaded(state);
	}

	@Override
	public void update(double ds) {
		if (!initGameRunning) {
			log.debug("PHASE: initGameRunning");
			script.initGameRunning();
			initGameRunning = true;
		}

		if (!initGamePresent && !VIEW.inters().load.isActivated()) {
			log.debug("PHASE: initGamePresent");
			script.initGamePresent();
			initGamePresent = true;
		}

		if (!initSettlementViewPresent && VIEW.s().isActive()) {
			log.debug("PHASE: initSettlementViewPresent");
			script.initSettlementViewPresent();
			initSettlementViewPresent = true;
		}
	}

	public boolean handleBrokenSavedState() {
		return true;
	}

	@Setter
	@Getter
	@Builder
	public static class State {

		private final static SemVersion NO_VERSION = SemVersion.from("0.0.0");

		@Builder.Default
		private boolean newGame = true;

		@Builder.Default
		private SemVersion savedModVersion = NO_VERSION;
		@Builder.Default
		private SemVersion modVersion = NO_VERSION;

		public void reset() {
			newGame = true;
			savedModVersion = NO_VERSION;
		}
	}
}

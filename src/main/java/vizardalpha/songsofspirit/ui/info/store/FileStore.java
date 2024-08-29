package vizardalpha.songsofspirit.ui.info.store;

import vizardalpha.songsofspirit.log.Logger;
import vizardalpha.songsofspirit.log.Loggers;
import vizardalpha.songsofspirit.util.FileUtil;
import vizardalpha.songsofspirit.game.api.GameModApi;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public abstract class FileStore {
    protected final static GameModApi gameModApi = GameModApi.getInstance();

    private final static Logger log = Loggers.getLogger(FileStore.class);

    protected static Optional<List<String>> load(String name) {
        Path modPath = gameModApi.getCurrentModPath();

        if (modPath == null) {
            log.warn("Could not load file %s, because current mod information isn't present.", name);
            return Optional.empty();
        }

        return FileUtil.load(modPath, name);
    }
}

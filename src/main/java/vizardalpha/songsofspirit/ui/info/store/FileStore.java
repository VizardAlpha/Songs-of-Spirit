package vizardalpha.songsofspirit.ui.info.store;

import vizardalpha.songsofspirit.util.FileUtil;
import vizardalpha.songsofspirit.game.api.GameModApi;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public abstract class FileStore {
    protected final static GameModApi gameModApi = GameModApi.getInstance();

    protected static Optional<List<String>> load(String name) {
        Path modPath = gameModApi.getCurrentModPath();
        return FileUtil.load(modPath, name);
    }
}

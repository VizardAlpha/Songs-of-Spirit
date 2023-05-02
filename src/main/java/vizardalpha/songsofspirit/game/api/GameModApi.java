package vizardalpha.songsofspirit.game.api;

import init.paths.ModInfo;
import init.paths.PATHS;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vizardalpha.songsofspirit.log.Logger;
import vizardalpha.songsofspirit.log.Loggers;
import vizardalpha.songsofspirit.util.Mapper;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GameModApi {
    private final static Logger log = Loggers.getLogger(GameModApi.class);

    @Getter(lazy = true)
    private final static GameModApi instance = new GameModApi();

    public List<ModInfo> getCurrentMods() {
        return Mapper.toJavaList(PATHS.currentMods());
    }

    public Path getCurrentModPath() {
        Path path = PATHS.SCRIPT().jar.get().toAbsolutePath();
        return path.getParent().getParent();
    }

    public Optional<ModInfo> getCurrentMod() {
        return getCurrentMods().stream().filter(modInfo -> {
            log.trace("Checking mod %s in %s", modInfo.name, modInfo.absolutePath);

            Path currentModPath = getCurrentModPath();

            if (currentModPath.startsWith(modInfo.absolutePath)) {
                log.trace("Found match!");
                return true;
            }

            return false;
        }).findFirst();
    }
}

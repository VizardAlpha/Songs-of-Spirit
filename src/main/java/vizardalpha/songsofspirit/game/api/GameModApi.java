package vizardalpha.songsofspirit.game.api;

import init.paths.ModInfo;
import init.paths.PATHS;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vizardalpha.songsofspirit.SongsofSpirit;
import vizardalpha.songsofspirit.log.Logger;
import vizardalpha.songsofspirit.log.Loggers;
import vizardalpha.songsofspirit.util.Mapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        return getCurrentMod()
                .map(modInfo -> Paths.get(modInfo.absolutePath + File.separator + "V"  + modInfo.majorVersion))
                .orElse(null);
    }

    public Optional<ModInfo> getCurrentMod() {
        return getCurrentMods().stream().filter(modInfo -> {
            log.trace("Checking mod %s in %s", modInfo.name, modInfo.absolutePath);

            return modInfo.name.contains(SongsofSpirit.MOD_INFO.name);
        }).findFirst();
    }
}

package vizardalpha.songsofspirit.util;

import vizardalpha.songsofspirit.log.Logger;
import vizardalpha.songsofspirit.log.Loggers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {

    private final static Logger log = Loggers.getLogger(FileUtil.class);

    public static Optional<List<String>> load(Path path, String fileName) {
        log.debug("Loading %s/%s", path, fileName);
        Path loadPath = path.resolve(fileName);
        if (!Files.exists(path.resolve(fileName))) {
            // do not load what's not there
            log.debug("File %s/%s not present.", path, fileName);
            return Optional.empty();
        }

        return load(loadPath);
    }

    public static Optional<List<String>> load(Path path) {
        try (Stream<String> stream = Files.lines(path)) {
            return Optional.of(stream.collect(Collectors.toList()));
        } catch (IOException e) {
            log.debug("Could not load file %s: %s", path, e.getMessage());
            return Optional.empty();
        }
    }
}

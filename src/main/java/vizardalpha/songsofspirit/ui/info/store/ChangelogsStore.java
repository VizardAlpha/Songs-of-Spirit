package vizardalpha.songsofspirit.ui.info.store;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;
import java.util.Optional;

@Data
@Builder
public class ChangelogsStore extends FileStore {

    @Singular
    private List<String> lines;

    public static Optional<ChangelogsStore> load() {
        return load("Changelogs.md")
            .map(lines -> ChangelogsStore.builder().lines(lines).build());
    }
}

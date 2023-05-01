package vizardalpha.songsofspirit.ui.info.store;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;
import java.util.Optional;

@Data
@Builder
public class CreditsStore extends FileStore {

    @Singular
    private List<String> lines;

    public static Optional<CreditsStore> load() {
        return load("Credits.md")
            .map(lines -> CreditsStore.builder().lines(lines).build());
    }
}

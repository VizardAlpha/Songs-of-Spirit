package vizardalpha.songsofspirit.ui.info.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class CreditsStore {

    @Singular
    private List<String> lines;
}

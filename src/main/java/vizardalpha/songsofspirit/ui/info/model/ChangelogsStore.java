package vizardalpha.songsofspirit.ui.info.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class ChangelogsStore {

    @Singular
    private List<String> lines;
}

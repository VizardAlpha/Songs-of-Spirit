package vizardalpha.songsofspirit.quest.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestEntry {
    private Integer id;

    @Builder.Default
    private QuestState state = QuestState.PENDING;

    private QuestData data;
}

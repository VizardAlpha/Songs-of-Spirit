package vizardalpha.songsofspirit.quest.data;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class QuestData {
    private String title;
    private String description;

    private QuestType type;

    @Builder.Default
    private QuestDifficulty difficulty = QuestDifficulty.EASY;

    /**
     * 0 = endless
     */
    @Builder.Default
    private int expireDays = 0;

    @Singular
    private List<QuestRequirement> requirements;
    @Singular
    private List<QuestReward> rewards;
}

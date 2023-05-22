package vizardalpha.songsofspirit.quest.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestReward {
    private QuestRewardType type;

    /**
     * {@link init.resources.RESOURCE#key}
     * {@link init.boostable.BOOSTABLE#key}
     */
    private String key;
    private int amount;
}

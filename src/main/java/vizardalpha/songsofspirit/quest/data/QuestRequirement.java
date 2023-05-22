package vizardalpha.songsofspirit.quest.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestRequirement {
    private QuestRequirementType type;

    /**
     * {@link init.resources.RESOURCE#key}
     * {@link init.boostable.BOOSTABLE#key}
     * {@link init.tech.TECH#key}
     */
    private String key;
    private int amount;
}

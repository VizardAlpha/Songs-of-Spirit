package vizardalpha.songsofspirit.quest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vizardalpha.songsofspirit.quest.data.QuestEntry;

import java.util.List;

@RequiredArgsConstructor
public class QuestLog {
    @Getter
    private final List<QuestEntry> quests;


}

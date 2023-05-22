package vizardalpha.songsofspirit.quest;

import lombok.RequiredArgsConstructor;
import vizardalpha.songsofspirit.quest.data.QuestData;
import vizardalpha.songsofspirit.quest.data.QuestEntry;

@RequiredArgsConstructor
public class Questing {

    private final QuestLog questLog;

    private final QuestPreProcessor preProcessor;

    public QuestEntry acceptQuest(QuestData questData) {
        QuestEntry questEntry = QuestEntry.builder()
            .id(nextId())
            .data(questData)
            .build();

        questEntry = preProcessor.process(questEntry);
        questLog.getQuests().add(questEntry);

        return questEntry;
    }

    public QuestEntry declineQuest(int id) {
        return questLog.getQuests().remove(id);
    }

    private int nextId() {
        return questLog.getQuests().size();
    }
}

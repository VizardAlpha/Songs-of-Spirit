package vizardalpha.songsofspirit.ui.quest;

import lombok.Getter;
import snake2d.util.gui.GuiSection;
import view.interrupter.ISidePanel;

@Getter
public class UIQuestLog extends ISidePanel {
    private final QuestInteractions questInteractions;

    private final QuestSelection questSelection;

    private final QuestInfo questInfo;

    public UIQuestLog(QuestInteractions questInteractions, QuestSelection questSelection, QuestInfo questInfo) {
        this.questInteractions = questInteractions;
        this.questSelection = questSelection;
        this.questInfo = questInfo;
        this.section = new GuiSection();

        section.addDownC(0, questInteractions);
        section.addDownC(5, questSelection);
        section.addDownC(5, questInfo);
    }
}

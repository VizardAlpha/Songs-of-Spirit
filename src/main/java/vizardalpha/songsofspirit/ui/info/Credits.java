package vizardalpha.songsofspirit.ui.info;

import snake2d.util.gui.GuiSection;
import vizardalpha.songsofspirit.ui.info.model.CreditsStore;
import vizardalpha.songsofspirit.ui.richtext.RichText;

import java.util.List;

public class Credits extends GuiSection {
    public Credits(CreditsStore creditsStore, int width, int height) {
        List<String> changelogLines = creditsStore.getLines();
        RichText richText = new RichText(changelogLines, width, height);
        addDownC(0, richText);
    }
}

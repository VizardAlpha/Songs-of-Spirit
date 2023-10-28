package vizardalpha.songsofspirit.ui.info;

import snake2d.util.gui.GuiSection;
import vizardalpha.songsofspirit.ui.info.store.ChangelogsStore;
import vizardalpha.songsofspirit.ui.richtext.RichText;
import java.util.List;

public class Changelogs extends GuiSection {


    public Changelogs(ChangelogsStore changelogsStore, int width, int height) {
        List<String> changelogLines = changelogsStore.getLines();
        RichText richText = new RichText(changelogLines, width, height);
        addDownC(0, richText);
    }
}
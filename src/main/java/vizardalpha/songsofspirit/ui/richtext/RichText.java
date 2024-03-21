package vizardalpha.songsofspirit.ui.richtext;

import init.sprite.SPRITES;
import snake2d.util.gui.GuiSection;
import util.gui.misc.GButt;

import java.util.List;

public class RichText extends GuiSection {

    private final RichTextLines richTextLines;

    public RichText(List<String> lines, int width, int height) {
        this(lines, width, height, 2, false);
    }

    public RichText(List<String> lines, int width, int height, int lineSpace, boolean alignCenter) {
        body().setDim(width, height);

        GButt.Panel up = new GButt.Panel(SPRITES.icons().m.arrow_up);
        up.body().moveX2(width);
        add(up);

        GButt.Panel down = new GButt.Panel(SPRITES.icons().m.arrow_down);
        down.body().moveY2(height).moveX2(width);
        add(down);

        richTextLines = new RichTextLines(lines, width - up.body().width() , height, lineSpace, alignCenter);
        add(richTextLines);

        down.clickActionSet(() -> richTextLines.increase(5));
        up.clickActionSet(() -> richTextLines.decrease(5));
    }
}

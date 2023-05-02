package vizardalpha.songsofspirit.ui.richtext;

import init.sprite.UI.UI;
import snake2d.MButt;
import snake2d.SPRITE_RENDERER;
import snake2d.util.color.COLOR;
import snake2d.util.gui.renderable.RENDEROBJ;
import snake2d.util.sprite.text.Font;
import util.colors.GCOLOR;

import java.util.List;

public class RichTextLines extends RENDEROBJ.RenderImp {
    private int top = 0;

    private final int lineSpace;

    private final List<String> lines;

    private final boolean alignCenter;

    public RichTextLines(List<String> lines, int width, int height, int lineSpace, boolean alignCenter) {
        this.lines = lines;
        this.lineSpace = lineSpace;
        this.alignCenter = alignCenter;
        body().setWidth(width).setHeight(height);
    }

    @Override
    public void render(SPRITE_RENDERER spriteRenderer, float v) {
        int i = (int) MButt.clearWheelSpin();
        top -= i;

        if (top >= lines.size() - 1) {
            top = lines.size() - 1;
        } else if (top < 0) {
            top = 0;
        }
        int y1 = body().y1();
        int line = top;

        while(line < lines.size() && y1 < body().y2()) {
            y1 = renderLine(y1, lines.get(line), spriteRenderer);
            line++;
        }
    }

    public void increase(int amount) {
        top += amount;
    }

    public void decrease(int amount) {
        top -= amount;
    }

    private int renderLine(int y1, String line, SPRITE_RENDERER spriteRenderer) {
        Font font = UI.FONT().S;
        int start = 0;
        String toRender = line;
        String indention = null;
        int headerSpace = 0;

        COLOR.WHITE100.bind();

        if (line.isEmpty()) {
            return y1 + font.height() + lineSpace;
        }

        char firstChar = line.charAt(0);
        if (line.equals("---")) {
            y1 += 5;
            GCOLOR.UI().border().render(spriteRenderer, body().x1(), body().x2(), y1, y1 + 1);
            return y1 + 6 + lineSpace;
        } else if (firstChar == '#') {
            GCOLOR.T().H1.bind();
            font = UI.FONT().H1;
            start = 1;
            headerSpace = 10;

            if (line.length() > 1 && line.charAt(1) == '#') {
                GCOLOR.T().H1.bind();
                font = UI.FONT().H1S;
                start = 2;
                headerSpace = 8;
                if (line.length() > 2 && line.charAt(2) == '#') {
                    headerSpace = 5;
                    GCOLOR.T().H2.bind();
                    font = UI.FONT().H2;
                    start = 3;
                }
            }
        } else if (firstChar == '*' || firstChar == '-') {
            indention = "  ";
            toRender = indention + line;
        }

        do {
            int y2 = y1 + font.height() + headerSpace;
            int end = font.getEndIndex(toRender, start, body().width());
            int x1 = body().x1();

            if (y2 < body().y2()) {
                if (start > 0 && indention != null) {
                    x1 += 4 * font.width(' ', 1);
                }

                if (alignCenter) {
                    int textWidth = font.width(toRender, start, end, 1);
                    int space = (body().width() - textWidth) / 2;
                    x1 += space;
                }

                font.render(spriteRenderer, toRender, x1, y1, start, end, 1);
            }

            y1 = y2;
            start = end;

        } while (start != toRender.length());

        COLOR.unbind();
        return y1 + lineSpace;
    }
}

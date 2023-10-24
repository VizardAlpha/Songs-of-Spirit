package vizardalpha.songsofspirit.game;

import snake2d.SPRITE_RENDERER;
import snake2d.util.color.COLOR;
import snake2d.util.color.OPACITY;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.renderable.RENDEROBJ;
import view.main.VIEW;
import view.ui.message.MessageSection;

public class MessageHighlight extends MessageSection {

    private final RENDEROBJ elementToHighlight;
    private final String body;

    public MessageHighlight(CharSequence title, CharSequence body, RENDEROBJ elementToHighlight) {
        super("" + title);
        this.elementToHighlight = elementToHighlight;
        this.body = "" + body;
    }

    @Override
    protected void make(GuiSection section) {
        paragraph(body);

        section.addDown(0, new RENDEROBJ.RenderImp(0) {
            @Override
            public void render(SPRITE_RENDERER r, float ds) {
                highlight(section, r, elementToHighlight);
                if (!VIEW.s().isActive())
                    VIEW.s().activate();
            }
        });
    }

    private static void highlight(GuiSection s, SPRITE_RENDERER r, RENDEROBJ o) {

        COLOR c = COLOR.RED2RED;

        c.render(r, o.body().x1() - 8, o.body().x2() + 8, o.body().y1() - 8, o.body().y1() - 4);
        c.render(r, o.body().x1() - 8, o.body().x2() + 8, o.body().y2() + 8, o.body().y2() + 4);
        c.render(r, o.body().x1() - 8, o.body().x1() - 4, o.body().y1() - 8, o.body().y2() + 8);
        c.render(r, o.body().x2() + 4, o.body().x2() + 8, o.body().y1() - 8, o.body().y2() + 8);

        if (o.body().cX() < s.body().cX()) {
            c.render(r, o.body().x2() + 4, s.body().cX() + 4, o.body().cY() - 4, o.body().cY() + 4);
        } else {
            c.render(r, o.body().x1() - 4, s.body().cX() + 4, o.body().cY() - 4, o.body().cY() + 4);
        }

        int y1 = s.body().y1() - 80;
        int y2 = s.body().y2();

        if (o.body().y2() < y1) {
            c.render(r, s.body().cX() - 4, s.body().cX() + 4, o.body().cY(), y1);
        } else {
            c.render(r, s.body().cX() - 4, s.body().cX() + 4, o.body().cY(), y2);
        }


        OPACITY.unbind();
    }

}

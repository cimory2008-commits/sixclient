package my.sixclient.ui.util;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/**
 * SATU-SATUNYA tempat pemanggilan API gambar Minecraft untuk helper UI.
 * Jika 26.2 mengubah nama (mis. GuiGraphics -> GuiGraphicsExtractor, drawString -> text,
 * ResourceLocation -> Identifier), cukup ubah file ini + signature override di screen/widget.
 * STATUS: belum dikompilasi terhadap 26.2.
 */
public final class Gfx {
    private Gfx() {}

    public static void rect(GuiGraphics g, int x1, int y1, int x2, int y2, int argb) {
        g.fill(x1, y1, x2, y2, argb);
    }

    public static void border(GuiGraphics g, int x, int y, int w, int h, int argb) {
        g.fill(x, y, x + w, y + 1, argb);
        g.fill(x, y + h - 1, x + w, y + h, argb);
        g.fill(x, y, x + 1, y + h, argb);
        g.fill(x + w - 1, y, x + w, y + h, argb);
    }

    public static void text(GuiGraphics g, Font font, String s, int x, int y, int argb) {
        g.drawString(font, s, x, y, argb, true);
    }

    public static void centered(GuiGraphics g, Font font, String s, int cx, int y, int argb) {
        g.drawString(font, s, cx - font.width(s) / 2, y, argb, true);
    }

    /** Menggambar seluruh tekstur (texW x texH) diskalakan ke w x h. */
    public static void tex(GuiGraphics g, Identifier id, int x, int y, int w, int h, int texW, int texH) {
        g.blit(RenderPipelines.GUI_TEXTURED, id, x, y, 0f, 0f, w, h, texW, texH, texW, texH);
    }
}

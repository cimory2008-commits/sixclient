package my.sixclient.ui.widget;

import my.sixclient.ui.util.Gfx;
import my.sixclient.ui.util.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/** Tombol bertema hitam-merah. Isi subtitle untuk tampilan kartu. */
public class SixButton extends Button {
    private final boolean accent;
    private boolean selected;
    private String subtitle;
    private int subtitleColor = Theme.GREY;

    public SixButton(int x, int y, int w, int h, String label, boolean accent, OnPress onPress) {
        super(x, y, w, h, Component.literal(label), onPress, DEFAULT_NARRATION);
        this.accent = accent;
    }

    public SixButton subtitle(String s, int color) { this.subtitle = s; this.subtitleColor = color; return this; }
    public SixButton selected(boolean sel) { this.selected = sel; return this; }

    // API-SENSITIF: nama/signature method ini dapat berubah di 26.2 (mis. extractWidgetRenderState).
    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        var font = Minecraft.getInstance().font;
        int x = getX(), y = getY(), w = getWidth(), h = getHeight();
        boolean hot = active && isHoveredOrFocused();
        int bg = (accent || selected) ? Theme.RED_DARK : (hot ? Theme.CARD_HOVER : Theme.CARD);
        Gfx.rect(g, x, y, x + w, y + h, bg);
        Gfx.border(g, x, y, w, h, (hot || accent || selected) ? Theme.RED : 0xFF2A2A30);
        int col = active ? Theme.WHITE : Theme.GREY;
        if (subtitle == null) {
            Gfx.centered(g, font, getMessage().getString(), x + w / 2, y + (h - 8) / 2, col);
        } else {
            Gfx.text(g, font, getMessage().getString(), x + 8, y + 8, col);
            Gfx.centered(g, font, subtitle, x + w / 2, y + h - 18, subtitleColor);
        }
    }
}

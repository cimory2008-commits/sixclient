package my.sixclient.ui.screen;

import my.sixclient.ui.config.SixConfig;
import my.sixclient.ui.util.Gfx;
import my.sixclient.ui.util.Theme;
import my.sixclient.ui.widget.SixButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SixModMenuScreen extends Screen {
    private final Screen parent;
    private ModCard.Tab tab;
    private String search = "";
    private int scrollRow = 0;
    private EditBox searchBox;
    private final List<SixButton> cardButtons = new ArrayList<>();
    private int px, py, pw, ph, gx, gy, gw, gh;

    public SixModMenuScreen(Screen parent) {
        super(Component.literal("SIX CLIENT - Mod Menu"));
        this.parent = parent;
        try { this.tab = ModCard.Tab.valueOf(SixConfig.get().lastTab); }
        catch (Exception e) { this.tab = ModCard.Tab.ALL; }
    }

    @Override
    protected void init() {
        pw = Math.min(width - 24, 760);
        ph = Math.min(height - 24, 440);
        px = (width - pw) / 2;
        py = (height - ph) / 2;

        int x = px + 10, y = py + 10;
        for (ModCard.Tab t : ModCard.Tab.values()) {
            String label = t == ModCard.Tab.ALL ? "All" : cap(t.name());
            int w = Math.max(40, font.width(label) + 16);
            final ModCard.Tab target = t;
            addRenderableWidget(new SixButton(x, y, w, 20, label, false, b -> {
                tab = target; scrollRow = 0;
                SixConfig.get().lastTab = t.name(); SixConfig.save();
                rebuildWidgets();
            }).selected(t == tab));
            x += w + 4;
        }

        int rightX = px + pw - 10;
        addRenderableWidget(new SixButton(rightX - 20, y, 20, 20, "x", false, b -> onClose()));
        addRenderableWidget(new SixButton(rightX - 20 - 4 - 50, y, 50, 20, "Reset", false, b -> {
            SixConfig.reset(); search = ""; scrollRow = 0; tab = ModCard.Tab.ALL; rebuildWidgets();
        }));
        int sbW = Math.max(80, Math.min(150, rightX - 20 - 4 - 50 - 8 - x));
        searchBox = new EditBox(font, rightX - 20 - 4 - 50 - 4 - sbW, y, sbW, 20, Component.literal("Search"));
        searchBox.setHint(Component.literal("Search"));
        searchBox.setValue(search);
        searchBox.setResponder(s -> { search = s; scrollRow = 0; rebuildCards(); });
        addRenderableWidget(searchBox);

        gx = px + 10; gy = py + 40; gw = pw - 20; gh = ph - 50;
        rebuildCards();
    }

    private static String cap(String s) { return s.charAt(0) + s.substring(1).toLowerCase(Locale.ROOT); }

    private List<ModCard> filtered() {
        String q = search.toLowerCase(Locale.ROOT).trim();
        List<ModCard> out = new ArrayList<>();
        for (ModCard c : ModCard.ALL) {
            if (tab != ModCard.Tab.ALL && c.tab() != tab) continue;
            if (!q.isEmpty() && !c.name().toLowerCase(Locale.ROOT).contains(q)) continue;
            out.add(c);
        }
        return out;
    }

    private void rebuildCards() {
        for (SixButton b : cardButtons) removeWidget(b);
        cardButtons.clear();
        List<ModCard> list = filtered();
        int gap = 6, minW = 150;
        int cols = Math.max(1, (gw + gap) / (minW + gap));
        int cardW = (gw - gap * (cols - 1)) / cols;
        int cardH = 56;
        int rowsVisible = Math.max(1, (gh + gap) / (cardH + gap));
        int totalRows = (list.size() + cols - 1) / cols;
        scrollRow = Math.max(0, Math.min(scrollRow, Math.max(0, totalRows - rowsVisible)));

        for (int i = 0; i < list.size(); i++) {
            int row = i / cols - scrollRow, col = i % cols;
            if (row < 0 || row >= rowsVisible) continue;
            ModCard c = list.get(i);
            SixButton b = new SixButton(gx + col * (cardW + gap), gy + row * (cardH + gap), cardW, cardH,
                c.name(), false, btn -> {
                    if (c.implemented()) { c.toggler().run(); SixConfig.save(); rebuildCards(); }
                });
            if (!c.implemented()) { b.subtitle("Coming Soon", Theme.GREY); b.active = false; }
            else if (c.getter().getAsBoolean()) b.subtitle("Enabled", Theme.RED);
            else b.subtitle("Disabled", Theme.GREY);
            cardButtons.add(b);
            addRenderableWidget(b);
        }
    }

    // API-SENSITIF: signature mouseScrolled berubah antar versi.
    @Override
    public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
        if (scrollY != 0) { scrollRow -= (int) Math.signum(scrollY); rebuildCards(); return true; }
        return super.mouseScrolled(mx, my, scrollX, scrollY);
    }

    @Override public void onClose() { Minecraft.getInstance().setScreen(parent); }

    @Override
    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        Gfx.rect(g, 0, 0, width, height, Theme.DIM);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g, mouseX, mouseY, partialTick);
        Gfx.rect(g, px, py, px + pw, py + ph, Theme.PANEL);
        Gfx.border(g, px, py, pw, ph, Theme.RED_DARK);
        Gfx.rect(g, px, py, px + pw, py + 2, Theme.RED);
        super.render(g, mouseX, mouseY, partialTick);
        if (filtered().isEmpty())
            Gfx.centered(g, font, "No results", width / 2, gy + 20, Theme.GREY);
    }
}

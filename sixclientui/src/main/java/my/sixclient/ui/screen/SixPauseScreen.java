package my.sixclient.ui.screen;

import my.sixclient.ui.util.Gfx;
import my.sixclient.ui.util.Theme;
import my.sixclient.ui.widget.SixButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;

public class SixPauseScreen extends Screen {
    /** true saat user memilih "Vanilla Menu" supaya tidak diganti lagi. */
    public static boolean bypass = false;

    public SixPauseScreen() { super(Component.literal("SIX CLIENT")); }

    @Override public boolean isPauseScreen() { return true; }

    @Override
    protected void init() {
        Minecraft mc = Minecraft.getInstance();
        int bw = 200, bh = 22, gap = 4;
        int logoH = logoHeight();
        int y = 16 + logoH + 14;
        int x = (width - bw) / 2;

        addRenderableWidget(new SixButton(x, y, bw, bh, "Back to Game", true, b -> mc.setScreen(null)));
        y += bh + gap;
        addRenderableWidget(new SixButton(x, y, bw, bh, "Settings", false,
            b -> mc.setScreen(new OptionsScreen(this, mc.options))));
        y += bh + gap;
        addRenderableWidget(new SixButton(x, y, bw, bh, "Mods", false,
            b -> mc.setScreen(new SixModMenuScreen(this))));
        y += bh + gap;
        SixButton store = new SixButton(x, y, bw, bh, "Store (Coming Soon)", false, b -> {});
        store.active = false;
        addRenderableWidget(store);
        y += bh + gap;
        addRenderableWidget(new SixButton(x, y, bw, bh, "Vanilla Menu", false, b -> {
            bypass = true; // akses Advancements/Statistics/Open to LAN/dll tetap lewat menu vanilla
            mc.setScreen(new PauseScreen(true));
        }));
        y += bh + gap;
        String leaveLabel = mc.isLocalServer() ? "Save and Quit to Title" : "Disconnect";
        addRenderableWidget(new SixButton(x, y, bw, bh, leaveLabel, false, b -> leave(mc)));
    }

    private int logoHeight() {
        int h = Math.min(100, (int) (height * 0.22f));
        return Math.max(h, 40);
    }

    /** Meniru alur PauseScreen vanilla 1.21.x. API-SENSITIF: verifikasi terhadap 26.2. */
    private void leave(Minecraft mc) {
        boolean local = mc.isLocalServer();
        if (mc.level != null) mc.level.disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE);
        if (local) {
            mc.disconnect(new GenericMessageScreen(Component.translatable("menu.savingLevel")));
            mc.setScreen(new TitleScreen());
        } else {
            mc.disconnect();
            mc.setScreen(new JoinMultiplayerScreen(new TitleScreen()));
        }
    }

    // API-SENSITIF: nama method bisa berubah di 26.2.
    @Override
    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        Gfx.rect(g, 0, 0, width, height, Theme.DIM); // game tetap terlihat, hanya digelapkan
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        int lh = logoHeight();
        int lw = lh * Theme.LOGO_W / Theme.LOGO_H;
        Gfx.tex(g, Theme.LOGO, (width - lw) / 2, 16, lw, lh, Theme.LOGO_W, Theme.LOGO_H);

        Minecraft mc = Minecraft.getInstance();
        String name = mc.getUser().getName();
        int tw = font.width(name) + 16;
        Gfx.rect(g, width - tw - 8, 8, width - 8, 28, Theme.CARD);
        Gfx.border(g, width - tw - 8, 8, tw, 20, Theme.RED_DARK);
        Gfx.text(g, font, name, width - tw, 14, Theme.WHITE);
        Gfx.text(g, font, "SIX CLIENT UI v0.1.0", 6, height - 12, Theme.GREY);
    }
}

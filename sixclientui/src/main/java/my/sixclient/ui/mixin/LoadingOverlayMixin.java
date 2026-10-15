package my.sixclient.ui.mixin;

import my.sixclient.ui.config.SixConfig;
import my.sixclient.ui.util.Gfx;
import my.sixclient.ui.util.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Menggambar splash SIX CLIENT DI ATAS LoadingOverlay vanilla (tail of render).
 * Logika vanilla (reload resource, fade, penutupan overlay) TIDAK diubah/dibatalkan,
 * jadi startup tidak terkunci. Progress bar = indeterminate (jujur, bukan loading palsu).
 * defaultRequire=0: jika method target berubah di 26.2, injeksi dilewati (splash vanilla tampil), tidak crash.
 * API-SENSITIF: signature render(GuiGraphics,int,int,float) bisa berbeda di 26.2.
 */
@Mixin(LoadingOverlay.class)
public abstract class LoadingOverlayMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void sixclientui$drawSplash(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!SixConfig.get().customSplash) return;
        int sw = g.guiWidth(), sh = g.guiHeight();
        var font = Minecraft.getInstance().font;

        Gfx.rect(g, 0, 0, sw, sh, Theme.BLACK);

        int logoW = Math.min(360, (int) (sw * 0.5f));
        int logoH = logoW * Theme.LOGO_H / Theme.LOGO_W;
        if (logoH > sh * 0.55f) { logoH = (int) (sh * 0.55f); logoW = logoH * Theme.LOGO_W / Theme.LOGO_H; }
        int lx = (sw - logoW) / 2, ly = sh / 2 - logoH / 2 - 14;

        if (logoAvailable()) {
            Gfx.tex(g, Theme.LOGO, lx, ly, logoW, logoH, Theme.LOGO_W, Theme.LOGO_H);
        } else { // fallback tanpa gambar
            Gfx.centered(g, font, "SIX CLIENT", sw / 2, sh / 2 - 20, Theme.RED);
        }

        int barW = logoW, barH = 4, bx = lx, by = ly + logoH + 18;
        Gfx.rect(g, bx, by, bx + barW, by + barH, 0xFF1A1A1E);
        float t = (Util.getMillis() % 1400L) / 1400f;          // 0..1 berulang
        int seg = barW / 3;
        int start = (int) (t * (barW + seg)) - seg;
        int x1 = Math.max(bx, bx + start), x2 = Math.min(bx + barW, bx + start + seg);
        if (x2 > x1) Gfx.rect(g, x1, by, x2, by + barH, Theme.RED);
        Gfx.centered(g, font, "Loading resources...", sw / 2, by + 12, Theme.GREY);
    }

    private static Boolean logoOk;
    private static boolean logoAvailable() {
        if (logoOk == null) {
            logoOk = Minecraft.getInstance().getResourceManager().getResource(Theme.LOGO).isPresent();
        }
        return logoOk;
    }
}

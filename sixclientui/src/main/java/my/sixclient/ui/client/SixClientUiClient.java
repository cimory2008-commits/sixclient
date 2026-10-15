package my.sixclient.ui.client;

import my.sixclient.ui.config.SixConfig;
import my.sixclient.ui.screen.SixPauseScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screens.PauseScreen;

public class SixClientUiClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SixConfig.load();

        // Tanpa mixin: ganti PauseScreen vanilla dengan SixPauseScreen saat berada di world/server.
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            var screen = mc.screen;
            if (!(screen instanceof PauseScreen)) {
                SixPauseScreen.bypass = false; // reset setelah vanilla menu ditutup
                return;
            }
            if (SixPauseScreen.bypass || !SixConfig.get().customPauseMenu) return;
            if (mc.level == null || mc.player == null) return;
            mc.setScreen(new SixPauseScreen());
        });
    }
}

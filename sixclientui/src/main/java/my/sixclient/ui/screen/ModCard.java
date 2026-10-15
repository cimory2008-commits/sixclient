package my.sixclient.ui.screen;

import my.sixclient.ui.config.SixConfig;
import java.util.List;
import java.util.function.BooleanSupplier;

/** Definisi satu kartu. getter == null -> "Coming Soon" dan tidak bisa di-toggle. */
public record ModCard(String name, Tab tab, BooleanSupplier getter, Runnable toggler) {

    public enum Tab { ALL, HUD, RENDER, MOVEMENT, PLAYER, UTILITY }

    public boolean implemented() { return getter != null; }

    private static ModCard soon(String n, Tab t) { return new ModCard(n, t, null, null); }

    public static final List<ModCard> ALL = List.of(
        new ModCard("Custom ESC Menu", Tab.UTILITY,
            () -> SixConfig.get().customPauseMenu,
            () -> SixConfig.get().customPauseMenu = !SixConfig.get().customPauseMenu),
        new ModCard("Custom Splash", Tab.UTILITY,
            () -> SixConfig.get().customSplash,
            () -> SixConfig.get().customSplash = !SixConfig.get().customSplash),
        soon("FPS", Tab.HUD), soon("Coordinates", Tab.HUD), soon("Direction", Tab.HUD),
        soon("Clock", Tab.HUD), soon("Memory Usage", Tab.HUD), soon("Biome", Tab.HUD),
        soon("Resource Packs", Tab.UTILITY), soon("Server Address", Tab.HUD),
        soon("Ping", Tab.HUD), soon("Server TPS", Tab.HUD), soon("Day Counter", Tab.HUD),
        soon("Speed", Tab.MOVEMENT)
    );
}

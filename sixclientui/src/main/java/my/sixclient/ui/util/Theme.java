package my.sixclient.ui.util;

import net.minecraft.resources.Identifier;

public final class Theme {
    private Theme() {}
    public static final int BLACK      = 0xFF000000;
    public static final int PANEL      = 0xF00B0B0D;
    public static final int CARD       = 0xFF141417;
    public static final int CARD_HOVER = 0xFF1E1013;
    public static final int RED        = 0xFFE01B24;
    public static final int RED_DARK   = 0xFF7A0E14;
    public static final int WHITE      = 0xFFFFFFFF;
    public static final int GREY       = 0xFF9A9AA2;
    public static final int DIM        = 0xB8000000;

    public static final int LOGO_W = 800, LOGO_H = 525;
    public static final int BG_W = 1600, BG_H = 1003;

    public static final Identifier LOGO = Identifier.fromNamespaceAndPath("sixclientui", "textures/gui/logo.png");
    public static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath("sixclientui", "textures/gui/background.png");
}

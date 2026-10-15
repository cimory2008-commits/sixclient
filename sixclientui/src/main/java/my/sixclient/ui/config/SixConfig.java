package my.sixclient.ui.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Preferensi lokal: config/sixclientui.json */
public final class SixConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static SixConfig instance = new SixConfig();

    public boolean customPauseMenu = true;
    public boolean customSplash = true;
    public String lastTab = "ALL";

    public static SixConfig get() { return instance; }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("sixclientui.json");
    }

    public static void load() {
        try {
            Path p = path();
            if (Files.exists(p)) {
                SixConfig c = GSON.fromJson(Files.readString(p), SixConfig.class);
                if (c != null) instance = c;
            }
        } catch (Exception e) {
            System.err.println("[SIX CLIENT UI] Gagal memuat config, pakai default: " + e);
            instance = new SixConfig();
        }
    }

    public static void save() {
        try {
            Files.writeString(path(), GSON.toJson(instance));
        } catch (IOException e) {
            System.err.println("[SIX CLIENT UI] Gagal menyimpan config: " + e);
        }
    }

    public static void reset() {
        instance = new SixConfig();
        save();
    }
}

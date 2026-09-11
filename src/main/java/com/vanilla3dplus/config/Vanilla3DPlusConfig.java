package com.vanilla3dplus.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.fabricmc.loader.api.FabricLoader;

public final class Vanilla3DPlusConfig {
    private Vanilla3DPlusConfig() {}

    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("vanilla_3d_plus.json");

    public static boolean weatherEffects = true;
    public static boolean waterEffects = true;
    public static boolean footprints = true;
    public static boolean dynamicFog = true;
    public static boolean rainbow = true;
    public static boolean clouds = true;
    public static boolean materialEffects = true;
    public static boolean weaponEffects = true;
    public static boolean relief3D = true;
    public static Quality quality = Quality.HIGH;

    public enum Quality { LOW, MEDIUM, HIGH }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) { save(); return; }
        try {
            String json = Files.readString(CONFIG_PATH);
            weatherEffects = readBoolean(json, "weatherEffects", true);
            waterEffects = readBoolean(json, "waterEffects", true);
            footprints = readBoolean(json, "footprints", true);
            dynamicFog = readBoolean(json, "dynamicFog", true);
            rainbow = readBoolean(json, "rainbow", true);
            clouds = readBoolean(json, "clouds", true);
            materialEffects = readBoolean(json, "materialEffects", true);
            weaponEffects = readBoolean(json, "weaponEffects", true);
            relief3D = readBoolean(json, "relief3D", true);
            try { quality = Quality.valueOf(readString(json, "quality", "HIGH").toUpperCase()); }
            catch (IllegalArgumentException ignored) { quality = Quality.HIGH; }
        } catch (IOException ignored) { setDefaults(); }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            String json = """
                    {
                      "weatherEffects": %s,
                      "waterEffects": %s,
                      "footprints": %s,
                      "dynamicFog": %s,
                      "rainbow": %s,
                      "clouds": %s,
                      "materialEffects": %s,
                      "weaponEffects": %s,
                      "relief3D": %s,
                      "quality": "%s"
                    }
                    """.formatted(weatherEffects, waterEffects, footprints, dynamicFog, rainbow,
                    clouds, materialEffects, weaponEffects, relief3D, quality.name());
            Files.writeString(CONFIG_PATH, json);
        } catch (IOException ignored) {}
    }

    public static void setDefaults() {
        weatherEffects = true; waterEffects = true; footprints = true; dynamicFog = true;
        rainbow = true; clouds = true; materialEffects = true; weaponEffects = true; relief3D = true;
        quality = Quality.HIGH;
    }

    private static boolean readBoolean(String json, String key, boolean fallback) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(key) + "\"\s*:\s*(true|false)").matcher(json);
        return matcher.find() ? Boolean.parseBoolean(matcher.group(1)) : fallback;
    }

    private static String readString(String json, String key, String fallback) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(key) + "\"\s*:\s*\"([^\"]+)\"").matcher(json);
        return matcher.find() ? matcher.group(1) : fallback;
    }
}

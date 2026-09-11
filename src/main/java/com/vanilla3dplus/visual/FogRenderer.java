package com.vanilla3dplus.visual;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;

/** Screen-space atmospheric tint. Kept separate so a future world fog hook can replace it cleanly. */
public final class FogRenderer {
    private FogRenderer() {}

    public static void renderHud(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null || !Vanilla3DPlusConfig.dynamicFog) return;
        Player player = client.player;
        ClientLevel level = client.level;
        if (player.isUnderWater()) return;

        int width = client.getWindow().getGuiScaledWidth();
        int height = client.getWindow().getGuiScaledHeight();
        float atmosphere = 0.0f;

        if (level.isRaining()) atmosphere += level.isThundering() ? 0.16f : 0.08f;
        long time = level.getGameTime() % 24000L;
        if (time >= 13000L && time <= 23000L) atmosphere += 0.055f;

        if (atmosphere <= 0.0f) return;
        int alpha = (int) Mth.clamp(atmosphere * 255.0f, 0.0f, 70.0f);
        int fogColor = level.isThundering()
                ? (alpha << 24) | (35 << 16) | (40 << 8) | 55
                : level.isRaining()
                    ? (alpha << 24) | (55 << 16) | (70 << 8) | 85
                    : (alpha << 24) | (20 << 16) | (25 << 8) | 40;
        graphics.fill(0, 0, width, height, fogColor);
    }
}

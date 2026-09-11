package com.vanilla3dplus.visual;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public final class HudVisualRenderer {
    private HudVisualRenderer() {}

    public static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;

        int width = client.getWindow().getGuiScaledWidth();
        int height = client.getWindow().getGuiScaledHeight();
        Player player = client.player;
        ClientLevel level = client.level;

        if (Vanilla3DPlusConfig.waterEffects && player.isUnderWater()) {
            float time = (level.getGameTime() + deltaTracker.getGameTimeDeltaPartialTick(true)) * 0.045f;
            float wave = (Mth.sin(time) + 1.0f) * 0.5f;
            int alpha = 52 + (int) (wave * 24.0f);
            graphics.fill(0, 0, width, height, (alpha << 24) | (25 << 16) | (95 << 8) | 175);
            graphics.fillGradient(0, height / 2, width, height, 0x00000000, (42 << 24) | (5 << 16) | (25 << 8) | 65);
            graphics.fillGradient(0, 0, width, height / 3, 0x52000A18, 0x00000000);
        }

        if (Vanilla3DPlusConfig.dynamicFog && !player.isUnderWater()) {
            renderAtmosphericHaze(graphics, width, height, level, player);
        }
    }

    private static void renderAtmosphericHaze(GuiGraphicsExtractor graphics, int width, int height, ClientLevel level, Player player) {
        float haze = 0.0f;
        if (level.isThundering()) haze += 0.34f;
        else if (level.isRaining()) haze += 0.22f;

        long time = level.getGameTime() % 24000L;
        if (time >= 12500L && time <= 23000L) haze += 0.16f;
        else if (time < 3500L) haze += 0.07f;
        if (player.getDeltaMovement().horizontalDistanceSqr() < 0.0025) haze += 0.025f;

        float rainPulse = (Mth.sin(level.getGameTime() * 0.018f) + 1.0f) * 0.5f;
        int alpha = (int) Mth.clamp(38.0f + haze * 245.0f + rainPulse * 10.0f, 0.0f, 132.0f);
        int fogColor;
        if (level.isThundering()) fogColor = (alpha << 24) | (24 << 16) | (30 << 8) | 46;
        else if (level.isRaining()) fogColor = (alpha << 24) | (42 << 16) | (58 << 8) | 75;
        else fogColor = (alpha << 24) | (28 << 16) | (33 << 8) | 46;

        graphics.fill(0, 0, width, height, fogColor);
        int topAlpha = Math.min(108, alpha + 18);
        graphics.fillGradient(0, 0, width, height / 2, (topAlpha << 24) | (22 << 16) | (27 << 8) | 39, 0x00000000);
        int bottomAlpha = Math.min(86, alpha / 2 + 16);
        graphics.fillGradient(0, height / 2, width, height, 0x00000000, (bottomAlpha << 24) | (15 << 16) | (19 << 8) | 27);
    }
}

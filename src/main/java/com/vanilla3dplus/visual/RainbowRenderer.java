package com.vanilla3dplus.visual;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public final class RainbowRenderer {
    private RainbowRenderer() {}

    public static void update(Minecraft client, VisualState state) {
        if (!Vanilla3DPlusConfig.rainbow || client.player == null || client.level == null) return;

        ClientLevel level = client.level;
        Player player = client.player;

        if (!state.wasRaining || level.isRaining() || level.isThundering()) return;

        long time = level.getGameTime() % 24000L;
        if (time <= 500L || time >= 12500L) return;

        if (state.tick % 2 != 0) return;

        var random = level.getRandom();
        int count = switch (Vanilla3DPlusConfig.quality) {
            case LOW -> 4;
            case MEDIUM -> 7;
            case HIGH -> 11;
        };

        double rotation = state.tick * 0.0035;
        double baseRadius = 18.0 + random.nextDouble() * 4.0;
        double baseY = player.getY() + 5.0;

        for (int i = 0; i < count; i++) {
            double t = (i + random.nextDouble()) / count;
            double arc = Math.PI * (0.16 + t * 0.68);
            double band = (i % 7) * 0.72;
            double radius = baseRadius + band;

            double angle = rotation + arc;
            double x = player.getX() + Math.cos(angle) * radius;
            double z = player.getZ() + Math.sin(angle) * radius;
            double y = baseY + Math.sin(arc) * 7.0 + (random.nextDouble() - 0.5) * 0.35;

            ParticleOptions particle = colorParticle(i % 7);
            level.addParticle(particle, x, y, z, 0.0, 0.006, 0.0);
        }
    }

    private static ParticleOptions colorParticle(int band) {
        return switch (band) {
            case 0 -> ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1.0f, 0.12f, 0.12f);
            case 1 -> ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1.0f, 0.42f, 0.08f);
            case 2 -> ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1.0f, 0.90f, 0.10f);
            case 3 -> ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.20f, 1.0f, 0.28f);
            case 4 -> ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.10f, 0.72f, 1.0f);
            case 5 -> ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.20f, 0.28f, 1.0f);
            default -> ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.72f, 0.16f, 1.0f);
        };
    }
}

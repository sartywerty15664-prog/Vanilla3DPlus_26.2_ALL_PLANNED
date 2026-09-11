package com.vanilla3dplus.visual;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;

/** Layered particle volumes used as a shader-safe approximation of 3D clouds. */
public final class CloudRenderer {
    private CloudRenderer() {}

    public static void update(Minecraft client, VisualState state) {
        if (!Vanilla3DPlusConfig.clouds || client.player == null || client.level == null) return;
        if (state.tick % 3 != 0) return;

        ClientLevel level = client.level;
        Player player = client.player;
        if (level.isRaining() || level.isThundering()) return;

        var random = level.getRandom();
        int chance = Vanilla3DPlusConfig.quality == Vanilla3DPlusConfig.Quality.HIGH ? 78 : 58;
        if (random.nextInt(100) > chance) return;

        int count = switch (Vanilla3DPlusConfig.quality) {
            case LOW -> 8;
            case MEDIUM -> 17;
            case HIGH -> 30;
        };

        double centerAngle = random.nextDouble() * Math.PI * 2.0;
        double centerRadius = 20.0 + random.nextDouble() * 22.0;
        double centerX = player.getX() + Math.cos(centerAngle) * centerRadius;
        double centerZ = player.getZ() + Math.sin(centerAngle) * centerRadius;
        double centerY = player.getY() + 12.0 + random.nextDouble() * 8.0;

        for (int i = 0; i < count; i++) {
            double layer = (i % 5) - 2.0;
            double angle = random.nextDouble() * Math.PI * 2.0;
            double radius = Math.sqrt(random.nextDouble());
            double horizontalX = 7.0 + Math.abs(layer) * 1.7;
            double horizontalZ = 6.0 + (2.0 - Math.abs(layer)) * 1.8;
            double x = centerX + Math.cos(angle) * radius * horizontalX;
            double y = centerY + layer * 0.9 + (random.nextDouble() - 0.5) * 1.6;
            double z = centerZ + Math.sin(angle) * radius * horizontalZ;
            double driftX = 0.003 + random.nextDouble() * 0.009;
            double driftZ = (random.nextDouble() - 0.5) * 0.003;
            level.addParticle(ParticleTypes.CLOUD, x, y, z, driftX, 0.0, driftZ);
        }
    }
}

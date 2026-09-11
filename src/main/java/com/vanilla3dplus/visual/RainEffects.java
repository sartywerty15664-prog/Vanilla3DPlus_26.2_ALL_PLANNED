package com.vanilla3dplus.visual;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;

public final class RainEffects {
    private RainEffects() {
    }

    public static void updateRain(Minecraft client, VisualState state) {
        if (!Vanilla3DPlusConfig.weatherEffects || client.player == null || client.level == null) {
            return;
        }

        ClientLevel level = client.level;
        Player player = client.player;

        if (!level.isRaining()) {
            return;
        }

        var random = level.getRandom();

        int count;
        switch (Vanilla3DPlusConfig.quality) {
            case LOW -> count = 2;
            case MEDIUM -> count = 4;
            case HIGH -> count = 7;
            default -> count = 4;
        }

        if (level.isThundering()) {
            count += 3;
        }

        for (int i = 0; i < count; i++) {
            int radius = 8;
            int x = player.blockPosition().getX()
                    + random.nextInt(radius * 2 + 1) - radius;
            int z = player.blockPosition().getZ()
                    + random.nextInt(radius * 2 + 1) - radius;

            BlockPos surface = level.getHeight(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                    x,
                    z
            );

            if (surface.getY() < player.getY() - 6
                    || surface.getY() > player.getY() + 18) {
                continue;
            }

            double px = x + random.nextDouble();
            double py = surface.getY() + 6.0 + random.nextDouble() * 7.0;
            double pz = z + random.nextDouble();

            level.addParticle(
                    ParticleTypes.RAIN,
                    px,
                    py,
                    pz,
                    (random.nextDouble() - 0.5) * 0.01,
                    -0.45 - random.nextDouble() * 0.18,
                    (random.nextDouble() - 0.5) * 0.01
            );
        }

        if (state.tick % 2 == 0) {
            int splashCount = level.isThundering() ? 4 : 2;

            for (int i = 0; i < splashCount; i++) {
                int radius = 7;
                BlockPos pos = player.blockPosition().offset(
                        random.nextInt(radius * 2 + 1) - radius,
                        0,
                        random.nextInt(radius * 2 + 1) - radius
                );

                if (!level.getBlockState(pos).isSolidRender()) {
                    continue;
                }

                double x = pos.getX() + random.nextDouble();
                double y = pos.getY() + 1.01;
                double z = pos.getZ() + random.nextDouble();

                level.addParticle(
                        ParticleTypes.SPLASH,
                        x, y, z,
                        (random.nextDouble() - 0.5) * 0.08,
                        0.02 + random.nextDouble() * 0.035,
                        (random.nextDouble() - 0.5) * 0.08
                );
            }
        }
    }
}

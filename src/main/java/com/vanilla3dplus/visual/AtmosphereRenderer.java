package com.vanilla3dplus.visual;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Blocks;

public final class AtmosphereRenderer {
    private AtmosphereRenderer() {}

        public static void updateAtmosphere(
                Minecraft client, VisualState state
        ) {

            if (!Vanilla3DPlusConfig.dynamicFog
                    && !Vanilla3DPlusConfig.weatherEffects) {
                return;
            }

            ClientLevel level = client.level;
            Player player = client.player;

            var random = level.getRandom();

            if (state.tick % 12 != 0) {
                return;
            }

            float chance =
                    level.isThundering()
                            ? 0.55f
                            : level.isRaining()
                            ? 0.28f
                            : 0.10f;

            if (random.nextFloat() > chance) {
                return;
            }

            int y =
                    player.blockPosition()
                            .getY();

            if (level.isThundering()) {

                level.addParticle(
                        ParticleTypes.ASH,
    player.getX()
                                + (random.nextDouble() - 0.5)
                                * 8,

                        y + 2
                                + random.nextDouble() * 4,

                        player.getZ()
                                + (random.nextDouble() - 0.5)
                                * 8,

                        0,
                        -0.005,
                        0
                );

            } else if (level.isRaining()
                    && Vanilla3DPlusConfig.weatherEffects) {

                level.addParticle(
                        ParticleTypes.CLOUD,

                        player.getX()
                                + (random.nextDouble() - 0.5)
                                * 10,

                        y + 2
                                + random.nextDouble() * 3,

                        player.getZ()
                                + (random.nextDouble() - 0.5)
                                * 10,

                        0,
                        0.002,
                        0
                );
            }
        }

        public static void updateWetWorld(
                Minecraft client, VisualState state
        ) {
    if (!Vanilla3DPlusConfig.materialEffects
                    && !Vanilla3DPlusConfig.weatherEffects) {
                return;
            }

            ClientLevel level = client.level;
            Player player = client.player;

            var random = level.getRandom();

            if (!level.isRaining()) {
                return;
            }

            if (state.tick % 10 != 0) {
                return;
            }

            BlockPos pos =
                    player.blockPosition();

            var groundState =
                    level.getBlockState(
                            pos.below()
                    );

            boolean naturalGround =
                    groundState.is(Blocks.GRASS_BLOCK)
                            || groundState.is(Blocks.DIRT)
                            || groundState.is(Blocks.COARSE_DIRT)
                            || groundState.is(Blocks.ROOTED_DIRT)
                            || groundState.is(Blocks.MOSS_BLOCK)
                            || groundState.is(Blocks.SAND)
                            || groundState.is(Blocks.RED_SAND);

            if (!naturalGround) {
                return;
            }

            if (random.nextFloat() > 0.32f) {
                return;
            }

            level.addParticle(
                    ParticleTypes.DRIPPING_WATER,

                    player.getX()
                            + (random.nextDouble() - 0.5)
                            * 1.2,

                    player.getY()
                            + 0.8
                            + random.nextDouble() * 1.5,

                    player.getZ()
                            + (random.nextDouble() - 0.5)
                            * 1.2,

                    0,
                    -0.02,
                    0
            );
        }
}

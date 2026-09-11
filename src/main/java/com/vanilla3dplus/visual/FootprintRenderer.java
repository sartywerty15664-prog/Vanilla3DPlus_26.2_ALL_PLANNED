package com.vanilla3dplus.visual;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Blocks;

public final class FootprintRenderer {
    private FootprintRenderer() {}

        public static void updateGroundDustAndFootprints(
                Minecraft client, VisualState state
        ) {

            if (!Vanilla3DPlusConfig.footprints) {
                return;
            }

            Player player = client.player;
            ClientLevel level = client.level;

            var random = level.getRandom();

            if (!player.onGround()
                    || player.isShiftKeyDown()
                    || player.isInWater()
                    || player.isFallFlying()) {
                return;
            }

            if (player.getDeltaMovement()
                    .horizontalDistanceSqr() < 0.002) {
                return;
            }

            if (state.tick % 4 != 0) {
                return;
            }

            BlockPos below =
                    player.blockPosition()
                            .below();

            var groundState =
                    level.getBlockState(below);

            boolean sand =
                    groundState.is(Blocks.SAND)
                            || groundState.is(Blocks.RED_SAND);

            boolean dirt =
                    groundState.is(Blocks.DIRT)
                            || groundState.is(Blocks.COARSE_DIRT)
                            || groundState.is(Blocks.ROOTED_DIRT);

            boolean gravel =
                    groundState.is(Blocks.GRAVEL);

            boolean snow =
                    groundState.is(Blocks.SNOW_BLOCK)
                            || groundState.is(Blocks.SNOW);

            boolean grass =
                    groundState.is(Blocks.GRASS_BLOCK)
                            || groundState.is(Blocks.MOSS_BLOCK);

            if (sand || dirt || gravel || grass) {

                int count =
                        sand
                                ? 2
                                : 1;

                if (Vanilla3DPlusConfig.quality
                        == Vanilla3DPlusConfig.Quality.LOW) {
                    count = 1;
                }

                if (Vanilla3DPlusConfig.quality
                        == Vanilla3DPlusConfig.Quality.HIGH
                        && sand) {
                    count = 3;
                }

                for (int i = 0;
                     i < count;
                     i++) {

                    level.addParticle(
                            ParticleTypes.POOF,

                            player.getX()
                                    + (random.nextDouble() - 0.5)
                                    * 0.45,

                            below.getY()
                                    + 1.015,

                            player.getZ()
                                    + (random.nextDouble() - 0.5)
                                    * 0.45,

                            (random.nextDouble() - 0.5)
                                    * 0.015,

                            0.008,

                            (random.nextDouble() - 0.5)
                                    * 0.015
                    );
                }

            } else if (snow) {

                level.addParticle(
                        ParticleTypes.SNOWFLAKE,

                        player.getX()
                                + (random.nextDouble() - 0.5)
                                * 0.3,

                        below.getY()
                                + 1.02,

                        player.getZ()
                                + (random.nextDouble() - 0.5)
                                * 0.3,

                        0,
                        0.012,
                        0
                );
            }

            /*
             * Soft temporary track particle.
             */
            if (state.tick % 8 == 0
                    && (sand || dirt || snow)) {
    ParticleOptions footprintParticle =
                        snow
                                ? ParticleTypes.SNOWFLAKE
                                : ParticleTypes.POOF;

                level.addParticle(
                        footprintParticle,

                        player.getX()
                                - player.getDeltaMovement().x
                                * 0.8,

                        below.getY()
                                + 1.018,

                        player.getZ()
                                - player.getDeltaMovement().z
                                * 0.8,

                        0,
                        0.004,
                        0
                );
            }
        }

        public static void updateHardLanding(
                Minecraft client, VisualState state
        ) {

            if (!Vanilla3DPlusConfig.footprints) {
                return;
            }

            Player player = client.player;
            ClientLevel level = client.level;

            var random = level.getRandom();

            if (!player.onGround()
                    || state.wasOnGround
                    || state.lastFallDistance <= 2.0
                    || state.tick % 2 != 0) {
                return;
            }

            float impact =
                    (float) VisualMath.clamp(
                            state.lastFallDistance / 12.0,
                            0.0,
                            1.0
                    );

            int count =
                    3
                            + (int)
                            (impact * 14);

            if (Vanilla3DPlusConfig.quality
                    == Vanilla3DPlusConfig.Quality.LOW) {
                count /= 2;
            }

            for (int i = 0;
                 i < count;
                 i++) {

                level.addParticle(
                        ParticleTypes.POOF,

                        player.getX()
                                + (random.nextDouble() - 0.5)
                                * (0.6 + impact),

                        player.getY()
                                + 0.02,

                        player.getZ()
                                + (random.nextDouble() - 0.5)
                                * (0.6 + impact),

                        (random.nextDouble() - 0.5)
                                * 0.12,

                        0.025
                                + impact * 0.08,

                        (random.nextDouble() - 0.5)
                                * 0.12
                );
            }

            /*
             * Strong landing gets one extra impact particle.
             */
            if (impact > 0.55
                    && Vanilla3DPlusConfig.quality
                    == Vanilla3DPlusConfig.Quality.HIGH) {

                level.addParticle(
                        ParticleTypes.EXPLOSION,

                        player.getX(),
                        player.getY() + 0.03,
                        player.getZ(),

                        0,
                        0,
                        0
                );
            }
        }
}

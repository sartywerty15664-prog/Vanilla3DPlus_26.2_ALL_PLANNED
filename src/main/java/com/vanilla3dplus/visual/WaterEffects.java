package com.vanilla3dplus.visual;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FluidState;

public final class WaterEffects {
    private WaterEffects() {}

        public static void updateWaterSplash(
                Minecraft client, VisualState state
        ) {

            if (!Vanilla3DPlusConfig.waterEffects) {
                return;
            }

            Player player = client.player;
            ClientLevel level = client.level;

            var random = level.getRandom();

            FluidState fluid =
                    level.getFluidState(
                            player.blockPosition()
                    );

            boolean inWater =
                    fluid.is(FluidTags.WATER);

            /*
             * Trigger only when entering water.
             */
            if (inWater && !state.wasInWater) {

                double fallDistance =
                        Math.max(
                                0.0,
                                player.fallDistance
                        );

                double verticalSpeed =
                        Math.max(
                                0.0,
                                -state.previousVerticalVelocity
                        );

                double movementFall =
                        Math.max(
                                0.0,
                                Math.abs(
                                        state.previousY
                                                - player.getY()
                                )
                        );

                double impact =
                        VisualMath.clamp(
                                fallDistance / 12.0
                                        + verticalSpeed * 0.35
                                        + movementFall * 0.08,
                                0.0,
                                1.0
                        );

                int count =
                        5
                                + (int)
                                (impact * 22);

                if (Vanilla3DPlusConfig.quality
                        == Vanilla3DPlusConfig.Quality.LOW) {
                    count /= 2;
                }

                double spread =
                        0.35
                                + impact * 1.1;

                for (int i = 0;
                     i < count;
                     i++) {

                    level.addParticle(
                            ParticleTypes.SPLASH,

                            player.getX()
                                    + (random.nextDouble() - 0.5)
                                    * spread,

                            player.getY()
                                    + 0.04
                                    + random.nextDouble()
                                    * 0.12,

                            player.getZ()
                                    + (random.nextDouble() - 0.5)
                                    * spread,

                            (random.nextDouble() - 0.5)
                                    * (0.08
                                    + impact * 0.35),

                            0.08
                                    + impact * 0.34,

                            (random.nextDouble() - 0.5)
                                    * (0.08
                                    + impact * 0.35)
                    );
                }

                /*
                 * Extra mist for hard impacts.
                 */
                if (impact > 0.45
                        && Vanilla3DPlusConfig.quality
                        != Vanilla3DPlusConfig.Quality.LOW) {
    int mistCount =
                            1
                                    + (int)
                                    (impact * 4);

                    for (int i = 0;
                         i < mistCount;
                         i++) {

                        level.addParticle(
                                ParticleTypes.CLOUD,

                                player.getX()
                                        + (random.nextDouble() - 0.5)
                                        * spread,

                                player.getY()
                                        + 0.12,

                                player.getZ()
                                        + (random.nextDouble() - 0.5)
                                        * spread,

                                0,

                                0.04
                                        + impact * 0.05,

                                0
                        );
                    }
                }
            }

            state.wasInWater = inWater;
        }
}

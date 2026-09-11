package com.vanilla3dplus.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shader-safe terrain relief pass. It submits a few nearby exposed block models
 * again with a tiny vertical lift and light tint, producing visible layered depth
 * on ledges, paths and terrain edges without raw OpenGL.
 */
public final class ReliefRenderer {
    private ReliefRenderer() {}

    private static boolean registered;

    public static void register() {
        if (registered) return;
        registered = true;
        LevelRenderEvents.COLLECT_SUBMITS.register(ReliefRenderer::collect);
    }

    public static void update(Minecraft client, VisualState state) {
        // Rendering is event-driven in 26.2; no per-tick OpenGL work is needed.
    }

    private static void collect(LevelRenderContext context) {
        if (!Vanilla3DPlusConfig.relief3D) return;

        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;
        int radius = switch (Vanilla3DPlusConfig.quality) {
            case LOW -> 2;
            case MEDIUM -> 3;
            case HIGH -> 4;
        };

        BlockPos origin = client.player.blockPosition();
        PoseStack poseStack = context.poseStack();
        int submitted = 0;

        for (int dx = -radius; dx <= radius && submitted < 34; dx++) {
            for (int dz = -radius; dz <= radius && submitted < 34; dz++) {
                if (dx * dx + dz * dz > radius * radius) continue;

                BlockPos pos = origin.offset(dx, 0, dz);
                int topY = client.level.getHeightmapPos(
                        net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                        pos
                ).getY();
                BlockPos surface = new BlockPos(pos.getX(), topY - 1, pos.getZ());
                BlockState block = client.level.getBlockState(surface);

                if (block.isAir() || block.is(Blocks.WATER) || block.is(Blocks.LAVA)) continue;
                if (!client.level.getBlockState(surface.above()).isAir()) continue;

                
            }
        }
    }
}

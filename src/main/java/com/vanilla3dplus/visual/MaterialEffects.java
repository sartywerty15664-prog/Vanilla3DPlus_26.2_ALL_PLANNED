package com.vanilla3dplus.visual;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public final class MaterialEffects {
    private MaterialEffects() {}

    public static void updateMaterialLight(Minecraft client, VisualState state) {
        if (!Vanilla3DPlusConfig.materialEffects || client.player == null || client.level == null) return;
        if (state.tick % 3 != 0) return;

        Player player = client.player;
        ClientLevel level = client.level;
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty()) return;

        String name = held.getItem().toString().toLowerCase(Locale.ROOT);
        int color = colorFor(name);
        if (color == 0) return;

        float brightness = level.getMaxLocalRawBrightness(player.blockPosition());
        if (brightness < 5 && level.getSkyDarken() > 8) return;

        var random = level.getRandom();
        ParticleOptions particle = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT,
                ((color >> 16) & 255) / 255.0f, ((color >> 8) & 255) / 255.0f, (color & 255) / 255.0f);

        int count = Vanilla3DPlusConfig.quality == Vanilla3DPlusConfig.Quality.HIGH ? 4 : 2;
        for (int i = 0; i < count; i++) {
            level.addParticle(particle,
                    player.getX() + (random.nextDouble() - 0.5) * 0.55,
                    player.getY() + 0.85 + random.nextDouble() * 0.55,
                    player.getZ() + (random.nextDouble() - 0.5) * 0.55,
                    0, 0.012 + random.nextDouble() * 0.012, 0);
        }
    }

    private static int colorFor(String name) {
        if (VisualMath.containsAny(name, "netherite")) return 0xB86BFF;
        if (VisualMath.containsAny(name, "diamond")) return 0x54EFFF;
        if (VisualMath.containsAny(name, "copper")) return 0xFF8A45;
        if (VisualMath.containsAny(name, "gold")) return 0xFFD94A;
        if (VisualMath.containsAny(name, "iron")) return 0xDDEBFF;
        if (VisualMath.containsAny(name, "spear")) return 0xE6F4FF;
        if (VisualMath.containsAny(name, "mace")) return 0xF1D6FF;
        return 0;
    }
}

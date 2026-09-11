package com.vanilla3dplus.visual;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

/** Visible material aura for worn armor. Uses the same shader-safe particle path as the other effects. */
public final class ArmorEffectsRenderer {
    private ArmorEffectsRenderer() {}

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    public static void update(Minecraft client, VisualState state) {
        if (!Vanilla3DPlusConfig.materialEffects || client.player == null || client.level == null) return;
        if (state.tick % 3 != 0) return;

        Player player = client.player;
        ClientLevel level = client.level;
        var random = level.getRandom();

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty()) continue;

            int color = colorFor(stack);
            if (color == 0) continue;

            int count = switch (Vanilla3DPlusConfig.quality) {
                case LOW -> 1;
                case MEDIUM -> 2;
                case HIGH -> 3;
            };

            double baseY = switch (slot) {
                case HEAD -> player.getY() + 1.68;
                case CHEST -> player.getY() + 1.12;
                case LEGS -> player.getY() + 0.62;
                case FEET -> player.getY() + 0.18;
                default -> player.getY() + 1.0;
            };

            float red = ((color >> 16) & 255) / 255.0f;
            float green = ((color >> 8) & 255) / 255.0f;
            float blue = (color & 255) / 255.0f;
            ColorParticleOption particle = ColorParticleOption.create(
                    ParticleTypes.ENTITY_EFFECT, red, green, blue);

            for (int i = 0; i < count; i++) {
                double angle = random.nextDouble() * Math.PI * 2.0;
                double radius = 0.24 + random.nextDouble() * 0.34;
                double x = player.getX() + Math.cos(angle) * radius;
                double y = baseY + (random.nextDouble() - 0.5) * 0.38;
                double z = player.getZ() + Math.sin(angle) * radius;

                level.addParticle(particle, x, y, z, 0.0, 0.008, 0.0);
            }
        }
    }

    private static int colorFor(ItemStack stack) {
        String name = stack.getItem().toString().toLowerCase(Locale.ROOT);
        if (name.contains("netherite")) return 0xB86BFF;
        if (name.contains("diamond")) return 0x56F3FF;
        if (name.contains("gold")) return 0xFFD84A;
        if (name.contains("copper")) return 0xFF8A45;
        if (name.contains("iron")) return 0xD9E6F2;
        if (name.contains("chainmail")) return 0xB9C7D3;
        if (name.contains("turtle")) return 0x55D68A;
        if (name.contains("leather")) return 0xB86F42;
        return 0;
    }
}

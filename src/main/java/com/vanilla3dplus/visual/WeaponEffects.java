package com.vanilla3dplus.visual;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public final class WeaponEffects {
    private WeaponEffects() {}

    public static void updateHeldWeaponEffects(Minecraft client, VisualState state) {
        if (!Vanilla3DPlusConfig.weaponEffects || client.player == null || client.level == null) return;

        Player player = client.player;
        ClientLevel level = client.level;
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty()) return;

        String name = held.getItem().toString().toLowerCase(Locale.ROOT);
        boolean weapon = VisualMath.containsAny(name, "sword", "spear", "mace", "trident", "bow", "crossbow");
        if (!weapon) return;

        var random = level.getRandom();
        int color = name.contains("spear") ? 0xDCEBFF : name.contains("mace") ? 0xD9A8FF : 0xA8D8FF;
        var aura = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT,
                ((color >> 16) & 255) / 255.0f, ((color >> 8) & 255) / 255.0f, (color & 255) / 255.0f);

        if (state.tick % 2 == 0) {
            level.addParticle(aura,
                    player.getX() + (random.nextDouble() - 0.5) * 0.45,
                    player.getY() + 0.9 + random.nextDouble() * 0.6,
                    player.getZ() + (random.nextDouble() - 0.5) * 0.45,
                    0, 0.01, 0);
        }

        double speed = player.getDeltaMovement().length();
        boolean falling = !player.onGround() && player.getDeltaMovement().y < -0.08;

        if (name.contains("spear") && speed > 0.10 && state.tick % 2 == 0) {
            level.addParticle(ParticleTypes.END_ROD,
                    player.getX() - player.getDeltaMovement().x * 1.2,
                    player.getY() + 1.0,
                    player.getZ() - player.getDeltaMovement().z * 1.2,
                    -player.getDeltaMovement().x * 0.15, 0.005, -player.getDeltaMovement().z * 0.15);
        }

        if (name.contains("mace") && falling && player.fallDistance > 2.0 && state.tick % 2 == 0) {
            double strength = VisualMath.clamp(player.fallDistance / 12.0, 0.0, 1.0);
            int count = 2 + (int)(strength * 6);
            for (int i = 0; i < count; i++) {
                level.addParticle(ParticleTypes.CRIT,
                        player.getX() + (random.nextDouble() - 0.5) * 0.65,
                        player.getY() + 0.25 + random.nextDouble() * 0.85,
                        player.getZ() + (random.nextDouble() - 0.5) * 0.65,
                        (random.nextDouble() - 0.5) * strength * 0.2,
                        0.04 + strength * 0.08,
                        (random.nextDouble() - 0.5) * strength * 0.2);
            }
        }
    }
}

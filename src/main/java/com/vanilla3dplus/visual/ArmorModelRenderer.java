package com.vanilla3dplus.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Re-renders vanilla humanoid armor through Fabric's 26.2 ArmorRenderer API.
 * The model remains vanilla-shaped, but the material is tinted and receives
 * a subtle animated highlight, making armor visibly different without raw GL.
 */
public final class ArmorModelRenderer {
    private ArmorModelRenderer() {}

    public static void register() {
        ArmorRenderer renderer = ArmorModelRenderer::render;
        ArmorRenderer.register(renderer,
                Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS,
                Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS,
                Items.COPPER_HELMET, Items.COPPER_CHESTPLATE, Items.COPPER_LEGGINGS, Items.COPPER_BOOTS,
                Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
                Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS,
                Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS,
                Items.TURTLE_HELMET
        );
    }

    private static void render(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            ItemStack stack,
            HumanoidRenderState humanoidRenderState,
            EquipmentSlot slot,
            int light,
            HumanoidModel<HumanoidRenderState> contextModel
    ) {
        if (!Vanilla3DPlusConfig.materialEffects || !(stack.getItem() instanceof ArmorItem armor)) {
            return;
        }
        if (!(submitNodeCollector instanceof OrderedSubmitNodeCollector ordered)) {
            return;
        }

        Identifier texture = armorTexture(armor, slot);
        RenderType renderType = RenderType.entityCutoutNoCull(texture);
        int tint = tintColor(stack, armor, (System.currentTimeMillis() % 8000L) * 0.001f);

        ArmorRenderer.submitTransformCopyingModel(
                contextModel,
                humanoidRenderState,
                contextModel,
                humanoidRenderState,
                false,
                ordered,
                poseStack,
                renderType,
                light,
                0,
                tint,
                null,
                0,
                null
        );
    }

    private static Identifier armorTexture(ArmorItem armor, EquipmentSlot slot) {
        String material = armor.getMaterial().value().getName();
        int layer = slot == EquipmentSlot.LEGS ? 2 : 1;
        return Identifier.fromNamespaceAndPath(
                "minecraft",
                "textures/models/armor/" + material + "_layer_" + layer + ".png"
        );
    }

    private static int tintColor(ItemStack stack, ArmorItem armor, float age) {
        String material = armor.getMaterial().value().getName();
        int base = switch (material) {
            case "netherite" -> 0xFFB78AFF;
            case "diamond" -> 0xFF8DF4FF;
            case "copper" -> 0xFFFFA05C;
            case "gold" -> 0xFFFFE15A;
            case "iron" -> 0xFFE8F0FF;
            case "chainmail" -> 0xFFC9D8E5;
            case "turtle_scute" -> 0xFF72E0A1;
            case "leather" -> 0xFFFFFFFF;
            default -> 0xFFFFFFFF;
        };

        float pulse = 0.92f + (float) Math.sin(age * 0.075f) * 0.08f;
        int a = (base >>> 24) & 255;
        int r = Math.min(255, Math.max(0, Math.round(((base >>> 16) & 255) * pulse)));
        int g = Math.min(255, Math.max(0, Math.round(((base >>> 8) & 255) * pulse)));
        int b = Math.min(255, Math.max(0, Math.round((base & 255) * pulse)));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }


}

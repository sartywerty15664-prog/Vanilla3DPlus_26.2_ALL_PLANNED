 package com.vanilla3dplus.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Locale;

public final class ArmorModelRenderer {

    private ArmorModelRenderer() {
    }

    public static void register() {
        ArmorRenderer renderer = ArmorModelRenderer::render;

        ArmorRenderer.register(
                renderer,

                Items.LEATHER_HELMET,
                Items.LEATHER_CHESTPLATE,
                Items.LEATHER_LEGGINGS,
                Items.LEATHER_BOOTS,

                Items.CHAINMAIL_HELMET,
                Items.CHAINMAIL_CHESTPLATE,
                Items.CHAINMAIL_LEGGINGS,
                Items.CHAINMAIL_BOOTS,

                Items.IRON_HELMET,
                Items.IRON_CHESTPLATE,
                Items.IRON_LEGGINGS,
                Items.IRON_BOOTS,

                Items.COPPER_HELMET,
                Items.COPPER_CHESTPLATE,
                Items.COPPER_LEGGINGS,
                Items.COPPER_BOOTS,

                Items.GOLDEN_HELMET,
                Items.GOLDEN_CHESTPLATE,
                Items.GOLDEN_LEGGINGS,
                Items.GOLDEN_BOOTS,

                Items.DIAMOND_HELMET,
                Items.DIAMOND_CHESTPLATE,
                Items.DIAMOND_LEGGINGS,
                Items.DIAMOND_BOOTS,

                Items.NETHERITE_HELMET,
                Items.NETHERITE_CHESTPLATE,
                Items.NETHERITE_LEGGINGS,
                Items.NETHERITE_BOOTS,

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
        if (!Vanilla3DPlusConfig.materialEffects) {
            return;
        }

        if (stack.isEmpty()) {
            return;
        }

        if (!(submitNodeCollector instanceof OrderedSubmitNodeCollector ordered)) {
            return;
        }

        String material = getMaterialName(stack.getItem());

        if (material == null) {
            return;
        }

        Identifier texture = createArmorTexture(material, slot);

        /*
         * 26.2 does not expose the old entityCutoutNoCull path
         * used by previous versions.
         *
         * ArmorRenderer only needs a valid RenderType here.
         */
        RenderType renderType = RenderTypes.armorCutoutNoCull(texture);

        float animationTime =
                (System.currentTimeMillis() % 6000L) / 1000.0f;

        int tint = getTint(material, animationTime);

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

    private static String getMaterialName(Item item) {
        String name = item.toString().toLowerCase(Locale.ROOT);

        if (name.contains("leather")) {
            return "leather";
        }

        if (name.contains("chainmail")) {
            return "chainmail";
        }
         if (name.contains("iron")) {
            return "iron";
        }

        if (name.contains("copper")) {
            return "copper";
        }

        if (name.contains("gold")) {
            return "gold";
        }

        if (name.contains("diamond")) {
            return "diamond";
        }

        if (name.contains("netherite")) {
            return "netherite";
        }

        if (name.contains("turtle")) {
            return "turtle";
        }

        return null;
    }

    private static Identifier createArmorTexture(
            String material,
            EquipmentSlot slot
    ) {
        int layer = slot == EquipmentSlot.LEGS ? 2 : 1;

        String path =
                "textures/models/armor/"
                        + material
                        + "_layer_"
                        + layer
                        + ".png";

        return Identifier.fromNamespaceAndPath(
                "minecraft",
                path
        );
    }

    private static int getTint(
            String material,
            float time
    ) {
        int baseColor;

        switch (material) {
            case "leather" -> baseColor = 0xFFFFFFFF;

            case "chainmail" -> baseColor = 0xFFD8E4EE;

            case "iron" -> baseColor = 0xFFEAF2FF;

            case "copper" -> baseColor = 0xFFFF9B5C;

            case "gold" -> baseColor = 0xFFFFE45C;

            case "diamond" -> baseColor = 0xFF7CF3FF;

            case "netherite" -> baseColor = 0xFFB889FF;

            case "turtle" -> baseColor = 0xFF73E5A1;

            default -> baseColor = 0xFFFFFFFF;
        }

        /*
         * Very subtle animated material highlight.
         * The armor itself remains vanilla-shaped.
         */
        float pulse =
                0.94f
                        + (float) Math.sin(time * 2.0f) * 0.06f;

        int red =
                Math.min(
                        255,
                        Math.max(
                                0,
                                Math.round(
                                        ((baseColor >> 16) & 255) * pulse
                                )
                        )
                );

        int green =
                Math.min(
                        255,
                        Math.max(
                                0,
                                Math.round(
                                        ((baseColor >> 8) & 255) * pulse
                                )
                        )
                );

        int blue =
                Math.min(
                        255,
                        Math.max(
                                0,
                                Math.round(
                                        (baseColor & 255) * pulse
                                )
                        )
                );

        return
                0xFF000000
                        | (red << 16)
                        | (green << 8)
                        | blue;
    }
}
package com.vanilla3dplus;

import com.mojang.blaze3d.platform.InputConstants;
import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import com.vanilla3dplus.gui.Vanilla3DPlusConfigScreen;
import com.vanilla3dplus.visual.ArmorEffectsRenderer;
import com.vanilla3dplus.visual.ArmorModelRenderer;
import com.vanilla3dplus.visual.AtmosphereRenderer;
import com.vanilla3dplus.visual.CloudRenderer;
import com.vanilla3dplus.visual.FootprintRenderer;
import com.vanilla3dplus.visual.HudVisualRenderer;
import com.vanilla3dplus.visual.MaterialEffects;
import com.vanilla3dplus.visual.RainEffects;
import com.vanilla3dplus.visual.RainbowRenderer;
import com.vanilla3dplus.visual.ReliefRenderer;
import com.vanilla3dplus.visual.VisualState;
import com.vanilla3dplus.visual.WaterEffects;
import com.vanilla3dplus.visual.WeaponEffects;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;

public final class Vanilla3DPlusClient implements ClientModInitializer {
    public static final String MOD_ID = "vanilla_3_d_plus";

    private static final KeyMapping.Category SETTINGS_CATEGORY =
            KeyMapping.Category.register(
                    Identifier.fromNamespaceAndPath(MOD_ID, "settings"));

    private static final KeyMapping SETTINGS_KEY =
            KeyMappingHelper.registerKeyMapping(
                    new KeyMapping(
                            "key.vanilla_3_d_plus.settings",
                            InputConstants.Type.KEYSYM,
                            InputConstants.KEY_O,
                            SETTINGS_CATEGORY));

    private final VisualState state = new VisualState();

    @Override
    public void onInitializeClient() {
        Vanilla3DPlusConfig.load();
        ArmorModelRenderer.register();
        ReliefRenderer.register();

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(MOD_ID, "visual_effects"),
                HudVisualRenderer::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null) return;

            state.tick++;

            if (SETTINGS_KEY.consumeClick()) {
                Screen currentScreen = client.gui.screen();
                client.gui.setScreen(new Vanilla3DPlusConfigScreen(currentScreen));
            }

            RainEffects.updateRain(client, state);
            FootprintRenderer.updateGroundDustAndFootprints(client, state);
            WaterEffects.updateWaterSplash(client, state);
            FootprintRenderer.updateHardLanding(client, state);
            AtmosphereRenderer.updateAtmosphere(client, state);
            AtmosphereRenderer.updateWetWorld(client, state);
            RainbowRenderer.update(client, state);
            MaterialEffects.updateMaterialLight(client, state);
            ArmorEffectsRenderer.update(client, state);
            WeaponEffects.updateHeldWeaponEffects(client, state);
            CloudRenderer.update(client, state);
            ReliefRenderer.update(client, state);

            state.previousY = client.player.getY();
            state.previousVerticalVelocity = client.player.getDeltaMovement().y;
            state.wasOnGround = client.player.onGround();
            state.wasRaining = client.level.isRaining();
            state.lastFallDistance = client.player.fallDistance;
        });
    }
}

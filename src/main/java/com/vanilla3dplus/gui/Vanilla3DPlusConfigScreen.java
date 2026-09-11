package com.vanilla3dplus.gui;

import com.vanilla3dplus.config.Vanilla3DPlusConfig;
import com.vanilla3dplus.config.Vanilla3DPlusConfig.Quality;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class Vanilla3DPlusConfigScreen extends Screen {

    private final Screen parent;

    private Button weatherButton;
    private Button waterButton;
    private Button footprintsButton;
    private Button fogButton;
    private Button rainbowButton;
    private Button cloudsButton;
    private Button materialButton;
    private Button weaponButton;
    private Button reliefButton;
    private Button qualityButton;

    public Vanilla3DPlusConfigScreen(Screen parent) {
        super(Component.literal("Vanilla 3+ Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        int buttonWidth = 260;
        int buttonHeight = 20;

        int x = centerX - buttonWidth / 2;
        int y = 45;
        int gap = 24;

        weatherButton = addToggle(
                x, y, buttonWidth, buttonHeight,
                "Weather Effects",
                () -> Vanilla3DPlusConfig.weatherEffects,
                value -> Vanilla3DPlusConfig.weatherEffects = value
        );
        y += gap;

        waterButton = addToggle(
                x, y, buttonWidth, buttonHeight,
                "Water Effects",
                () -> Vanilla3DPlusConfig.waterEffects,
                value -> Vanilla3DPlusConfig.waterEffects = value
        );
        y += gap;

        footprintsButton = addToggle(
                x, y, buttonWidth, buttonHeight,
                "Footprints",
                () -> Vanilla3DPlusConfig.footprints,
                value -> Vanilla3DPlusConfig.footprints = value
        );
        y += gap;

        fogButton = addToggle(
                x, y, buttonWidth, buttonHeight,
                "Dynamic Fog",
                () -> Vanilla3DPlusConfig.dynamicFog,
                value -> Vanilla3DPlusConfig.dynamicFog = value
        );
        y += gap;

        rainbowButton = addToggle(
                x, y, buttonWidth, buttonHeight,
                "Rainbow",
                () -> Vanilla3DPlusConfig.rainbow,
                value -> Vanilla3DPlusConfig.rainbow = value
        );
        y += gap;

        cloudsButton = addToggle(
                x, y, buttonWidth, buttonHeight,
                "Clouds",
                () -> Vanilla3DPlusConfig.clouds,
                value -> Vanilla3DPlusConfig.clouds = value
        );
        y += gap;

        materialButton = addToggle(
                x, y, buttonWidth, buttonHeight,
                "Material Effects",
                () -> Vanilla3DPlusConfig.materialEffects,
                value -> Vanilla3DPlusConfig.materialEffects = value
        );
        y += gap;

        weaponButton = addToggle(
                x, y, buttonWidth, buttonHeight,
                "Weapon Effects",
                () -> Vanilla3DPlusConfig.weaponEffects,
                value -> Vanilla3DPlusConfig.weaponEffects = value
        );
        y += gap;

        reliefButton = addToggle(
                x, y, buttonWidth, buttonHeight,
                "3D Relief",
                () -> Vanilla3DPlusConfig.relief3D,
                value -> Vanilla3DPlusConfig.relief3D = value
        );
        y += gap;

        qualityButton = addQualityButton(
                x, y, buttonWidth, buttonHeight
        );

        addRenderableWidget(
                Button.builder(
                        Component.literal("Done"),
                        button -> {
                            Vanilla3DPlusConfig.save();
                            closeToParent();
                        }
                ).bounds(
                        centerX - 130,
                        this.height - 35,
                        125,
                        20
                ).build()
        );
addRenderableWidget(
                Button.builder(
                        Component.literal("Reset"),
                        button -> {
                            Vanilla3DPlusConfig.setDefaults();
                            refreshLabels();
                        }
                ).bounds(
                        centerX + 5,
                        this.height - 35,
                        125,
                        20
                ).build()
        );
    }

    private Button addToggle(
            int x,
            int y,
            int width,
            int height,
            String name,
            BooleanSupplier getter,
            BooleanConsumer setter
    ) {
        Button button = Button.builder(
                Component.literal(
                        getLabel(name, getter.getAsBoolean())
                ),
                clicked -> {
                    setter.accept(!getter.getAsBoolean());

                    clicked.setMessage(
                            Component.literal(
                                    getLabel(name, getter.getAsBoolean())
                            )
                    );
                }
        ).bounds(
                x,
                y,
                width,
                height
        ).build();

        addRenderableWidget(button);

        return button;
    }

    private Button addQualityButton(
            int x,
            int y,
            int width,
            int height
    ) {
        Button button = Button.builder(
                Component.literal(
                        getQualityLabel()
                ),
                clicked -> {
                    Vanilla3DPlusConfig.quality =
                            switch (Vanilla3DPlusConfig.quality) {
                                case LOW -> Quality.MEDIUM;
                                case MEDIUM -> Quality.HIGH;
                                case HIGH -> Quality.LOW;
                            };

                    clicked.setMessage(
                            Component.literal(
                                    getQualityLabel()
                            )
                    );
                }
        ).bounds(
                x,
                y,
                width,
                height
        ).build();

        addRenderableWidget(button);

        return button;
    }

    private String getQualityLabel() {
        return "Effect Quality: "
                + Vanilla3DPlusConfig.quality.name();
    }

    private void refreshLabels() {
        weatherButton.setMessage(
                Component.literal(
                        getLabel(
                                "Weather Effects",
                                Vanilla3DPlusConfig.weatherEffects
                        )
                )
        );

        waterButton.setMessage(
                Component.literal(
                        getLabel(
                                "Water Effects",
                                Vanilla3DPlusConfig.waterEffects
                        )
                )
        );

        footprintsButton.setMessage(
                Component.literal(
                        getLabel(
                                "Footprints",
                                Vanilla3DPlusConfig.footprints
                        )
                )
        );

        fogButton.setMessage(
                Component.literal(
                        getLabel(
                                "Dynamic Fog",
                                Vanilla3DPlusConfig.dynamicFog
                        )
                )
        );

        rainbowButton.setMessage(
                Component.literal(
                        getLabel(
                                "Rainbow",
                                Vanilla3DPlusConfig.rainbow
                        )
                )
        );
cloudsButton.setMessage(
                Component.literal(
                        getLabel(
                                "Clouds",
                                Vanilla3DPlusConfig.clouds
                        )
                )
        );

        materialButton.setMessage(
                Component.literal(
                        getLabel(
                                "Material Effects",
                                Vanilla3DPlusConfig.materialEffects
                        )
                )
        );

        weaponButton.setMessage(
                Component.literal(
                        getLabel(
                                "Weapon Effects",
                                Vanilla3DPlusConfig.weaponEffects
                        )
                )
        );

        reliefButton.setMessage(
                Component.literal(
                        getLabel(
                                "3D Relief",
                                Vanilla3DPlusConfig.relief3D
                        )
                )
        );

        qualityButton.setMessage(
                Component.literal(
                        getQualityLabel()
                )
        );
    }

    private String getLabel(String name, boolean enabled) {
        return name + ": " + (enabled ? "ON" : "OFF");
    }

    private void closeToParent() {
        Vanilla3DPlusConfig.save();

        if (this.minecraft != null) {
            this.minecraft.gui.setScreen(parent);
        }
    }

    @Override
    public void onClose() {
        Vanilla3DPlusConfig.save();

        if (this.minecraft != null) {
            this.minecraft.gui.setScreen(parent);
        }
    }

    @FunctionalInterface
    private interface BooleanSupplier {
        boolean getAsBoolean();
    }

    @FunctionalInterface
    private interface BooleanConsumer {
        void accept(boolean value);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}

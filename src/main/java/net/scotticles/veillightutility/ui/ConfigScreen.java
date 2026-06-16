package net.scotticles.veillightutility.ui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.scotticles.veillightutility.settings.VeilLightUtilityConfig;


public class ConfigScreen extends Screen {

    // ────────────────── Layout Constants ──────────────────

    private static final int startY = 80;
    private static final int labelSpacing = 20;

    // ────────────────── UI References ──────────────────

    private ButtonWidget savingMethodButton;

    public ConfigScreen() {
        //Screen Title
        super(Text.literal("VLU Config Screen"));
    }

    // ────────────────── Screen Init (Runs On Screen Being Opened) ──────────────────

    @Override
    protected void init() {
        super.init();

            addConfigButtons();

            if (VeilLightUtilityConfig.enableTotalSaving) {
                savingMethodButton.setMessage(Text.literal("Save Method: Total"));
            }
            else {
                savingMethodButton.setMessage(Text.literal("Save Method: Compatible"));
            }

            addConfigLabels();
    }

    private void addConfigButtons() {
        savingMethodButton = addButton("Save Method:", (width - 140) / 2, startY, 140, 20, () -> {
            if (VeilLightUtilityConfig.enableTotalSaving) {
                VeilLightUtilityConfig.enableTotalSaving = false;
                VeilLightUtilityConfig.save();
                savingMethodButton.setMessage(Text.literal("Save Method: Compatible"));
            }
            else {
                VeilLightUtilityConfig.enableTotalSaving = true;
                VeilLightUtilityConfig.save();
                savingMethodButton.setMessage(Text.literal("Save Method: Total"));
            }
        });
    }

    private void addConfigLabels() {

        addButton("Done", this.width / 2 - 100, this.height / 4 + 48 + 24 * 4, 200, 20, this::close);

        addText("Test Label", (this.width / 2) - 100, 20, 200, 20);

        addText("Set Saving Method", (this.width / 2) - 100, savingMethodButton.getY() - labelSpacing, 200, 20);
    }


    // ────────────────── Helper Functions ──────────────────

    private ButtonWidget addButton(String buttonName, int xPos, int yPos, int buttonWidth, int buttonHeight, Runnable action) {
        ButtonWidget button = ButtonWidget.builder(Text.literal(buttonName), b -> action.run())
                .dimensions(
                        xPos,
                        yPos, buttonWidth, buttonHeight)
                .build();
        addDrawableChild(button);
        return button;
    }

    private TextWidget addText(String textContent, int xPos, int yPos, int textWidth, int textHeight) {
        TextWidget text = new TextWidget(
                xPos,
                yPos,
                textWidth,
                textHeight,
                Text.literal(textContent),
                this.textRenderer
        );
        addDrawableChild(text);
        return text;
    }


    // ────────────────── Screen Behavior ──────────────────

//    @Override
//    public boolean shouldPause() {
//        return false;
//    }

//    @Override
//    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
//        //Empty disables blur
//        super.renderBackground(context, mouseX, mouseY, delta);
//    }

}
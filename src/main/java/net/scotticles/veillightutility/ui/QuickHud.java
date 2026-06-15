package net.scotticles.veillightutility.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.scotticles.veillightutility.event.KeyInputHandler;
import net.scotticles.veillightutility.light.LightManager;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public class QuickHud extends Screen {

    // ────────────────── Layout Constants ──────────────────
    private static final int buttonWidth = 50;
    private static final int buttonHeight = 20;
    private static final int smallButtonWidth = 20;
    private static final int timeButtonWidth = 30;
    private static final int spacing = 5;
    private static final int startY = 80;
    private static final int labelSpacing = 20;

    private static final int[] removeRanges = {3, 5, 10, 15};

    int removeLightButtonsLabelY;

    // ────────────────── UI States () ──────────────────
    private boolean previousHudState;
    private final int holdKeyCode;

    private ButtonWidget addAreaLightButton;
    private ButtonWidget addDirectionalLightButton;
    private ButtonWidget setTimeMidnightButton;
//    private ButtonWidget setTimeNoonButton;

    public QuickHud(int holdKeyCode) {
        //Screen Title
        super(Text.literal("VLU Quick HUD"));
        this.holdKeyCode = holdKeyCode;
    }

    // ────────────────── Screen Init (Runs On Screen Being Opened) ──────────────────

    @Override
    protected void init() {
        super.init();

        previousHudState = client.options.hudHidden;
        client.options.hudHidden = true;

        int totalAddLightButtonsWidth = buttonWidth * 2 + spacing;
        int startX = (width - totalAddLightButtonsWidth) / 2;

        addAddLightButtons(startX, startY, totalAddLightButtonsWidth);

        removeLightButtonsLabelY = addDirectionalLightButton.getY() + (buttonHeight + labelSpacing);

        addRemoveLightButtons(startX, removeLightButtonsLabelY, totalAddLightButtonsWidth);

//        addTimeButtons(width - timeButtonWidth - spacing * 2, height - buttonHeight - spacing * 2);
    }

    // ────────────────── Button Groups ──────────────────

    private void addAddLightButtons(int x, int y, int totalWidth) {

        //Area Light Button
        addAreaLightButton = addButton("Area", x, y, buttonWidth, () -> {
            spawnAreaLight();
            notifyAndClose("Area Light Added");
        });

        addButton("Point", x + buttonWidth + spacing, y, buttonWidth, () -> {
            spawnPointLight();
            notifyAndClose("Point Light Added");
        });

        addDirectionalLightButton = addButton("Directional", x, y + buttonHeight + spacing, totalWidth, () -> {
            spawnDirectionalLight();
            notifyAndClose("Directional Light Added");
        });
    }

    private void addRemoveLightButtons(int x, int y, int totalWidth) {

        int removeLightButtonsY = y + labelSpacing;

        int total = smallButtonWidth * removeRanges.length + spacing * (removeRanges.length - 1);
        int startX = (width - total) / 2;

        for  (int i = 0; i < removeRanges.length; i++) {
            int range = removeRanges[i];
            int bx = startX + i * (smallButtonWidth + spacing);

            addButton(String.valueOf((int) range), bx, removeLightButtonsY, smallButtonWidth, () -> {
                ClientPlayerEntity player = client.player;
                if (player != null) {
                    LightManager.removeLightsNearPosition(player.getPos(), range);
                    notifyAndClose("Lights Removed Within " + range + " Blocks");
                }
            });
        }

        addButton("Directional Lights",startX, removeLightButtonsY + buttonHeight + spacing,(smallButtonWidth * 4) + (spacing * 3),  () -> {
           LightManager.removeDirectionalLights();
           notifyAndClose("Directional Lights Removed");
        });

        addButton("All Lights",startX, removeLightButtonsY + (buttonHeight + spacing) * 2,(smallButtonWidth * 4) + (spacing * 3),  () -> {
            LightManager.removeAllLights();
            notifyAndClose("All Lights Removed");
        });
    }

//    private void addTimeButtons(int x, int y) {
//        setTimeMidnightButton = addButton("Night", x, y, timeButtonWidth, () -> {
//            //Set Time To Midnight Here
//            notifyAndClose("Time Set To Midnight");
//        });
//
//        setTimeNoonButton = addButton("Day", x, setTimeMidnightButton.getY() - buttonHeight - spacing, timeButtonWidth, () -> {
//            //Set Time To Noon Here
//            notifyAndClose("Time Set To Noon");
//        });
//    }

    // ────────────────── Helper Functions ──────────────────

    private ButtonWidget addButton(String buttonName, int x, int y, int width, Runnable action) {
        ButtonWidget button = ButtonWidget.builder(Text.literal(buttonName), b -> action.run())
                .dimensions(x, y, width, buttonHeight)
                .build();
        addDrawableChild(button);
        return button;
    }

    private void notifyAndClose(String message) {
        ClientPlayerEntity player = client.player;
        if (player != null) {
            player.sendMessage(Text.literal(message), true);
        }
        KeyInputHandler.quickHudBlock = true;
        close();
    }

    // ────────────────── Light Adders ──────────────────

    private void spawnPointLight() {
        ClientPlayerEntity player = client.player;
        if (player != null) {
            Vec3d playerPos = player.getPos();
            LightManager.addPointLight(
                    1.0f,
                    1.0f, 1.0f, 1.0f,
                    playerPos,
                    15.0f
            );
        }
    }

    private void spawnAreaLight() {
        ClientPlayerEntity player = client.player;
        if (player != null) {
            Vec3d playerPos = player.getPos();

            Vec3d look = player.getRotationVector();
            Quaternionf playerOrientation = new Quaternionf().lookAlong(
                    new Vector3f((float) -look.x, (float) -look.y, (float) -look.z),
                    new Vector3f(0, 1, 0)
            );

            float angleRadians = (float) Math.toRadians(45.0f);

            LightManager.addAreaLight(
                    1.0f, 1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f,
                    playerPos,
                    playerOrientation,
                    angleRadians,
                    15.0f
            );
        }
    }

    private void spawnDirectionalLight() {
        ClientPlayerEntity player = client.player;
        if (player != null) {
            LightManager.addDirectionalLight(
                    1.0f, 1.0f, 1.0f, 0.0f,
                    -1.0f, 0.0f, 1.0f
            );
        }
    }

    // ────────────────── Rendering ──────────────────


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        drawCenteredText(context, "VLU Quick HUD", 20, 0xFFFFFF, true);
        drawCenteredText(context, "Add Light", addAreaLightButton.getY() - labelSpacing, 0xFFFFFF, true);
        drawCenteredText(context, "Remove Lights", removeLightButtonsLabelY, 0xFFFFFF, true);
//        drawButtonCenteredText(context, "Set Time", setTimeNoonButton, spacing, 0xFFFFFF, true);
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawCenteredText(DrawContext context, String text, int y, int color, boolean shadow) {
        int x = (width - client.textRenderer.getWidth(text)) / 2;
        context.drawText(client.textRenderer, text, x, y, color, shadow);
    }


    private void drawButtonCenteredText(DrawContext context, String text, ButtonWidget anchor, int spacing, int color, boolean shadow) {
        int textwidth = client.textRenderer.getWidth(text);
        int textHeight = client.textRenderer.fontHeight;

        int centerX = anchor.getX() + anchor.getWidth() / 2;
        int textX = centerX - textwidth / 2;
        int textY = anchor.getY() - spacing - textHeight;

        context.drawText(client.textRenderer, text, textX, textY, color, shadow);
    }

    private void drawNonCenteredText(DrawContext context, String text, int x, int y, int color, boolean shadow) {
        context.drawText(client.textRenderer, text, x, y, color, shadow);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        //Empty disables blur
    }

    // ────────────────── Screen Behaviour ──────────────────

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == holdKeyCode) {
            this.close();
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        client.options.hudHidden = previousHudState;
        super.close();
    }
}
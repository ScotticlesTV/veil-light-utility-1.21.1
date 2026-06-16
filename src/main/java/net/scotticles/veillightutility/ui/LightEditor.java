package net.scotticles.veillightutility.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.scotticles.veillightutility.event.KeyInputHandler;

public class LightEditor extends Screen {

    public LightEditor() {
        //Screen Title
        super(Text.literal("VLU Light Editor"));
    }



    // ────────────────── Helper Functions ──────────────────

    private ButtonWidget addButton(String buttonName, int x, int y, int width, int height, Runnable action) {
        ButtonWidget button = ButtonWidget.builder(Text.literal(buttonName), b -> action.run())
                .dimensions(x, y, width, height)
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

    // ──────────────────── Screen Behavior ────────────────────

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        //Empty disables blur
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
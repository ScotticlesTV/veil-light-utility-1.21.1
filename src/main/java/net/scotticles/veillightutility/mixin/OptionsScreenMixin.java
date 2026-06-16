package net.scotticles.veillightutility.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.scotticles.veillightutility.ui.ConfigScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.realms.task.LongRunningTask.setScreen;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "init()V")
    private void addCustomButton(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) {

            int buttonX = this.width / 2 - 100;
            int buttonY = this.height / 4 + 53 + 24 * 4;

            // Create and add the button
            this.addDrawableChild(ButtonWidget.builder(Text.literal("VLU Config"), button -> {
                // Action to execute when the button is clicked
                setScreen(new ConfigScreen());
            }).dimensions(buttonX, buttonY, 200, 20).build());
        }
    }
}
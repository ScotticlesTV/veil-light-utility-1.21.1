package net.scotticles.veillightutility.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.scotticles.veillightutility.ui.QuickHud;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_VEILLIGHTUTILITY = "key.category.veil-light-utility";

    public static final String QUICK_HUD_KEY = "key.veil-light-utility.quick-hud_key";
    //public static final String LIGHT_EDITOR_KEY = "key.veil-light-utility.light-editor_key";



    // KeyBindings

    public static KeyBinding quickHudKey;
    //public static KeyBinding lightEditorKey;



    public static boolean quickHudBlock = false;
    private static boolean quickHudWasDown = false;

    public static void registerKeyInputs() {

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {

            boolean isDown = quickHudKey.isPressed();

            if (isDown && !quickHudWasDown && !quickHudBlock && minecraftClient.currentScreen == null) {
                quickHudBlock = true;
                minecraftClient.setScreen(new QuickHud(quickHudKey.getDefaultKey().getCode()));
            }

            if (!isDown) {
                quickHudBlock = false;
            }

            quickHudWasDown = isDown;


            //if (lightEditorKey.wasPressed()) {
            //    minecraftClient.setScreen(new LightEditor());
            //}

        });
    }

    public static void registerKeybinds() {

        quickHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                QUICK_HUD_KEY,
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT,
                KEY_CATEGORY_VEILLIGHTUTILITY));


        //lightEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        //        LIGHT_EDITOR_KEY,
        //        InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K,
        //        KEY_CATEGORY_VEILLIGHTUTILITY));

        registerKeyInputs();
    }
}

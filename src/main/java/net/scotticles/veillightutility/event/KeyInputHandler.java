package net.scotticles.veillightutility.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.scotticles.veillightutility.light.LightManager;
import net.scotticles.veillightutility.ui.LightEditor;
import net.scotticles.veillightutility.ui.QuickHud;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_VEILLIGHTUTILITY = "key.category.veil-light-utility";

    // ────────────────── NOTE!!! ──────────────────
    // Veil Syncing Features Are Temporarily Disabled To Allow For Compatibility With Other Veil Mods

    // Previously used keys for adding/removing lights are being kept as comments in case in the future
    // you want to make a Quick Keys options for people who want an even quicker options for managing lights


    public static final String QUICK_HUD_KEY = "key.veil-light-utility.quick-hud_key";
    public static final String LIGHT_EDITOR_KEY = "key.veil-light-utility.light-editor_key";

//    public static final String SYNC_LIGHTS_KEY = "key.veil-light-utility.sync_lights_key";

//    public static final String POINT_LIGHT_KEY = "key.veil-light-utility.point_light_key";
//    public static final String AREA_LIGHT_KEY = "key.veil-light-utility.area_light_key";
//    public static final String DIRECTIONAL_LIGHT_KEY = "key.veil-light-utility.directional_light_key";
//    public static final String REMOVE_LIGHT_NEAR_POSITION_KEY = "key.veil-light-utility.remove_near_position_key";


    // ────────────────── Keybindings ──────────────────

    public static KeyBinding quickHudKey;

    public static KeyBinding lightEditorKey;

//    public static KeyBinding syncLightsKey;

//    public static KeyBinding pointLightKey;
//    public static KeyBinding areaLightKey;
//    public static KeyBinding directionalLightKey;
//    public static KeyBinding removeLightNearPositionKey;


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

            if (lightEditorKey.wasPressed()) {
                minecraftClient.setScreen(new LightEditor());
            }

//            if (syncLightsKey.wasPressed()) {
//                LightManager.syncLightsFromVeil();
//                LightManager.saveAllLights();
//                if (minecraftClient.player != null) {
//                    minecraftClient.player.sendMessage(Text.literal("Veil lights synced and saved!"), false);
//                }
//            }

//            if (pointLightKey.wasPressed()) {
//                if (minecraftClient.player != null) {
//                    Vec3d playerPos = minecraftClient.player.getPos();
//                    LightManager.addPointLight(
//                            1.0f,
//                            1.0f, 1.0f, 1.0f,
//                            playerPos,
//                            15.0f
//                    );
//                }
//            }

//            if (areaLightKey.wasPressed()) {
//                if (minecraftClient.player != null) {
//                    Vec3d playerPos = minecraftClient.player.getPos();
//
//                    Vec3d look = minecraftClient.player.getRotationVector();
//                    Quaternionf playerOrientation = new Quaternionf().lookAlong(
//                            new Vector3f((float) -look.x, (float) -look.y, (float) -look.z),
//                            new Vector3f(0, 1, 0)
//                    );
//
//                    float angleRadians = (float) Math.toRadians(45.0f);
//
//                    LightManager.addAreaLight(
//                            1.0f, 1.0f, 1.0f, 1.0f,
//                            1.0f, 1.0f,
//                            playerPos,
//                            playerOrientation,
//                            angleRadians,
//                            15.0f
//                    );
//                }
//            }

//            if (directionalLightKey.wasPressed()) {
//                if (minecraftClient.player != null) {
//                    LightManager.addDirectionalLight(
//                            1.0f, 1.0f, 1.0f, 0.0f,
//                            -1.0f, 0.0f, 1.0f
//                    );
//                }
//            }

//            if (removeLightNearPositionKey.wasPressed()) {
//                if (minecraftClient.player != null) {
//                    Vec3d playerPos = minecraftClient.player.getPos();
//                    LightManager.removeLightsNearPosition(playerPos, 3.0f);
//                }
//            }
        });
    }

    public static void registerKeybinds() {

        quickHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                QUICK_HUD_KEY,
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT,
                KEY_CATEGORY_VEILLIGHTUTILITY));

        lightEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                LIGHT_EDITOR_KEY,
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K,
                KEY_CATEGORY_VEILLIGHTUTILITY));

//        syncLightsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                SYNC_LIGHTS_KEY,
//                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_END,
//                KEY_CATEGORY_VEILLIGHTUTILITY));

//        pointLightKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                POINT_LIGHT_KEY,
//                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_INSERT,
//                KEY_CATEGORY_VEILLIGHTUTILITY));
//
//        areaLightKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                AREA_LIGHT_KEY,
//                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_DELETE,
//                KEY_CATEGORY_VEILLIGHTUTILITY));

//        directionalLightKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                DIRECTIONAL_LIGHT_KEY,
//                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PAGE_DOWN,
//                KEY_CATEGORY_VEILLIGHTUTILITY));

//        removeLightNearPositionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                REMOVE_LIGHT_NEAR_POSITION_KEY,
//                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_HOME,
//                KEY_CATEGORY_VEILLIGHTUTILITY));
        registerKeyInputs();
    }
}

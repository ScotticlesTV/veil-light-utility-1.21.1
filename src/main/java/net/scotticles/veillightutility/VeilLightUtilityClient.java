package net.scotticles.veillightutility;


import net.fabricmc.api.ClientModInitializer;
import net.scotticles.veillightutility.event.KeyInputHandler;
import net.scotticles.veillightutility.light.LightManager;

public class VeilLightUtilityClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LightManager.worldLightsInit();
        KeyInputHandler.registerKeybinds();
    }
}
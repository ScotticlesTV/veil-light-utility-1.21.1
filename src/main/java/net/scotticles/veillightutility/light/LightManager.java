package net.scotticles.veillightutility.light;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import foundry.veil.api.client.color.Color;
import foundry.veil.api.client.registry.LightTypeRegistry;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.AreaLightData;
import foundry.veil.api.client.render.light.data.DirectionalLightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import net.scotticles.veillightutility.light.savedlights.SavedAreaLights;
import net.scotticles.veillightutility.light.savedlights.SavedDirectionalLights;
import net.scotticles.veillightutility.light.savedlights.SavedPointLights;
import net.scotticles.veillightutility.light.savedlights.WorldLightsSave;
import net.scotticles.veillightutility.settings.VeilLightUtilityConfig;
import org.joml.*;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LightManager {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Set<PointLightData> activePointLights = new HashSet<>();
    private static final Set<AreaLightData> activeAreaLights = new HashSet<>();
    private static final Set<DirectionalLightData> activeDirectionalLights = new HashSet<>();


    private static Path getSaveFile() {
        String worldName = "default_world";
        if (MinecraftClient.getInstance().getServer() != null) {
            worldName = MinecraftClient.getInstance().getServer().getSaveProperties().getLevelName();
        }
        return FabricLoader.getInstance().getConfigDir().resolve("veillightutility/worldlights_" + worldName + ".json");
    }

    public static void worldLightsInit() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> loadWorldLights());
        ClientPlayConnectionEvents.DISCONNECT.register((handler,client) ->
        {
            if (VeilLightUtilityConfig.enableTotalSaving) {
                syncLightsFromVeil();
            }

            saveAllLights();
            activePointLights.clear();
            activeAreaLights.clear();
            activeDirectionalLights.clear();
        });
    }

    // ────────────────── Load Lights ──────────────────

    public static void loadWorldLights() {
        Path saveFile = getSaveFile();

        if (!Files.exists(saveFile)) return;
        try (Reader reader = Files.newBufferedReader(saveFile)) {
            WorldLightsSave loaded = GSON.fromJson(reader, WorldLightsSave.class);

            if (loaded == null) return;

            //Clear Active Lights (Why?)
            activePointLights.clear();
            activeAreaLights.clear();
            activeDirectionalLights.clear();

            //Load Point Lights From File
            if (loaded.pointLights != null) {
                for (SavedPointLights saved : loaded.pointLights) {
                    PointLightData pointLightData = saved.toPointLightData();
                    VeilRenderSystem.renderer().getLightRenderer().addLight(pointLightData);
                    activePointLights.add (pointLightData);
                }
            }

            //Load Area Lights From File
            if (loaded.areaLights != null) {
                for (SavedAreaLights saved : loaded.areaLights) {
                    AreaLightData areaLightData = saved.toAreaLightData();
                    VeilRenderSystem.renderer().getLightRenderer().addLight(areaLightData);
                    activeAreaLights.add (areaLightData);
                }

            }

            //Load Directional Lights From File
            if (loaded.directionalLights != null) {
                for (SavedDirectionalLights saved : loaded.directionalLights) {
                    DirectionalLightData directionalLightData = saved.toDirectionalLightData();
                    VeilRenderSystem.renderer().getLightRenderer().addLight(directionalLightData);
                    activeDirectionalLights.add (directionalLightData);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ────────────────── Save Lights ──────────────────

    public static void saveAllLights() {

        WorldLightsSave save = new WorldLightsSave();

        //Save Point Lights To File
        for (PointLightData pointLightData : activePointLights) {
            Color color = pointLightData.getColor();
            Vector3dc pos = pointLightData.getPosition();

            save.pointLights.add(new SavedPointLights(
                    pointLightData.getBrightness(),
                    color.red(),color.green(),color.blue(),
                    pos.x(), pos.y(), pos.z(),
                    pointLightData.getRadius()
            ));
        }

        //Save Area Lights To File
        for (AreaLightData areaLightData : activeAreaLights) {

            Color color = areaLightData.getColor();
            Vector2fc size = areaLightData.getSize();
            Vector3dc pos = areaLightData.getPosition();
            Quaternionfc orientation = areaLightData.getOrientation();

            save.areaLights.add(new SavedAreaLights(
                    areaLightData.getBrightness(),
                    color.red(),color.green(),color.blue(),
                    size.x(),size.y(),
                    pos.x(), pos.y(), pos.z(),
                    orientation.x(), orientation.y(), orientation.z(), orientation.w(),
                    areaLightData.getAngle(),
                    areaLightData.getDistance()
            ));
        }

        //Save Directional Lights To File
        for (DirectionalLightData directionalLightData : activeDirectionalLights) {

            Color color = directionalLightData.getColor();
            Vector3fc direction = directionalLightData.getDirection();

            save.directionalLights.add(new SavedDirectionalLights(
                    direction.x(),direction.y(),direction.z(),
                    color.red(),color.green(),color.blue(),
                    directionalLightData.getBrightness()
            ));
        }

        Path saveFile = getSaveFile();

        try {
            Files.createDirectories(saveFile.getParent());
            try (Writer writer = Files.newBufferedWriter(saveFile)) {
                GSON.toJson(save, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ────────────────── Add Lights ──────────────────

    public static void addPointLight( float brightness,  float r, float g, float b, Vec3d pos, float radius) {

        PointLightData pointLightData = new PointLightData()
                .setBrightness(brightness)
                .setColor(r, g, b)
                .setPosition(new Vector3d(pos.getX(),pos.getY(),pos.getZ()))
                .setRadius(radius);

        VeilRenderSystem.renderer().getLightRenderer().addLight(pointLightData);
        activePointLights.add(pointLightData);
    }

    public static void addAreaLight(float brightness, float r, float g, float b, float width, float height, Vec3d pos, Quaternionfc orientation, float angle, float distance) {

        AreaLightData areaLightData = new AreaLightData()
                .setBrightness(brightness)
                .setColor(r, g, b)
                .setSize(width, height)
                .setAngle(angle)
                .setDistance(distance);

        areaLightData.getPosition().set(pos.getX(),pos.getY(),pos.getZ());
        areaLightData.getOrientation().set(orientation);

        VeilRenderSystem.renderer().getLightRenderer().addLight(areaLightData);
        activeAreaLights.add(areaLightData);
    }

    public static void addDirectionalLight(float r, float g, float b, float x, float y, float z, float brightness) {

        DirectionalLightData directionalLightData = new DirectionalLightData()
                .setDirection(x ,y ,z)
                .setColor(r, g ,b)
                .setBrightness(brightness);

        VeilRenderSystem.renderer().getLightRenderer().addLight(directionalLightData);
        activeDirectionalLights.add(directionalLightData);
    }

    // ────────────────── Remove Lights Near Position ──────────────────

    public static void removeLightsNearPosition (Vec3d pos, double radius) {
        //        syncLightsFromVeil();

        boolean removed = false;

        //Remove Point Lights
        removed |= activePointLights.removeIf(pointLightData -> {
            Vec3d lightPos = new Vec3d(pointLightData.getPosition().x(), pointLightData.getPosition().y(), pointLightData.getPosition().z());
            return lightPos.distanceTo(pos) <= radius;
        });

        //Remove Area Lights
        removed |= activeAreaLights.removeIf(areaLightData -> {
            Vec3d lightPos = new Vec3d(areaLightData.getPosition().x(), areaLightData.getPosition().y(), areaLightData.getPosition().z());
            return lightPos.distanceTo(pos) <= radius;
        });

        //Re-add Lights
        if (removed) {
            var renderer = VeilRenderSystem.renderer().getLightRenderer();
            renderer.free();

            for (PointLightData pointLightData : activePointLights) {
                renderer.addLight(pointLightData);
            }

            for (AreaLightData areaLightData : activeAreaLights) {
                renderer.addLight(areaLightData);
            }

            for (DirectionalLightData directionalLightData : activeDirectionalLights) {
                renderer.addLight(directionalLightData);
            }
        }
    }

    // ────────────────── Remove Directional Lights ──────────────────

    public static void removeDirectionalLights () {
        boolean removed = false;

        //Remove Directional Lights
        for (DirectionalLightData directionalLightData : activeDirectionalLights) {
            activeDirectionalLights.remove(directionalLightData);
        }

        var renderer = VeilRenderSystem.renderer().getLightRenderer();
        renderer.free();

        for (PointLightData pointLightData : activePointLights) {
            renderer.addLight(pointLightData);
        }

        for (AreaLightData areaLightData : activeAreaLights) {
            renderer.addLight(areaLightData);
        }

        for (DirectionalLightData directionalLightData : activeDirectionalLights) {
            renderer.addLight(directionalLightData);
        }
    }

    // ────────────────── Remove All Lights ──────────────────

    public static void removeAllLights() {

        //Remove Point Lights
            activePointLights.clear();

        //Remove Area Lights
            activeAreaLights.clear();

        //Remove Directional Lights
            activeDirectionalLights.clear();

        VeilRenderSystem.renderer().getLightRenderer().free();

    }

    // ────────────────── Sync Light From Veil Renderer ──────────────────

    public static void syncLightsFromVeil() {
        var lightRenderer = VeilRenderSystem.renderer().getLightRenderer();
        if (lightRenderer == null) return;

        // Clear existing tracked lights
        activePointLights.clear();
        activeAreaLights.clear();
        activeDirectionalLights.clear();

        // Add all current lights from Veil
        for (var handle : lightRenderer.getLights(LightTypeRegistry.POINT.get())) {
            activePointLights.add(handle.getLightData());
        }
        for (var handle : lightRenderer.getLights(LightTypeRegistry.AREA.get())) {
            activeAreaLights.add(handle.getLightData());
        }
        for (var handle : lightRenderer.getLights(LightTypeRegistry.DIRECTIONAL.get())) {
            activeDirectionalLights.add(handle.getLightData());
        }
    }
}
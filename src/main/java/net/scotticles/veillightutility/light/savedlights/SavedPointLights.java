package net.scotticles.veillightutility.light.savedlights;

import foundry.veil.api.client.color.Color;
import foundry.veil.api.client.render.light.data.PointLightData;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.awt.*;

public class SavedPointLights {
    public float brightness;
    public float r, g ,b;
    public double x, y ,z;
    public float radius;
    public boolean occluded;

    public SavedPointLights() {}

    public SavedPointLights(PointLightData pointLightData ) {
        this.brightness = pointLightData.getBrightness();

        Color color = pointLightData.getColor();
        this.r = color.red();
        this.g = color.green();
        this.b = color.blue();

        Vector3dc pos = pointLightData.getPosition();
        this.x = pos.x();
        this.y = pos.y();
        this.z = pos.z();

        this.radius = pointLightData.getRadius();

        this.occluded = pointLightData.isOcclusionEnabled();


    }

    public SavedPointLights( float brightness, float r, float g, float b, double x, double y, double z, float radius, boolean occluded ) {
        this.brightness = brightness;

        this.r = r;
        this.g = g;
        this.b = b;

        this.x = x;
        this.y = y;
        this.z = z;

        this.radius = radius;

        this.occluded = occluded;
    }

    public PointLightData toPointLightData() {
        return new PointLightData()
                .setBrightness(brightness)
                .setColor(r, g ,b)
                .setPosition(new Vector3d(x, y ,z))
                .setRadius(radius)
                .setOcclusionEnabled(occluded);

    }
}

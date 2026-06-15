package net.scotticles.veillightutility.light.savedlights;

import foundry.veil.api.client.color.Color;
import foundry.veil.api.client.render.light.data.DirectionalLightData;
import org.joml.Vector3fc;

public class SavedDirectionalLights {
    public float brightness;
    public float r, g ,b;
    public float x, y ,z;



    public SavedDirectionalLights() {}

    public SavedDirectionalLights(DirectionalLightData directionalLightData) {

        Color color = directionalLightData.getColor();
        this.r = color.red();
        this.g = color.green();
        this.b = color.blue();

        Vector3fc direction = directionalLightData.getDirection();


        this.x = direction.x();
        this.y = direction.y();
        this.z = direction.z();

        this.brightness = directionalLightData.getBrightness();



    }

    public SavedDirectionalLights(float x, float y, float z, float r, float g, float b, float brightness) {

        this.brightness = brightness;

        this.r = r;
        this.g = g;
        this.b = b;

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public DirectionalLightData toDirectionalLightData() {

        return (DirectionalLightData) new DirectionalLightData()
                .setColor(r, g ,b)
                .setDirection(x, y, z)
                .setBrightness(brightness);
    }
}

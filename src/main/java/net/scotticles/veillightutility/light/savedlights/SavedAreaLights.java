package net.scotticles.veillightutility.light.savedlights;

import foundry.veil.api.client.color.Color;
import foundry.veil.api.client.render.light.data.AreaLightData;
import org.joml.*;

public class SavedAreaLights {
    //Needed Attributes
    //Brightness
    //Color
    //Size
    //Position
    //Angle
    //Distance
    //Orientation
    //Occluded
    public float brightness;
    public float r, g ,b;
    public float width, height;
    public double x, y ,z;
    public float angle;
    public float distance;
    public float o1, o2, o3, o4;
    public boolean occluded;



    public SavedAreaLights() {}

    public SavedAreaLights(AreaLightData areaLightData) {

        this.brightness = areaLightData.getBrightness();

        Color color = areaLightData.getColor();
        this.r = color.red();
        this.g = color.green();
        this.b = color.blue();

        Vector3dc pos = areaLightData.getPosition();
        this.x = pos.x();
        this.y = pos.y();
        this.z = pos.z();

        Quaternionfc orientation = areaLightData.getOrientation();

        this.o1 = orientation.x();
        this.o2 = orientation.y();
        this.o3 = orientation.z();
        this.o4 = orientation.w();

        Vector2fc size = areaLightData.getSize();
        this.width = size.x();
        this.height = size.y();

        this.angle = areaLightData.getAngle();

        this.distance = areaLightData.getDistance();

        boolean occluded = areaLightData.isOcclusionEnabled();
    }

    public SavedAreaLights(float brightness, float r, float g, float b, float width, float height, double x, double y, double z, float o1, float o2, float o3, float o4, float angle, float distance, boolean occluded) {
        this.brightness = brightness;
        this.r = r;
        this.g = g;
        this.b = b;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.z = z;
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
        this.o4 = o4;
        this.angle = angle;
        this.distance = distance;
        this.occluded = occluded;
    }

    public AreaLightData toAreaLightData() {
        AreaLightData areaLightData = new AreaLightData()
                .setBrightness(brightness)
                .setColor(r, g, b)
                .setSize(width, height)
                .setAngle(angle)
                .setDistance(distance)
                .setOcclusionEnabled(occluded);

                areaLightData.getPosition().set(x, y, z);
                areaLightData.getOrientation().set(new Quaternionf(o1, o2 ,o3, o4).normalize());

                return areaLightData;
    }


}

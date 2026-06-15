package net.scotticles.veillightutility.settings;

import net.fabricmc.loader.api.FabricLoader;
import net.scotticles.veillightutility.VeilLightUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class VeilLightUtilityConfig {
    // Config variables
    public static boolean enableTotalSaving = false;

    // Config File Reference Inside Fabric Config File
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), VeilLightUtility.MOD_ID + ".properties");

    public static void load() {
        Properties properties = new Properties();

        if (CONFIG_FILE.exists()) {
            try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
                properties.load(in);

                // Assign config values from file and set them to defaults if they don't exist
                enableTotalSaving = Boolean.parseBoolean(properties.getProperty("enableTotalSaving", "false"));

            } catch (IOException | NumberFormatException e) {
                System.err.println(VeilLightUtility.MOD_ID + " Failed to load config file, using defaults.");
                e.printStackTrace();
            }
        } else {
            // Generate a fresh config file with defaults if it doesn't exist
            save();
        }
    }

    public static void save() {
        Properties properties = new Properties();

        // Put variables into properties object
        properties.setProperty("enableTotalSaving", String.valueOf(enableTotalSaving));

        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            properties.store(out, "VLU Config Settings");
        } catch (IOException e) {
            System.err.println(VeilLightUtility.MOD_ID + " Failed to save config file.");
            e.printStackTrace();
        }
    }
}

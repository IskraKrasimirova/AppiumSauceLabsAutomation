package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.AppiumSettings;

import java.io.File;

public class ConfigReader {
    private static AppiumSettings settings;

    public  static AppiumSettings getSettings() {
        if (settings == null) {
            loadSettings();
        }

        return settings;
    }

    private static void loadSettings() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            settings = mapper.readValue(new File("src/main/resources/appsettings.json"), AppiumSettings.class);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load appsettings.json", e);
        }
    }
}

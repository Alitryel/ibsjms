package ru.alitryel.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    public static Properties loadProperties(String filename) throws IOException {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(filename)) {
            Properties prop = new Properties();
            if (input == null) {
                throw new IOException("Unable to find " + filename);
            }
            prop.load(input);
            return prop;
        }
    }
}

package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties = new Properties();

    private ConfigReader() {
        loadProperties();
    }

    private void loadProperties() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties");
        if (in == null) {
            in = getClass().getClassLoader().getResourceAsStream("config.properties.example");
        }
        if (in != null) {
            try {
                properties.load(in);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load configuration", e);
            }
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getBaseUrl() {
        return resolve("base.url", "BASE_URL", "https://www.saucedemo.com");
    }

    public String getBrowser() {
        return resolve("browser", "BROWSER", "chrome");
    }

    public String getUsername() {
        return resolve("test.username", "TEST_USERNAME", "standard_user");
    }

    public String getPassword() {
        return resolve("test.password", "TEST_PASSWORD", "secret_sauce");
    }

    public int getImplicitWaitSeconds() {
        return Integer.parseInt(resolve("implicit.wait.seconds", null, "0"));
    }

    /** Priority: system property > env var > properties file > default. */
    private String resolve(String propKey, String envKey, String defaultValue) {
        String sysProp = System.getProperty(propKey);
        if (sysProp != null && !sysProp.isEmpty()) return sysProp;

        if (envKey != null) {
            String envVal = System.getenv(envKey);
            if (envVal != null && !envVal.isEmpty()) return envVal;
        }

        String fileProp = properties.getProperty(propKey);
        if (fileProp != null && !fileProp.isEmpty()) return fileProp;

        return defaultValue;
    }
}

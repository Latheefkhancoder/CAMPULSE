package com.smartcampus.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/smart_campus";

    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "Latheef@1212";

    private DBConnection() {
        // Utility class; prevent instantiation.
    }

    public static Connection getConnection() throws SQLException {

        String url = getSetting("SMART_CAMPUS_DB_URL", DEFAULT_URL);
        String user = getSetting("SMART_CAMPUS_DB_USER", DEFAULT_USER);
        String password = getSetting(
                "SMART_CAMPUS_DB_PASSWORD", DEFAULT_PASSWORD);

        return DriverManager.getConnection(url, user, password);
    }

    private static String getSetting(String key, String defaultValue) {

        // 1. Check Java system properties.
        String value = System.getProperty(key);

        if (value != null && !value.isBlank()) {
            return value.trim();
        }

        // 2. Check environment variables.
        value = System.getenv(key);

        if (value != null && !value.isBlank()) {
            return value.trim();
        }

        // 3. Check db.properties in the working directory.
        Path configPath = Path.of(
                System.getProperty("user.dir"), "db.properties");

        if (Files.exists(configPath)) {
            Properties properties = new Properties();

            try (InputStream inputStream =
                         Files.newInputStream(configPath)) {

                properties.load(inputStream);

                value = properties.getProperty(key);

                if (value != null && !value.isBlank()) {
                    return value.trim();
                }

            } catch (IOException e) {
                throw new IllegalStateException(
                        "Failed to read database configuration file: "
                                + configPath,
                        e);
            }
        }

        // 4. Use the default value.
        return defaultValue;
    }
}
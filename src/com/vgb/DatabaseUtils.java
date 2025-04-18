package com.vgb;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseUtils {
    private static final String PROPS_FILE = "/db.properties";
    private static final Properties props = new Properties();

    static {
        try (InputStream in = DatabaseUtils.class.getResourceAsStream(PROPS_FILE)) {
            props.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new ExceptionInInitializerError("DB init failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws Exception {
        String url  = props.getProperty("jdbc.url");
        String user = props.getProperty("jdbc.user");
        String pass = props.getProperty("jdbc.pass");
        return DriverManager.getConnection(url, user, pass);
    }
}
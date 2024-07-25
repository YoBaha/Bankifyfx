package models;

import java.util.Properties;

public class EmailConfig {
    public static final String USERNAME = "Noxus.Baha@outlook.com";
    public static final String PASSWORD = "sexymotherfucker789z";

    public static Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp-mail.outlook.com");
        properties.put("mail.smtp.port", "587");
        return properties;
    }
}

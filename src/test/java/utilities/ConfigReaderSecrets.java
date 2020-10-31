package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReaderSecrets {

    // Bu Classin amaci ==> configuration.properties dosyasindaki verilerini okumaktir.
    private static Properties properties;

    static {
        String path = "configuration_secrets.properties";
        try {
            FileInputStream file = new FileInputStream(path);
            properties =  new Properties();
            properties.load(file);
        } catch (IOException e) {
            System.out.println("Configuration secrets file bulunamadi");
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }


}

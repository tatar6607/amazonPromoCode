package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    // Bu Classin amaci ==> configuration.properties dosyasindaki verilerini okumaktir.
    private static Properties properties;

    static {
        String path = "configuration.properties";
        try {
            FileInputStream file = new FileInputStream(path);
            properties =  new Properties();
            properties.load(file);
        } catch (IOException e) {
            System.out.println("Configuration file bulunamadi");
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }


}

package app.utils;

import app.exceptions.ApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
//gør bla. at vi kan bruge vores api-key klad
// læser vores config.properties fil med path til db
public class Utils {

    public static String getPropertyValue(String propName, String resourceName)  {
        try (InputStream is = Utils.class.getClassLoader().getResourceAsStream(resourceName)) {
            Properties prop = new Properties();
            prop.load(is);

            String value = prop.getProperty(propName);
            if (value != null) {
                return value.trim();  // Trim whitespace
            } else {
                throw new ApiException(500, String.format("Property %s not found in %s", propName, resourceName));
            }
        } catch (IOException ex) {
            throw new ApiException(500, String.format("Could not read property %s.", propName));
        }
    }
}
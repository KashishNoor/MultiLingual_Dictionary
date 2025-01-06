package co;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	private Properties properties = new Properties();
	String url;
	String user;
	String password;
	String filePath = "config.properties";
	//String filePath = "testconfig.properties";
	public ConfigReader() {
		try {
            InputStream input = getClass().getClassLoader().getResourceAsStream(filePath);
            if (input == null) {
                throw new FileNotFoundException("config.properties file not found in the classpath");
            }
            properties.load(input);
            url = getProperty("database.url");
            user = getProperty("database.user");
            password = getProperty("database.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public String getUrl() {
		return url;
	}
	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}

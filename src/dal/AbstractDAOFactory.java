package dal;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class AbstractDAOFactory implements IDAOFactory {
    private static IDAOFactory instance;

    public static final IDAOFactory getInstance() {
        if (instance == null) {
            String factoryClassName = null;
            try (InputStream input = AbstractDAOFactory.class.getResourceAsStream("/config.properties")) {
                if (input == null) {
                    throw new IOException("Configuration file 'config.properties' not found in classpath.");
                }
                Properties prop = new Properties();
                prop.load(input);
                factoryClassName = prop.getProperty("dal.factory");
                if (factoryClassName == null || factoryClassName.isEmpty()) {
                    throw new IllegalArgumentException("Property 'dal.factory' is not set in config.properties.");
                }
                Class<?> clazz = Class.forName(factoryClassName);
                instance = (IDAOFactory) clazz.getDeclaredConstructor().newInstance();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error loading configuration file.", e);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error instantiating DAO factory.", e);
            }
        }
        return instance;
    }
}

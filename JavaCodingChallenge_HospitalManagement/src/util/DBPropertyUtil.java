package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    // This method reads database properties and returns the connection string
    public static String getConnectionString(String fileName) throws IOException {
        String connStr = null;
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("src/db.properties");
        props.load(fis);

        String user = props.getProperty("user");
        String password = props.getProperty("password");
        String protocol = props.getProperty("protocol");
        String system = props.getProperty("system");
        String database = props.getProperty("database");
        String port = props.getProperty("port");

        connStr = protocol + "//" + system + ":" + port + "/" + database + "?user=" + user + "&password=" + password;
        return connStr;
    }
}
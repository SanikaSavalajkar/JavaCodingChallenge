package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
	public static String getPropertyString(String fileName) {
		Properties prop = new Properties();
		try (FileInputStream fis = new FileInputStream(fileName)){
			prop.load(fis);
			String host = prop.getProperty("host");
			String port = prop.getProperty("port");
            String dbname = prop.getProperty("dbname");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            return "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?user=" + username + "&password=" + password;
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
}
package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import data.Lane;

public class Prop {

	public static Prop self = new Prop();

	public Properties prop;
	private InputStream input = null;

	public Prop(){
		try {
			prop = new Properties();
			input = new FileInputStream("config.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			System.out.println(prop.getProperty("p1.ID"));			

		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public void close(){
		if (input != null) {
			try {
				input.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}
}	


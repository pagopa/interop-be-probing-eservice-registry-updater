
package it.pagopa.interop.probing.eservice.registry.updater.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesLoader {

	private static PropertiesLoader instance;

	private Properties props;

	public static final String PROPERTIES = "application.properties";

	public PropertiesLoader() throws IOException {
		InputStream inputStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(PROPERTIES);
		this.props = new Properties();
		try {
			this.props.load(inputStream);
		} catch (IOException e) {
			log.error("Error during reading properties from file");
			throw e;
		}
		log.info("Properties loaded successfully");
	}

	public String getKey(String key) {
		return this.props.getProperty(key);
	}

	static public PropertiesLoader getInstance() throws IOException {
		if (Objects.isNull(instance)) {
			synchronized (PropertiesLoader.class) {
				instance = new PropertiesLoader();
			}
		}
		return instance;
	}

}

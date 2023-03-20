
package it.pagopa.interop.probing.eservice.registry.updater.config;

import java.io.IOException;
import java.util.Objects;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesLoader {

	private static PropertiesLoader instance;

	private Config props;

	public PropertiesLoader() throws IOException {
		this.props = ConfigFactory.load();
		log.info("Properties loaded successfully");
	}

	public String getKey(String key) {
		return this.props.getString(key);
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

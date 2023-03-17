/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 7 mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.config
* File Name   : PropertiesLoader.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.config;

import java.io.IOException;
import java.util.Objects;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
public class PropertiesLoader {

	/** The instance. */
	private static PropertiesLoader instance;

	/** The props. */
	private Config props;

	/**
	 * Instantiates a new properties loader.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public PropertiesLoader() throws IOException {
		this.props = ConfigFactory.load();
		log.info("Properties loaded successfully");
	}

	/**
	 * Gets the key.
	 *
	 * @param key the key
	 * @return the key
	 */
	public String getKey(String key) {
		return this.props.getString(key);
	}

	/**
	 * Gets the single instance of PropertiesLoader.
	 *
	 * @return single instance of PropertiesLoader
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	static public PropertiesLoader getInstance() throws IOException {
		if (Objects.isNull(instance)) {
			synchronized (PropertiesLoader.class) {
				instance = new PropertiesLoader();
			}
		}
		return instance;
	}

}

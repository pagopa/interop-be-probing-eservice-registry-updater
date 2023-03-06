/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 6 Mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.util
* File Name   : EserviceTechnology.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The Enum EserviceType.
 */
public enum EserviceTechnology {

	/** The rest. */
	REST("REST"),

	/** The soap. */
	SOAP("SOAP");

	/** The value. */
	private String value;

	/**
	 * Instantiates a new eservice type.
	 *
	 * @param value the value
	 */
	EserviceTechnology(String value) {
		this.value = value;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	@JsonValue
	public String getValue() {
		return value;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return String.valueOf(value);
	}

	/**
	 * From value.
	 *
	 * @param value the value
	 * @return the eservice type
	 */
	@JsonCreator
	public static EserviceTechnology fromValue(String value) {
		for (EserviceTechnology b : EserviceTechnology.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}
}

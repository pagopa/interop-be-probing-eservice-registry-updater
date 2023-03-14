/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 7 mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.util
* File Name   : EserviceState.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/

package it.pagopa.interop.probing.eservice.registry.updater.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The Enum EserviceState.
 */
public enum EserviceState {

	/** The active. */
	ACTIVE("ACTIVE"),

	/** The inactive. */
	INACTIVE("INACTIVE");

	/** The value. */
	private String value;

	/**
	 * Instantiates a new eservice state.
	 *
	 * @param value the value
	 */
	EserviceState(String value) {
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
	 * @return the eservice state
	 */
	@JsonCreator
	public static EserviceState fromValue(String value) {
		for (EserviceState b : EserviceState.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}
}

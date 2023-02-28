/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 28 feb 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.util
* File Name   : EServiceState.java
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
 * The Enum EServiceState.
 */
public enum EServiceState {

	/** The active. */
	ACTIVE("ACTIVE"),

	/** The inactive. */
	INACTIVE("INACTIVE");

	/** The value. */
	private String value;

	/**
	 * Instantiates a new e service state.
	 *
	 * @param value the value
	 */
	EServiceState(String value) {
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
	 * @return the e service state
	 */
	@JsonCreator
	public static EServiceState fromValue(String value) {
		for (EServiceState b : EServiceState.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}
}

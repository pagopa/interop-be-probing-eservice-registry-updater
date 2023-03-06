/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 6 Mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater
* File Name   : InteropProbingApplication.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater;

import java.io.IOException;

import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;

public class InteropProbingApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		ServicesReceiver.getInstance().receiveStringMessage();
	}
}

/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 3 mar 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater
* File Name   : InteropProbingApplication.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater;

import java.io.IOException;

import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;

/**
 * The Class InteropProbingApplication.
 */
public class InteropProbingApplication {

	public static void main(String[] args) throws IOException {
		ServicesReceiver.getInstance().receiveStringMessage();
	}
}

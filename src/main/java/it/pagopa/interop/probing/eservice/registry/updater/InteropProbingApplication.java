/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 28 feb 2023
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

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;

/**
 * The Class InteropProbingApplication.
 */
public class InteropProbingApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		Weld weld = new Weld();
		WeldContainer container = weld.initialize();
		ServicesReceiver queue = container.select(ServicesReceiver.class).get();
		queue.receiveStringMessage();
		weld.shutdown();
	}

}

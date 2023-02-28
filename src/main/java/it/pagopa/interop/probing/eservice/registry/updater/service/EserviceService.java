/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 28 feb 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.service
* File Name   : EserviceService.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.service;

import java.io.IOException;

import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;

/**
 * The Interface EserviceService.
 */
public interface EserviceService {
		
		/**
		 * Save service.
		 *
		 * @param eservice the eservice
		 * @return the long
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		Long saveService(EserviceDTO eservice) throws IOException;
}

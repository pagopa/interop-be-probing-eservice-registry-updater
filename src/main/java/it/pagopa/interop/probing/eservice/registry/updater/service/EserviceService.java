/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 3 mar 2023
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
import java.util.UUID;

import javax.transaction.Transactional;

import it.pagopa.interop.probing.eservice.registry.updater.dao.EserviceDao;
import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.model.Eservice;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceTechnology;

/**
 * The Class EserviceServiceImpl.
 */
@Transactional
public class EserviceService {

	/** The instance. */
	private static EserviceService instance;

	/**
	 * Gets the single instance of BucketService.
	 *
	 * @return single instance of BucketService
	 */
	public static EserviceService getInstance() {
		if (instance == null) {
			instance = new EserviceService();
		}
		return instance;
	}

	/**
	 * Save service.
	 *
	 * @param eserviceNew the eservice new
	 * @return the long
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Long saveService(EserviceDTO eserviceNew) throws IOException {

		UUID eserviceId = UUID.fromString(eserviceNew.getEserviceId());
		UUID versionId = UUID.fromString(eserviceNew.getVersionId());

		Eservice eservice = EserviceDao.getInstance().findByEserviceIdAndVersionId(eserviceId, versionId);
		if (eservice == null) {
			eservice = new Eservice();
			eservice.setVersionId(versionId);
			eservice.setEserviceId(eserviceId);
		}
		eservice.setEserviceName(eserviceNew.getName());
		eservice.setState(EserviceState.valueOf(eserviceNew.getState()));
		eservice.setProducerName(eserviceNew.getProducerName());
		eservice.setTechnology(EserviceTechnology.valueOf(eserviceNew.getTechnology()));
		eservice.setBasePath(eserviceNew.getBasePath());

		return EserviceDao.getInstance().save(eservice);
	}

}

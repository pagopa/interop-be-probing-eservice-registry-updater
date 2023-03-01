/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 28 feb 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.service
* File Name   : EserviceServiceImpl.java
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

import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.model.Eservice;
import it.pagopa.interop.probing.eservice.registry.updater.repository.EserviceRepository;
import it.pagopa.interop.probing.eservice.registry.updater.util.EServiceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceType;
import jakarta.inject.Inject;

/**
 * The Class EserviceServiceImpl.
 */
@Transactional
public class EserviceServiceImpl implements EserviceService {

	@Inject
	EserviceRepository repo;

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

		Eservice eservice = repo.findByEserviceIdAndVersionId(eserviceId, versionId);
		if (eservice == null) {
			eservice = new Eservice();
			eservice.setVersionId(eserviceId);
			eservice.setEserviceId(versionId);
		}

		eservice.setEserviceName(eserviceNew.getName());
		eservice.setState(EServiceState.valueOf(eserviceNew.getState()));
		eservice.setProducerName(eserviceNew.getProducerName());
		eservice.setEserviceType(EserviceType.valueOf(eserviceNew.getType()));
		eservice.setBasePath(eserviceNew.getBasePath());

		return repo.save(eservice);
	}
	
	public Long saveService() throws IOException {
		
		

//		UUID eserviceId = UUID.fromString(eserviceNew.getEserviceId());
//		UUID versionId = UUID.fromString(eserviceNew.getVersionId());
//
//		Eservice eservice = repo.findByEserviceIdAndVersionId(eserviceId, versionId);
		Eservice eservice = new Eservice();
//		if (eservice == null) {
//			eservice = new Eservice();
//			eservice.setVersionId(eserviceId);
//			eservice.setEserviceId(versionId);
//		}
//
//		eservice.setEserviceName(eserviceNew.getName());
//		eservice.setState(EServiceState.valueOf(eserviceNew.getState()));
//		eservice.setProducerName(eserviceNew.getProducerName());
//		eservice.setEserviceType(EserviceType.valueOf(eserviceNew.getType()));
//		eservice.setBasePath(eserviceNew.getBasePath());

		return repo.save(eservice);
	}
}

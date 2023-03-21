
package it.pagopa.interop.probing.eservice.registry.updater.service;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import it.pagopa.interop.probing.eservice.registry.updater.dao.EserviceDao;
import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.model.Eservice;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceTechnology;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class EserviceService {

	private static EserviceService instance;

	public static EserviceService getInstance() {
		if (Objects.isNull(instance)) {
			instance = new EserviceService();
		}
		return instance;
	}

	public Long saveService(EserviceDTO eserviceNew) throws IOException {

		UUID eserviceId = UUID.fromString(eserviceNew.getEserviceId());
		UUID versionId = UUID.fromString(eserviceNew.getVersionId());
		log.info("saveService FIND");
		Eservice eservice = EserviceDao.getInstance().findByEserviceIdAndVersionId(eserviceId, versionId);
		if (Objects.isNull(eservice)) {
			eservice = new Eservice();
			eservice.setVersionId(versionId);
			eservice.setEserviceId(eserviceId);
		}
		eservice.setEserviceName(eserviceNew.getName());
		eservice.setState(EserviceState.valueOf(eserviceNew.getState()));
		eservice.setProducerName(eserviceNew.getProducerName());
		eservice.setTechnology(EserviceTechnology.valueOf(eserviceNew.getTechnology()));
		eservice.setBasePath(eserviceNew.getBasePath());
		log.info("saveService SAVE");
		return EserviceDao.getInstance().save(eservice);
	}

}

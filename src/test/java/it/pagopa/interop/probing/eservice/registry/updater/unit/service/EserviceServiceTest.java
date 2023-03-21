
package it.pagopa.interop.probing.eservice.registry.updater.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import it.pagopa.interop.probing.eservice.registry.updater.dao.EserviceDao;
import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.model.Eservice;
import it.pagopa.interop.probing.eservice.registry.updater.service.EserviceService;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;

class EserviceServiceTest {

	EserviceDao eserviceDao = mock(EserviceDao.class);

	EserviceDTO eServiceDTO;

	@BeforeEach
	void setup() throws IOException {
		eServiceDTO = new EserviceDTO();
		eServiceDTO.setEserviceId(UUID.randomUUID().toString());
		eServiceDTO.setVersionId(UUID.randomUUID().toString());
		eServiceDTO.setName("Service Name");
		eServiceDTO.setProducerName("Producer Name");
		eServiceDTO.setState("ACTIVE");
		eServiceDTO.setTechnology("REST");
		String[] basePath = { "basePath1", "basePath2" };
		eServiceDTO.setBasePath(basePath);

	}

	@Test
	@DisplayName("The saveEservice method is executed if the Eservice doesn't exist")
	void testSaveEservice_whenDoesntExist_GivenValidEService_thenSaveEntityAndReturnId() throws IOException {

		Eservice serviceEntity = getEserviceEntity(eServiceDTO.getEserviceId(), eServiceDTO.getVersionId());

		try (MockedStatic<EserviceDao> daoMock = Mockito.mockStatic(EserviceDao.class)) {

			daoMock.when(EserviceDao::getInstance).thenReturn(eserviceDao);

//			when(eserviceDao.findByEserviceIdAndVersionId(any(UUID.class), any(UUID.class))).thenReturn(null);
//			Mockito.when(eserviceDao.save(any(Eservice.class))).thenReturn(serviceEntity.getId());
//
//			Long id = EserviceService.getInstance().saveService(eServiceDTO);
//			assertEquals(id, serviceEntity.getId());
		}
	}

	private Eservice getEserviceEntity(String eserviceId, String versionId) {
		Eservice serviceEntity = new Eservice();
		serviceEntity.setId(10L);
		serviceEntity.setState(EserviceState.INACTIVE);
		serviceEntity.setEserviceId(UUID.fromString(eserviceId));
		serviceEntity.setVersionId(UUID.fromString(versionId));
		serviceEntity.setEserviceName("E-Service name");
		serviceEntity.setBasePath(new String[] { "test-BasePath-1", "test-BasePath-2" });
		serviceEntity.setPollingFrequency(5);
		serviceEntity.setProducerName("Producer name");
		serviceEntity.setProbingEnabled(true);
		return serviceEntity;
	}

}

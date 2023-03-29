
package it.pagopa.interop.probing.eservice.registry.updater.unit.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceTechnology;

@ExtendWith(MockitoExtension.class)
class EserviceDTOTest {

	private EserviceDTO eServiceDTO;

	@BeforeEach
	void setup() throws IOException {
		eServiceDTO = new EserviceDTO();
		eServiceDTO.setEserviceId("0b37ac73-cbd8-47f1-a14c-19bcc8f8f8e7");
		eServiceDTO.setVersionId("226574b8-82a1-4844-9484-55fffc9c15ef");
		eServiceDTO.setName("Service Name");
		eServiceDTO.setProducerName("Producer Name");
		eServiceDTO.setState(EserviceState.ONLINE.toString());
		eServiceDTO.setTechnology(EserviceTechnology.REST.toString());
		String[] basePath = { "basePath1", "basePath2" };
		eServiceDTO.setBasePath(basePath);

	}

	@Test
	@DisplayName("Test the utility toString of lombok.")
	void testToString_whenGivenValidEServiceDto_thenValidEquals() throws IOException {
		String serviceString = "EserviceDTO(name=Service Name, eserviceId=0b37ac73-cbd8-47f1-a14c-19bcc8f8f8e7, versionId=226574b8-82a1-4844-9484-55fffc9c15ef, technology=REST, state=ACTIVE, basePath=[basePath1, basePath2], producerName=Producer Name)";
		assertEquals(eServiceDTO.toString(), serviceString);
	}

	@Test
	@DisplayName("Test the utility Equals and HashCode of lombok.")
	void testEqualsHashCode_whenGivenValidEServiceDto_thenValidEquals() throws IOException {
		EserviceDTO copy = new EserviceDTO();
		copy.setEserviceId("0b37ac73-cbd8-47f1-a14c-19bcc8f8f8e7");
		copy.setVersionId("226574b8-82a1-4844-9484-55fffc9c15ef");
		copy.setName("Service Name");
		copy.setProducerName("Producer Name");
		copy.setState("ACTIVE");
		copy.setTechnology("REST");
		String[] basePath = { "basePath1", "basePath2" };
		copy.setBasePath(basePath);
		assertEquals(true, eServiceDTO.equals(copy));
		assertEquals(true, eServiceDTO.hashCode() == copy.hashCode());
	}
}

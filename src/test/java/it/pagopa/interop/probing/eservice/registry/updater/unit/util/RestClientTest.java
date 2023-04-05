package it.pagopa.interop.probing.eservice.registry.updater.unit.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceTechnology;
import it.pagopa.interop.probing.eservice.registry.updater.util.RestClient;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

class RestClientTest {

	Client client = mock(Client.class);
	WebTarget webTarget = mock(WebTarget.class);
	Invocation.Builder invocationBuilder = mock(Invocation.Builder.class);
	ClientBuilder clientBuilder = mock(ClientBuilder.class);
	Response resp = mock(Response.class);

	private EserviceDTO eServiceDTO;

	@BeforeEach
	void setup() throws IOException {
		String[] basePath = { "basePath1", "basePath2" };
		eServiceDTO = EserviceDTO.builder().eserviceId(UUID.fromString("0b37ac73-cbd8-47f1-a14c-19bcc8f8f8e7"))
				.versionId(UUID.fromString("226574b8-82a1-4844-9484-55fffc9c15ef")).name("Service Name")
				.producerName("Producer Name").state(EserviceState.ACTIVE).technology(EserviceTechnology.REST)
				.basePath(basePath).versionNumber(1).build();
	}

	@Test
	@DisplayName("The method call api rest and saves to db")
	void testRestClient_whenReadMessage_thenCallRest200() throws IOException {

		try (MockedStatic<ClientBuilder> clientBuilderMock = mockStatic(ClientBuilder.class)) {
			clientBuilderMock.when(ClientBuilder::newClient).thenReturn(client);

			Mockito.when(client.target(Mockito.anyString())).thenReturn(webTarget);
			Mockito.when(webTarget.path(Mockito.anyString())).thenReturn(webTarget);
			Mockito.when(webTarget.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilder);

			Mockito.when(invocationBuilder.post(Mockito.any())).thenReturn(resp);
			Mockito.when(resp.getStatus()).thenReturn(200);
			RestClient.getInstance().saveEservice(eServiceDTO, client);
			verify(resp).readEntity(Long.class);
		}
	}

	@Test
	@DisplayName("The method call api rest and not saves to db")
	void testRestClient_whenReadMessage_thenCallRest500() throws IOException {

		try (MockedStatic<ClientBuilder> clientBuilderMock = mockStatic(ClientBuilder.class)) {
			clientBuilderMock.when(ClientBuilder::newClient).thenReturn(client);

			Mockito.when(client.target(Mockito.anyString())).thenReturn(webTarget);
			Mockito.when(webTarget.path(Mockito.anyString())).thenReturn(webTarget);
			Mockito.when(webTarget.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilder);

			Mockito.when(invocationBuilder.post(Mockito.any())).thenReturn(resp);
			Mockito.when(resp.getStatus()).thenReturn(500);
			RestClient restClient = RestClient.getInstance();
			assertThrows(IOException.class, () -> restClient.saveEservice(eServiceDTO, client),
					"service has not been saved");
		}
	}

	@Test
	@DisplayName("The method throws ProcessingException")
	void testRestClient_whenReadMessage_thenThrowsProcessingException() throws IOException {

		try (MockedStatic<ClientBuilder> clientBuilderMock = mockStatic(ClientBuilder.class)) {
			clientBuilderMock.when(ClientBuilder::newClient).thenReturn(client);

			Mockito.when(client.target(Mockito.anyString())).thenReturn(webTarget);
			Mockito.when(webTarget.path(Mockito.anyString())).thenReturn(webTarget);
			Mockito.when(webTarget.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilder);

			Mockito.when(invocationBuilder.post(Mockito.any()))
					.thenThrow(new ProcessingException("Connection refused"));
			RestClient restClient = RestClient.getInstance();
			assertThrows(ProcessingException.class, () -> restClient.saveEservice(eServiceDTO, client));
		}
	}

}

package it.pagopa.interop.probing.eservice.registry.updater.util;

import java.io.IOException;
import java.util.Objects;

import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestClient {

	private static RestClient instance;

	public static RestClient getInstance() throws IOException {
		if (Objects.isNull(instance)) {
			instance = new RestClient();
		}
		return instance;
	}

	private static final String ESERVICE_OPERATION_URL = "eservice.operation.url";

	private String eserviceOperationUrl;

	public RestClient() throws IOException {
		this.eserviceOperationUrl = PropertiesLoader.getInstance().getKey(ESERVICE_OPERATION_URL);
	}

	public Long saveEservice(EserviceDTO eservice, Client client) throws IOException {

		WebTarget webTarget = client.target(eserviceOperationUrl);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

		try {
			log.info("Call to " + eserviceOperationUrl + " for service " + eservice.getEserviceId() + " with version "
					+ eservice.getVersionId());
			Response response = invocationBuilder.put(Entity.entity(eservice, MediaType.APPLICATION_JSON));
			if (response.getStatus() == 200) {
				return response.readEntity(Long.class);
			} else {
				log.error("Service " + eservice.getEserviceId() + " with version " + eservice.getVersionId()
						+ " has not been saved.");
				throw new IOException();
			}
		} catch (ProcessingException e) {
			log.error("Service " + eservice.getEserviceId() + " with version " + eservice.getVersionId()
			+ " has not been saved.", e.getMessage());
			throw e;
		}

	}
}
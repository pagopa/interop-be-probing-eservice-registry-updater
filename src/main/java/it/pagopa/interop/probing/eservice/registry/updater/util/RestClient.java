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

  private static final String ESERVICE_OPERATION_URL = "api.operations.baseUrl";
  private static final String ESERVICE_BASE_PATH = "api.eservice.basePath";

  private String eserviceOperationUrl;
  private String eserviceBasePath;

  private RestClient() throws IOException {
    this.eserviceOperationUrl = PropertiesLoader.getInstance().getKey(ESERVICE_OPERATION_URL);
    this.eserviceBasePath = PropertiesLoader.getInstance().getKey(ESERVICE_BASE_PATH);
  }

  public Long saveEservice(EserviceDTO eservice, Client client) throws IOException {

    log.info("EserviceDTO: " + eservice.toString());

    WebTarget webTarget = client.target(eserviceOperationUrl + eserviceBasePath)
        .path(eservice.getEserviceId().toString()).path("versions")
        .path(eservice.getVersionId().toString()).path("saveEservice");

    Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

    try {
      log.info("Call to EserviceOperations ms save method for service " + eservice.getEserviceId()
          + " with version " + eservice.getVersionId());
      Entity<EserviceDTO> ciao = Entity.entity(eservice, MediaType.APPLICATION_JSON);

      log.info(ciao.toString());

      EserviceDTO entity = ciao.getEntity();

      log.info("Eservice name: " + entity.getName());
      log.info("Eservice producer: " + entity.getProducerName());
      log.info("Eservice versionNumber: " + entity.getVersionNumber());
      log.info("Eservice basePath: " + entity.getBasePath());
      log.info("Eservice eserviceID: " + entity.getEserviceId());
      log.info("Eservice state: " + entity.getState());
      log.info("Eservice technology: " + entity.getTechnology());
      log.info("Eservice versionId: " + entity.getVersionId());

      Response response =
          invocationBuilder.put(Entity.entity(eservice, MediaType.APPLICATION_JSON));

      if (response.getStatus() == 200) {
        return response.readEntity(Long.class);
      } else {
        throw new IOException("Service " + eservice.getEserviceId() + "LELLO with version "
            + eservice.getVersionId() + " has not been saved.");
      }
    } catch (ProcessingException e) {
      log.error("Service " + eservice.getEserviceId() + "LELLA with version "
          + eservice.getVersionId() + " has not been saved.", e.getMessage());
      throw e;
    }

  }
}

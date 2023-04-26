package it.pagopa.interop.probing.eservice.registry.updater.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;

public interface EserviceClient {

  @RequestLine("PUT  /{eserviceId}/versions/{versionId}/saveEservice")
  @Headers("Content-Type: application/json")
  Long saveEservice(@Param("eserviceId") String eserviceId, @Param("versionId") String versionId,
      EserviceDTO eservice);
}

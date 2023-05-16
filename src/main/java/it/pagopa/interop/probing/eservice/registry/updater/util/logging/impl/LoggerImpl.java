package it.pagopa.interop.probing.eservice.registry.updater.util.logging.impl;

import it.pagopa.interop.probing.eservice.registry.updater.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.UUID;

@Slf4j
public class LoggerImpl implements Logger {
  @Override
  public void logMessageSavingEservice(UUID eserviceId, UUID versionId) {
    log.info("Saving e-service. eserviceId={}, versionId={}", eserviceId, versionId);
  }

  @Override
  public void logMessageExceptionSavingEservice(UUID eserviceId, UUID versionId,
      String exceptionMessage) {
    log.error(
        "Exception occurred when trying to save e-service. eserviceId={}, versionId={}, exception:\n{}",
        eserviceId, versionId, exceptionMessage);
  }

  @Override
  public void logMessageEserviceSaved(UUID eserviceId, UUID versionId) {
    log.info("e-service has been saved. eserviceId={}, versionId={}", eserviceId, versionId);
  }

  @Override
  public void logMessageQueueMessageDeleted(UUID eserviceId, UUID versionId, URI queueUrl) {
    log.info(
        "Message deleted from queue, reading next message. eserviceId={}, versioneId={}, queue={}",
        eserviceId, versionId, queueUrl);
  }
}

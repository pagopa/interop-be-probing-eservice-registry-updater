package it.pagopa.interop.probing.eservice.registry.updater.util.logging.impl;

import java.net.URI;
import java.util.UUID;
import com.amazonaws.xray.AWSXRay;
import it.pagopa.interop.probing.eservice.registry.updater.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerImpl implements Logger {
  @Override
  public void logMessageSavingEservice(UUID eserviceId, UUID versionId) {
    log.info("[TRACE_ID= {}] - Saving e-service. eserviceId={}, versionId={}",
        AWSXRay.getCurrentSegment().getTraceId().toString(), eserviceId, versionId);
  }

  @Override
  public void logMessageExceptionSavingEservice(UUID eserviceId, UUID versionId,
      String exceptionMessage) {
    log.error(
        "[TRACE_ID= {}] - Exception occurred when trying to save e-service. eserviceId={}, versionId={}, exception:\n{}",
        AWSXRay.getCurrentSegment().getTraceId().toString(), eserviceId, versionId,
        exceptionMessage);
  }

  @Override
  public void logMessageEserviceSaved(UUID eserviceId, UUID versionId) {
    log.info("[TRACE_ID= {}] - e-service has been saved. eserviceId={}, versionId={}",
        AWSXRay.getCurrentSegment().getTraceId().toString(), eserviceId, versionId);
  }

  @Override
  public void logMessageQueueMessageDeleted(UUID eserviceId, UUID versionId, URI queueUrl) {
    log.info(
        "[TRACE_ID= {}] - Message deleted from queue, reading next message. eserviceId={}, versioneId={}, queue={}",
        AWSXRay.getCurrentSegment().getTraceId().toString(), eserviceId, versionId, queueUrl);
  }
}

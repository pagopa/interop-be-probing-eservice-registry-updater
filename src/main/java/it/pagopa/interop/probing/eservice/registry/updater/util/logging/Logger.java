package it.pagopa.interop.probing.eservice.registry.updater.util.logging;

import java.net.URI;
import java.util.UUID;

public interface Logger {

  void logMessageSavingEservice(UUID eserviceId, UUID versionId);

  void logMessageExceptionSavingEservice(UUID eserviceId, UUID versionId, String exceptionMessage);

  void logMessageEserviceSaved(UUID eserviceId, UUID versionId);

  void logMessageQueueMessageDeleted(UUID eserviceId, UUID versionId, URI queueUrl);
}

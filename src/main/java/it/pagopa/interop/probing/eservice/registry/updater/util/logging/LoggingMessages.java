package it.pagopa.interop.probing.eservice.registry.updater.util.logging;

public class LoggingMessages {

  public static final String ESERVICE_SAVING = "Saving e-service. eserviceId={}, versionId={}";
  public static final String ESERVICE_SAVE_EXCEPTION =
      "Exception occurred when trying to save e-service. eserviceId={}, versionId={}, exception:\n{}";

  public static final String ESERVICE_SAVED =
      "e-service has been saved. eserviceId={}, versionId={}";
  public static final String QUEUE_MESSAGE_DELETED =
      "Message deleted from queue, reading next message. eserviceId={}, versioneId={}, queue={}";
}

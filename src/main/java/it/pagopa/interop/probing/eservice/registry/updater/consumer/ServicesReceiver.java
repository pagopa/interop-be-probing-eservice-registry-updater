package it.pagopa.interop.probing.eservice.registry.updater.consumer;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import it.pagopa.interop.probing.eservice.registry.updater.client.EserviceClient;
import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs.SqsConfig;
import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.util.logging.Logger;
import it.pagopa.interop.probing.eservice.registry.updater.util.logging.impl.LoggerImpl;


public class ServicesReceiver {

  private final Logger logger = new LoggerImpl();
  private static ServicesReceiver instance;

  private static final String SQS = "amazon.sqs.endpoint.services-queue";
  private static final String ESERVICE_OPERATION_URL = "api.operations.baseUrl";
  private static final String ESERVICE_BASE_PATH = "api.eservice.basePath";

  public static ServicesReceiver getInstance() throws IOException {
    if (Objects.isNull(instance)) {
      instance = new ServicesReceiver();
    }
    return instance;

  }

  private String sqsUrlServices;
  private String eserviceOperationUrl;
  private String eserviceBasePath;

  public ServicesReceiver() throws IOException {
    this.sqsUrlServices = PropertiesLoader.getInstance().getKey(SQS);
    this.eserviceOperationUrl = PropertiesLoader.getInstance().getKey(ESERVICE_OPERATION_URL);
    this.eserviceBasePath = PropertiesLoader.getInstance().getKey(ESERVICE_BASE_PATH);
  }

  public void receiveStringMessage() throws IOException {


    ObjectMapper mapper = new ObjectMapper();
    SqsConfig sqs = SqsConfig.getInstance();

    ReceiveMessageRequest receiveMessageRequest =
        new ReceiveMessageRequest(sqsUrlServices).withMaxNumberOfMessages(10);
    List<Message> sqsMessages =
        sqs.getAmazonSQSAsync().receiveMessage(receiveMessageRequest).getMessages();

    while (Objects.nonNull(sqsMessages) && !sqsMessages.isEmpty()) {
      for (Message message : sqsMessages) {
        EserviceDTO service = mapper.readValue(message.getBody(), EserviceDTO.class);

        EserviceClient eserviceClient = Feign.builder().client(new OkHttpClient())
            .encoder(new GsonEncoder()).decoder(new GsonDecoder())
            .target(EserviceClient.class, eserviceOperationUrl + eserviceBasePath);

        logger.logMessageSavingEservice(service.getEserviceId(), service.getVersionId());
        try {
          eserviceClient.saveEservice(service.getEserviceId().toString(),
              service.getVersionId().toString(), service);
        } catch (Exception e) {
          logger.logMessageExceptionSavingEservice(service.getEserviceId(), service.getVersionId(),
              e.getMessage());
          throw e;
        }
        logger.logMessageEserviceSaved(service.getEserviceId(), service.getVersionId());

        sqs.getAmazonSQSAsync().deleteMessage(new DeleteMessageRequest()
            .withQueueUrl(sqsUrlServices).withReceiptHandle(message.getReceiptHandle()));

        logger.logMessageQueueMessageDeleted(service.getEserviceId(), service.getVersionId(),
            URI.create(sqsUrlServices));
      }
      sqsMessages = sqs.getAmazonSQSAsync().receiveMessage(receiveMessageRequest).getMessages();
    }

  }

}

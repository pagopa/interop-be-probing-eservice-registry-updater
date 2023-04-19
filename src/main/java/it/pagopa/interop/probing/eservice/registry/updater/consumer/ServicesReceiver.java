package it.pagopa.interop.probing.eservice.registry.updater.consumer;

import java.io.IOException;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServicesReceiver {

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

        log.info("Call to EserviceOperations ms save method for service " + service.getEserviceId()
            + " with version " + service.getVersionId());

        eserviceClient.saveEservice(service.getEserviceId().toString(),
            service.getVersionId().toString(), service);

        log.info("Service " + service.getEserviceId() + " with version " + service.getVersionId()
            + " has been saved.");

        sqs.getAmazonSQSAsync().deleteMessage(new DeleteMessageRequest()
            .withQueueUrl(sqsUrlServices).withReceiptHandle(message.getReceiptHandle()));

        log.info("Message deleted from queue -> Service " + service.getEserviceId()
            + " with version " + service.getVersionId() + " Reading next message.");
      }
      sqsMessages = sqs.getAmazonSQSAsync().receiveMessage(receiveMessageRequest).getMessages();
    }
  }

}

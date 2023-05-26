package it.pagopa.interop.probing.eservice.registry.updater.consumer;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import it.pagopa.interop.probing.eservice.registry.updater.client.EserviceClient;
import it.pagopa.interop.probing.eservice.registry.updater.dto.impl.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.util.logging.Logger;

@Singleton
public class ServicesReceiver {

  @Inject
  private Logger logger;

  @Inject
  private AmazonSQSAsync sqs;

  @Inject
  private ObjectMapper mapper;

  @Inject
  @Named("amazon.sqs.endpoint.services-queue")
  private String sqsUrlServices;

  @Inject
  @Named("api.operations.baseUrl")
  private String eserviceOperationUrl;

  @Inject
  @Named("api.operations.eservice.basePath")
  private String eserviceBasePath;

  public void receiveStringMessage() throws IOException {

    ReceiveMessageRequest receiveMessageRequest =
        new ReceiveMessageRequest(sqsUrlServices).withMaxNumberOfMessages(10);
    List<Message> sqsMessages = sqs.receiveMessage(receiveMessageRequest).getMessages();

    while (!sqsMessages.isEmpty()) {
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

        sqs.deleteMessage(new DeleteMessageRequest().withQueueUrl(sqsUrlServices)
            .withReceiptHandle(message.getReceiptHandle()));

        logger.logMessageQueueMessageDeleted(service.getEserviceId(), service.getVersionId(),
            URI.create(sqsUrlServices));
      }
      sqsMessages = sqs.receiveMessage(receiveMessageRequest).getMessages();
    }

  }

}

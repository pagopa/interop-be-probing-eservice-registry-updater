package it.pagopa.interop.probing.eservice.registry.updater.consumer;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.entities.TraceHeader;
import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;
import com.amazonaws.xray.strategy.sampling.DefaultSamplingStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;
import it.pagopa.interop.probing.eservice.registry.updater.client.EserviceClient;
import it.pagopa.interop.probing.eservice.registry.updater.dto.impl.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
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

    AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withDefaultPlugins()
        .withSamplingStrategy(new DefaultSamplingStrategy());

    AWSXRay.setGlobalRecorder(builder.build());

    ReceiveMessageRequest receiveMessageRequest =
        new ReceiveMessageRequest(sqsUrlServices).withMaxNumberOfMessages(10)
            .withAttributeNames("AWSTraceHeader").withMessageAttributeNames("AWSTraceHeader");

    List<Message> sqsMessages = sqs.receiveMessage(receiveMessageRequest).getMessages();
    // System.out.println(AWSXRay.getCurrentSegment().getTraceId().toString());
    while (!sqsMessages.isEmpty()) {
      for (Message message : sqsMessages) {
        String traceHeaderStr = message.getAttributes().get("AWSTraceHeader");
        TraceHeader traceHeader = TraceHeader.fromString(traceHeaderStr);
        AWSXRay.getGlobalRecorder().beginSegment("Interop-be-probing-eservice-registry-updater",
            traceHeader.getRootTraceId(), null);
        System.out
            .println(" traceHeader.getRootTraceId() : " + traceHeader.getRootTraceId().toString());
        System.out.println(" traceHeader.getParentId() : " + traceHeader.getParentId());
        // System.out.println(
        // " currentSegment ParentId() : " + AWSXRay.getCurrentSegment().getParentId().toString());
        System.out.println(
            " currentSegment TraceId() : " + AWSXRay.getCurrentSegment().getTraceId().toString());
        EserviceDTO service = mapper.readValue(message.getBody(), EserviceDTO.class);
        service.setEserviceId(UUID.randomUUID());
        service.setVersionId(UUID.randomUUID());
        // Recover the trace context from the trace header
        // Segment segment = AWSXRay.getCurrentSegment();
        // segment.setTraceId(traceHeader.getRootTraceId());
        // segment.setParentId(traceHeader.getParentId());
        // segment.setSampled(traceHeader.getSampled().equals(TraceHeader.SampleDecision.SAMPLED));
        // AWSXRay.getGlobalRecorder().getCurrentSegment();

        EserviceClient eserviceClient =
            Feign.builder().client(new ApacheHttpClient(HttpClientBuilder.create().build()))
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
        AWSXRay.endSegment();
      }
      sqsMessages = sqs.receiveMessage(receiveMessageRequest).getMessages();
    }

  }

}


package it.pagopa.interop.probing.eservice.registry.updater.unit.consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.xray.AWSXRay;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import feign.Client;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import it.pagopa.interop.probing.eservice.registry.updater.client.EserviceClient;
import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;
import it.pagopa.interop.probing.eservice.registry.updater.dto.impl.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceTechnology;
import it.pagopa.interop.probing.eservice.registry.updater.util.logging.Logger;

class ServicesReceiverTest {

  private Feign.Builder builderMock = mock(Feign.Builder.class);

  private ServicesReceiver servicesReceiver;

  private Logger loggerMock = mock(Logger.class);

  private AmazonSQSAsync sqsMock = mock(AmazonSQSAsync.class);

  private ObjectMapper mapperMock = mock(ObjectMapper.class);

  @InjectMocks
  private EserviceClient client = mock(EserviceClient.class);

  private Message message = new Message();
  private EserviceDTO eServiceDTO;
  private Map<String, String> attributes = new HashMap<>();
  private String mockedId = "mockedId";

  @BeforeEach
  void setup() throws IOException {
    AWSXRay.beginSegment("test");
    String[] basePath = {"basePath1", "basePath2"};
    String[] audience = {"audience1", "audience2"};
    eServiceDTO =
        EserviceDTO.builder().eserviceId(UUID.fromString("0b37ac73-cbd8-47f1-a14c-19bcc8f8f8e7"))
            .versionId(UUID.fromString("226574b8-82a1-4844-9484-55fffc9c15ef")).name("Service Name")
            .producerName("Producer Name").state(EserviceState.ACTIVE)
            .technology(EserviceTechnology.REST).basePath(basePath).audience(audience)
            .versionNumber(1).build();

    message.setBody(eServiceDTO.toString());
    message.setAttributes(attributes);
    attributes.put("AWSTraceHeader", mockedId);

    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(Logger.class).toInstance(loggerMock);
        bind(AmazonSQSAsync.class).toInstance(sqsMock);
        bind(ObjectMapper.class).toInstance(mapperMock);

        bind(String.class).annotatedWith(Names.named("amazon.sqs.endpoint.services-queue"))
            .toInstance("mocked-sqs-url");
        bind(String.class).annotatedWith(Names.named("api.operations.baseUrl"))
            .toInstance("mocked-base-url");
        bind(String.class).annotatedWith(Names.named("api.operations.eservice.basePath"))
            .toInstance("mocked-eservice-base-path");
      }
    });

    servicesReceiver = injector.getInstance(ServicesReceiver.class);
  }

  @AfterEach
  void clean() {
    AWSXRay.endSegment();
  }

  @Test
  @DisplayName("The method reads message from queue and saves to db")
  void testReceiveStringMessage_whenReadMessage_thenSaveMessage() throws IOException {



    Mockito.when(sqsMock.receiveMessage(Mockito.any(ReceiveMessageRequest.class))).thenReturn(
        new ReceiveMessageResult().withMessages(List.of(message)), new ReceiveMessageResult());

    Mockito.when(mapperMock.readValue(ArgumentMatchers.eq(eServiceDTO.toString()),
        ArgumentMatchers.eq(EserviceDTO.class))).thenReturn(eServiceDTO);

    try (MockedStatic<Feign> feignMock = mockStatic(Feign.class)) {
      feignMock.when(Feign::builder).thenReturn(builderMock);

      Mockito.when(builderMock.client(Mockito.any(Client.class))).thenReturn(builderMock);
      Mockito.when(builderMock.encoder(Mockito.any(GsonEncoder.class))).thenReturn(builderMock);
      Mockito.when(builderMock.decoder(Mockito.any(GsonDecoder.class))).thenReturn(builderMock);
      Mockito.when(builderMock.target(Mockito.eq(EserviceClient.class), Mockito.anyString()))
          .thenReturn(client);

      Mockito.when(client.saveEservice(Mockito.anyString(), Mockito.anyString(),
          Mockito.any(EserviceDTO.class))).thenReturn(10L);

      servicesReceiver.receiveStringMessage();

      verify(client).saveEservice(Mockito.anyString(), Mockito.anyString(),
          Mockito.any(EserviceDTO.class));
      verify(sqsMock).deleteMessage(Mockito.any());
      verify(sqsMock, times(2)).receiveMessage(Mockito.any(ReceiveMessageRequest.class));
    }
  }

  @Test
  @DisplayName("The method reads message from queue and saves to db")
  void testReceiveStringMessage_whenReadMessage_thenThrowsException() throws IOException {

    Mockito.when(sqsMock.receiveMessage(Mockito.any(ReceiveMessageRequest.class))).thenReturn(
        new ReceiveMessageResult().withMessages(List.of(message)), new ReceiveMessageResult());

    Mockito.when(mapperMock.readValue(ArgumentMatchers.eq(eServiceDTO.toString()),
        ArgumentMatchers.eq(EserviceDTO.class))).thenReturn(eServiceDTO);

    try (MockedStatic<Feign> feignMock = mockStatic(Feign.class)) {
      feignMock.when(Feign::builder).thenReturn(builderMock);

      Mockito.when(builderMock.client(Mockito.any(OkHttpClient.class))).thenReturn(builderMock);
      Mockito.when(builderMock.encoder(Mockito.any(GsonEncoder.class))).thenReturn(builderMock);
      Mockito.when(builderMock.decoder(Mockito.any(GsonDecoder.class))).thenReturn(builderMock);
      Mockito.when(builderMock.target(Mockito.eq(EserviceClient.class), Mockito.anyString()))
          .thenReturn(null);

      assertThrows(Exception.class, () -> servicesReceiver.receiveStringMessage());

    }
  }
}

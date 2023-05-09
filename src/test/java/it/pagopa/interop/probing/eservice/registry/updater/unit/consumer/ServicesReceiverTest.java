
package it.pagopa.interop.probing.eservice.registry.updater.unit.consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import it.pagopa.interop.probing.eservice.registry.updater.client.EserviceClient;
import it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs.SqsConfig;
import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;
import it.pagopa.interop.probing.eservice.registry.updater.dto.impl.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceTechnology;

class ServicesReceiverTest {

  SqsConfig sqs = mock(SqsConfig.class);
  AmazonSQSAsync amazonSqs = mock(AmazonSQSAsync.class);

  OkHttpClient clientMock = mock(OkHttpClient.class);
  GsonEncoder encoderMock = mock(GsonEncoder.class);
  GsonDecoder decoderMock = mock(GsonDecoder.class);
  Feign.Builder builderMock = mock(Feign.Builder.class);
  EserviceClient client = mock(EserviceClient.class);

  private EserviceDTO eServiceDTO;

  @BeforeEach
  void setup() throws IOException {
    String[] basePath = {"basePath1", "basePath2"};
    eServiceDTO =
        EserviceDTO.builder().eserviceId(UUID.fromString("0b37ac73-cbd8-47f1-a14c-19bcc8f8f8e7"))
            .versionId(UUID.fromString("226574b8-82a1-4844-9484-55fffc9c15ef")).name("Service Name")
            .producerName("Producer Name").state(EserviceState.ACTIVE)
            .technology(EserviceTechnology.REST).basePath(basePath).versionNumber(1).build();
  }

  @Test
  @DisplayName("The method reads message from queue and saves to db")
  void testReceiveStringMessage_whenReadMessage_thenSaveMessage() throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    Message message = new Message();
    List<Message> messages = new ArrayList<>();
    ReceiveMessageResult receiveMessageResult = new ReceiveMessageResult();

    message.setBody(mapper.writeValueAsString(eServiceDTO));
    messages.add(message);
    receiveMessageResult.setMessages(messages);

    try (MockedStatic<SqsConfig> sqsConfigMock = mockStatic(SqsConfig.class);
        MockedStatic<Feign> feignMock = mockStatic(Feign.class)) {
      sqsConfigMock.when(SqsConfig::getInstance).thenReturn(sqs);
      feignMock.when(Feign::builder).thenReturn(builderMock);
      Mockito.when(sqs.getAmazonSQSAsync()).thenReturn(amazonSqs);
      Mockito.when(amazonSqs.receiveMessage(Mockito.any(ReceiveMessageRequest.class)))
          .thenReturn(receiveMessageResult, new ReceiveMessageResult());
      Mockito.when(builderMock.client(Mockito.any(OkHttpClient.class))).thenReturn(builderMock);
      Mockito.when(builderMock.encoder(Mockito.any(GsonEncoder.class))).thenReturn(builderMock);
      Mockito.when(builderMock.decoder(Mockito.any(GsonDecoder.class))).thenReturn(builderMock);
      Mockito.when(builderMock.target(Mockito.eq(EserviceClient.class), Mockito.anyString()))
          .thenReturn(client);

      Mockito.when(client.saveEservice(Mockito.anyString(), Mockito.anyString(),
          Mockito.any(EserviceDTO.class))).thenReturn(10L);

      ServicesReceiver.getInstance().receiveStringMessage();
      verify(client).saveEservice(Mockito.anyString(), Mockito.anyString(),
          Mockito.any(EserviceDTO.class));
      verify(amazonSqs).deleteMessage(Mockito.any());
    }
  }
}

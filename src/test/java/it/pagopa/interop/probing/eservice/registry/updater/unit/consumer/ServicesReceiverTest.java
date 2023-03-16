
package it.pagopa.interop.probing.eservice.registry.updater.unit.consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs.SqsConfig;
import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;
import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.service.EserviceService;

class ServicesReceiverTest {

	SqsConfig sqs = mock(SqsConfig.class);

	AmazonSQSAsync amazonSqs = mock(AmazonSQSAsync.class);

	EserviceService service = mock(EserviceService.class);

	private EserviceDTO eServiceDTO;

	@BeforeEach
	void setup() throws IOException {
		eServiceDTO = new EserviceDTO();
		eServiceDTO.setEserviceId("0b37ac73-cbd8-47f1-a14c-19bcc8f8f8e7");
		eServiceDTO.setVersionId("226574b8-82a1-4844-9484-55fffc9c15ef");
		eServiceDTO.setName("Service Name");
		eServiceDTO.setProducerName("Producer Name");
		eServiceDTO.setState("ACTIVE");
		eServiceDTO.setTechnology("REST");
		String[] basePath = { "basePath1", "basePath2" };
		eServiceDTO.setBasePath(basePath);
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
				MockedStatic<EserviceService> serviceMock = mockStatic(EserviceService.class)) {

			sqsConfigMock.when(SqsConfig::getInstance).thenReturn(sqs);
			serviceMock.when(EserviceService::getInstance).thenReturn(service);

			when(sqs.getAmazonSQSAsync()).thenReturn(amazonSqs);
			when(amazonSqs.receiveMessage(Mockito.any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult,
					new ReceiveMessageResult());
			when(service.saveService(Mockito.any(EserviceDTO.class))).thenReturn(10L);

			ServicesReceiver.getInstance().receiveStringMessage();
			verify(service).saveService(Mockito.any());
			verify(amazonSqs).deleteMessage(Mockito.any());
		}
	}
}
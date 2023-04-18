package it.pagopa.interop.probing.eservice.registry.updater.consumer;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs.SqsConfig;
import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.util.RestClient;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServicesReceiver {

	private static ServicesReceiver instance;

	private static final String SQS = "amazon.sqs.endpoint.services-queue";

	public static ServicesReceiver getInstance() throws IOException {
		if (Objects.isNull(instance)) {
			instance = new ServicesReceiver();
		}
		return instance;

	}

	private String sqsUrlServices;

	public ServicesReceiver() throws IOException {
		this.sqsUrlServices = PropertiesLoader.getInstance().getKey(SQS);
	}

	public void receiveStringMessage() throws IOException {

		try (Client client = ClientBuilder.newClient()) {
			ObjectMapper mapper = new ObjectMapper();
			SqsConfig sqs = SqsConfig.getInstance();

			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrlServices)
					.withMaxNumberOfMessages(10);
			List<Message> sqsMessages = sqs.getAmazonSQSAsync().receiveMessage(receiveMessageRequest).getMessages();

			while (Objects.nonNull(sqsMessages) && !sqsMessages.isEmpty()) {
				for (Message message : sqsMessages) {
					EserviceDTO service = mapper.readValue(message.getBody(), EserviceDTO.class);
					RestClient.getInstance().saveEservice(service, client);
					log.info("Service " + service.getEserviceId() + " with version " + service.getVersionId()
							+ " has been saved.");
					sqs.getAmazonSQSAsync().deleteMessage(new DeleteMessageRequest().withQueueUrl(sqsUrlServices)
							.withReceiptHandle(message.getReceiptHandle()));
					log.info("Message deleted from queue -> Service " + service.getEserviceId() + " with version "
							+ service.getVersionId() + " Reading next message.");
				}
				sqsMessages = sqs.getAmazonSQSAsync().receiveMessage(receiveMessageRequest).getMessages();
			}
		}
	}

}

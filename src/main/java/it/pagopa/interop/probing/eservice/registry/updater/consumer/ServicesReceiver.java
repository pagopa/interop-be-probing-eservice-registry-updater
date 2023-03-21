/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 7 mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.consumer
* File Name   : ServicesReceiver.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
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
import it.pagopa.interop.probing.eservice.registry.updater.service.EserviceService;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
public class ServicesReceiver {

	/** The instance. */
	private static ServicesReceiver instance;

	/** The Constant SQS. */
	private static final String SQS = "amazon.sqs.endpoint.services-queue";

	/**
	 * Gets the single instance of ServicesReceiver.
	 *
	 * @return single instance of ServicesReceiver
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ServicesReceiver getInstance() throws IOException {
		if (Objects.isNull(instance)) {
			instance = new ServicesReceiver();
		}
		return instance;

	}

	/** The sqs url services. */
	private String sqsUrlServices;

	/**
	 * Instantiates a new services receiver.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ServicesReceiver() throws IOException {
		this.sqsUrlServices = PropertiesLoader.getInstance().getKey(SQS);
	}

	/**
	 * Receive string message.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException 
	 */
	public void receiveStringMessage() throws IOException {
		log.info("receiveStringMessage START");
		ObjectMapper mapper = new ObjectMapper();
		SqsConfig sqs = SqsConfig.getInstance();
		log.info("subito prima receive");
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrlServices)
				.withMaxNumberOfMessages(10);
		List<Message> sqsMessages = sqs.getAmazonSQSAsync().receiveMessage(receiveMessageRequest).getMessages();
		log.info("subito dopo receive");
		while (Objects.nonNull(sqsMessages) && !sqsMessages.isEmpty()) {
			for (Message message : sqsMessages) {
				EserviceDTO service = mapper.readValue(message.getBody(), EserviceDTO.class);
				EserviceService.getInstance().saveService(service);
				log.info("Service " + service.getEserviceId() + " with version " + service.getVersionId()
						+ " has been saved.");
				sqs.getAmazonSQSAsync().deleteMessage(new DeleteMessageRequest().withQueueUrl(sqsUrlServices)
						.withReceiptHandle(message.getReceiptHandle()));
				log.info("Message deleted from queue -> Service " + service.getEserviceId() + " with version "
						+ service.getVersionId() + " Reading next message.");
			}
			sqsMessages = sqs.getAmazonSQSAsync().receiveMessage(receiveMessageRequest).getMessages();
		}
		log.info("receiveStringMessage END");
	}

}

/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 28 feb 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
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
import java.util.Properties;

import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs.SqsConfig;
import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.service.EserviceService;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ServicesReceiver.
 */
@Slf4j
public class ServicesReceiver {

	/** The sqs config. */
	@Inject
	SqsConfig sqsConfig;

	/** The eservice service. */
	@Inject
	EserviceService eserviceService;

	/** The sqs url services. */
	private String sqsUrlServices;

	/**
	 * Instantiates a new services receiver.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ServicesReceiver() throws IOException {
		Properties configuration = PropertiesLoader.loadProperties("application.properties");
		this.sqsUrlServices = configuration.getProperty("amazon.sqs.end-point.services-queue");
	}

	/**
	 * Receive string message.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void receiveStringMessage() throws IOException {

		GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest(sqsUrlServices)
				.withAttributeNames("All");
		GetQueueAttributesResult getQueueAttributesResult = sqsConfig.amazonSQSAsync()
				.getQueueAttributes(getQueueAttributesRequest);
		ObjectMapper mapper = new ObjectMapper();

		Integer numberOfMessages = Integer
				.parseInt(getQueueAttributesResult.getAttributes().get("ApproximateNumberOfMessages"));

		if (numberOfMessages != null && numberOfMessages > 0) {

			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrlServices);
			List<Message> sqsMessages = sqsConfig.amazonSQSAsync().receiveMessage(receiveMessageRequest).getMessages();
			for (Message message : sqsMessages) {
				EserviceDTO service = mapper.readValue(message.getBody(), EserviceDTO.class);
				eserviceService.saveService(service);
				log.info("Service saved.");
				sqsConfig.amazonSQSAsync().deleteMessage(new DeleteMessageRequest().withQueueUrl(sqsUrlServices)
						.withReceiptHandle(message.getReceiptHandle()));
			}

		}
	}

}

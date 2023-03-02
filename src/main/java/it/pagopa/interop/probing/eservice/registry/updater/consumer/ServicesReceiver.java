/**************************************************************************
 *
 * Copyright 2023 (C) DXC
 *
 * Created on  : 2 mar 2023
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
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs.SqsConfig;
import it.pagopa.interop.probing.eservice.registry.updater.dto.EserviceDTO;
import it.pagopa.interop.probing.eservice.registry.updater.service.EserviceService;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ServicesReceiver.
 */

/** The Constant log. */

/** The Constant log. */

/** The Constant log. */
@Slf4j
public class ServicesReceiver {


	/** The instance. */
	private static ServicesReceiver instance;


	/**
	 * Gets the single instance of BucketService.
	 *
	 * @return single instance of BucketService
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ServicesReceiver getInstance() throws IOException {
		if (instance == null) {
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
		Properties configuration = PropertiesLoader.loadProperties("application.properties");
		this.sqsUrlServices = configuration.getProperty("amazon.sqs.end-point.services-queue");
	}

	/**
	 * Receive string message.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void receiveStringMessage() throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		SqsConfig sqs = SqsConfig.getInstance();

		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrlServices);
		List<Message> sqsMessages = sqs.amazonSQSAsync().receiveMessage(receiveMessageRequest).getMessages();
		while(sqsMessages!=null && !sqsMessages.isEmpty()) {
				EserviceDTO service = mapper.readValue(sqsMessages.get(0).getBody(), EserviceDTO.class);
				EserviceService.getInstance().saveService(service);
				log.info("Service saved.");
				sqs.amazonSQSAsync().deleteMessage(new DeleteMessageRequest().withQueueUrl(sqsUrlServices)
						.withReceiptHandle(sqsMessages.get(0).getReceiptHandle()));
				log.info("Message deleted from queue. Reading next message.");
				sqsMessages=sqs.amazonSQSAsync().receiveMessage(receiveMessageRequest).getMessages();
			}
		}
	


}

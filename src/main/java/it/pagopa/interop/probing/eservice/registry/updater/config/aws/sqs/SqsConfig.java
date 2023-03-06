/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 6 Mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs
* File Name   : SqsConfig.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs;

import java.util.Objects;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

import lombok.Getter;

/**
 * The Class SqsConfig.
 */
/**
 * Gets the amazon SQS async.
 *
 * @return the amazon SQS async
 */

/**
 * Gets the amazon SQS async.
 *
 * @return the amazon SQS async
 */
@Getter
public class SqsConfig {

	/** The amazon SQS async. */
	private AmazonSQSAsync amazonSQSAsync;

	/** The instance. */
	private static SqsConfig instance;

	/**
	 * Gets the single instance of SqsConfig.
	 *
	 * @return single instance of SqsConfig
	 */
	public static SqsConfig getInstance() {
		if (Objects.isNull(instance)) {
			instance = new SqsConfig();
		}
		return instance;
	}

	/**
	 * Instantiates a new sqs config.
	 */
	private SqsConfig() {
		this.amazonSQSAsync = amazonSQSAsync();
	}

	/**
	 * Amazon SQS async.
	 *
	 * @return the amazon SQS async
	 */
	private AmazonSQSAsync amazonSQSAsync() {
		return AmazonSQSAsyncClientBuilder.standard().build();

	}

}

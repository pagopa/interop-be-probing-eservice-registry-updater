/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 28 feb 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs
* File Name   : SqsConfig.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs;

import java.io.IOException;
import java.util.Properties;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.util.ProjectConstants;

/**
 * The Class SqsConfig.
 */
public class SqsConfig {

	/** The region. */
	private String region;

	/** The access key. */
	private String accessKey;

	/** The secret key. */
	private String secretKey;

	/** The sqs url services. */
	private String sqsUrlServices;

	/** The profile. */
	private String profile;

	
	/** The Constant ACCESS_KEY. */
	private static final String ACCESS_KEY = "amazon.sqs.credentials.accessKey";
	
	/** The Constant SECRET_KEY. */
	private static final String SECRET_KEY = "amazon.sqs.credentials.accessKey";

	/** The Constant REGION. */
	private static final String REGION = "amazon.sqs.region.static";
	
	/** The Constant URL. */
	private static final String URL = "amazon.sqs.end-point.services-queue";
	
	/** The Constant PROFILE. */
	private static final String PROFILE = "amazon.sqs.end-point.services-queue";
	


	/**
	 * Instantiates a new sqs config.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public SqsConfig() throws IOException {
		Properties configuration = PropertiesLoader.loadProperties(ProjectConstants.PROPERTIES);
		String accessKey = configuration.getProperty(ACCESS_KEY);
		String secretKey = configuration.getProperty(SECRET_KEY);
		String amazonAWSRegion = configuration.getProperty(REGION);
		String sqsUrlServices = configuration.getProperty(URL);
		String profile = configuration.getProperty(PROFILE);
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.region = amazonAWSRegion;
		this.sqsUrlServices = sqsUrlServices;
		this.profile = profile;
	}

	/**
	 * Amazon SQS async.
	 *
	 * @return the amazon SQS async
	 */
	public AmazonSQSAsync amazonSQSAsync() {
		AmazonSQSAsync client = profile.equals("prod") ? AmazonSQSAsyncClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(sqsUrlServices, region)).build()
				: AmazonSQSAsyncClientBuilder.standard()
						.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(sqsUrlServices, region))
						.withCredentials(
								new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
						.build();
		return client;
	}


}

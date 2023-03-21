
package it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs;

import java.util.Objects;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

import lombok.Getter;

@Getter
public class SqsConfig {

	private AmazonSQSAsync amazonSQSAsync;

	private static SqsConfig instance;

	public static SqsConfig getInstance() {
		if (Objects.isNull(instance)) {
			instance = new SqsConfig();
		}
		return instance;
	}

	private SqsConfig() {
		this.amazonSQSAsync = amazonSQSAsync();
	}

	private AmazonSQSAsync amazonSQSAsync() {
		return AmazonSQSAsyncClientBuilder.standard().withCredentials(new DefaultAWSCredentialsProviderChain()).build();

	}

}

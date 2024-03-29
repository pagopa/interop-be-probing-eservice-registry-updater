
package it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.handlers.TracingHandler;
import com.amazonaws.xray.strategy.IgnoreErrorContextMissingStrategy;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.Getter;

@Getter
public class SqsConfig extends AbstractModule {

  @Override
  protected void configure() {
    bind(SqsConfig.class).asEagerSingleton();
  }

  @Provides
  @Singleton
  public AmazonSQSAsync provideAmazonSQSAsync() {
    AWSXRay.getGlobalRecorder().setContextMissingStrategy(new IgnoreErrorContextMissingStrategy());
    return AmazonSQSAsyncClientBuilder.standard()
        .withCredentials(new DefaultAWSCredentialsProviderChain())
        .withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder())).build();
  }

}

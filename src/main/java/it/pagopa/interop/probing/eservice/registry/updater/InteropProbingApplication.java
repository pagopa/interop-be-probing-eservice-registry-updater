package it.pagopa.interop.probing.eservice.registry.updater;

import java.io.IOException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import it.pagopa.interop.probing.eservice.registry.updater.config.BaseModule;
import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs.SqsConfig;
import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;

public class InteropProbingApplication {

  public static void main(String[] args) throws IOException {
    Injector injector =
        Guice.createInjector(new BaseModule(), new PropertiesLoader(), new SqsConfig());
    ServicesReceiver servicesReceiver = injector.getInstance(ServicesReceiver.class);
    servicesReceiver.receiveStringMessage();
  }
}

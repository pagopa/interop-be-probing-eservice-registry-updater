package it.pagopa.interop.probing.eservice.registry.updater;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import com.google.inject.Guice;
import com.google.inject.Injector;
import it.pagopa.interop.probing.eservice.registry.updater.config.BaseModule;
import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs.SqsConfig;
import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InteropProbingApplication {

  public static void main(String[] args) throws IOException {
    log.info("Eservice-Registry-Updater started at: {}", LocalDateTime.now(ZoneOffset.UTC));
    Injector injector =
        Guice.createInjector(new BaseModule(), new PropertiesLoader(), new SqsConfig());
    ServicesReceiver servicesReceiver = injector.getInstance(ServicesReceiver.class);
    servicesReceiver.receiveStringMessage();
    log.info("Eservice-Registry-Updater ended at: {}", LocalDateTime.now(ZoneOffset.UTC));
  }
}

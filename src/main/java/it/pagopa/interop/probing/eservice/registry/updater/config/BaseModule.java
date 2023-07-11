package it.pagopa.interop.probing.eservice.registry.updater.config;

import com.google.inject.AbstractModule;
import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;
import it.pagopa.interop.probing.eservice.registry.updater.util.logging.Logger;
import it.pagopa.interop.probing.eservice.registry.updater.util.logging.impl.LoggerImpl;

public class BaseModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Logger.class).to(LoggerImpl.class);
    bind(ServicesReceiver.class).asEagerSingleton();
  }
}

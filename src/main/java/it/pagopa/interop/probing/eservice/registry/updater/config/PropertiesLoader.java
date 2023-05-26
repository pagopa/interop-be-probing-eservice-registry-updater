
package it.pagopa.interop.probing.eservice.registry.updater.config;

import java.util.Properties;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class PropertiesLoader extends AbstractModule {

  @Override
  protected void configure() {
    Names.bindProperties(binder(), loadProperties());
  }

  @Provides
  @Singleton
  public Config provideConfig() {
    return ConfigFactory.load();
  }

  private Properties loadProperties() {
    Config config = ConfigFactory.load();
    Properties properties = new Properties();
    config.entrySet()
        .forEach(entry -> properties.put(entry.getKey(), entry.getValue().unwrapped()));
    log.info("Properties loaded successfully");
    return properties;
  }

}


package it.pagopa.interop.probing.eservice.registry.updater.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import com.google.inject.Guice;
import com.google.inject.Injector;
import it.pagopa.interop.probing.eservice.registry.updater.InteropProbingApplication;
import it.pagopa.interop.probing.eservice.registry.updater.config.BaseModule;
import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.config.aws.sqs.SqsConfig;
import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;

class InteropProbingApplicationTest {

  Injector injector = mock(Injector.class);
  ServicesReceiver servicesReceiver = mock(ServicesReceiver.class);

  @Test
  void main() throws IOException {
    try (MockedStatic<Guice> mockedGuice = mockStatic(Guice.class);) {

      mockedGuice.when(() -> Guice.createInjector(any(BaseModule.class),
          any(PropertiesLoader.class), any(SqsConfig.class))).thenReturn(injector);

      Mockito.when(injector.getInstance(ArgumentMatchers.eq(ServicesReceiver.class)))
          .thenReturn(servicesReceiver);
      InteropProbingApplication.main(new String[0]);

      verify(servicesReceiver).receiveStringMessage();
    }
  }
}


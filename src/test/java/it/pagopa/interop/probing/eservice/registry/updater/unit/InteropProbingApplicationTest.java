package it.pagopa.interop.probing.eservice.registry.updater.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import it.pagopa.interop.probing.eservice.registry.updater.InteropProbingApplication;
import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;

class InteropProbingApplicationTest {

  ServicesReceiver servicesReceiver = mock(ServicesReceiver.class);

  @Test
  void main() throws Exception {
    try (MockedStatic<ServicesReceiver> servicesReceiverMock = mockStatic(ServicesReceiver.class)) {
      servicesReceiverMock.when(ServicesReceiver::getInstance).thenReturn(servicesReceiver);
      InteropProbingApplication.main(null);
      verify(servicesReceiver).receiveStringMessage();
    }
  }
}

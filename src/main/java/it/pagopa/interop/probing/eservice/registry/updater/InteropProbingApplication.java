package it.pagopa.interop.probing.eservice.registry.updater;

import java.io.IOException;
import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;

public class InteropProbingApplication {

  public static void main(String[] args) throws IOException {
    ServicesReceiver.getInstance().receiveStringMessage();
  }
}

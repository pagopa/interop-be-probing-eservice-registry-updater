
package it.pagopa.interop.probing.eservice.registry.updater;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.consumer.ServicesReceiver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InteropProbingApplication {

	private static final String PERSISTENCE_UNIT_NAME = "hibernate.persist.unit";

	private static final String CONNECTION_PROP_URL = "javax.persistence.jdbc.url";

	private static final String CONNECTION_PROP_USR = "javax.persistence.jdbc.user";

	private static final String CONNECTION_PROP_PSW = "javax.persistence.jdbc.password";

	private static final String DB_URL = "db.url";

	private static final String DB_USR = "db.user";

	private static final String DB_PSW = "db.password";

	public static void main(String[] args) throws IOException {
		EntityManager em = Persistence.createEntityManagerFactory(
				PropertiesLoader.getInstance().getKey(PERSISTENCE_UNIT_NAME), getProperties()).createEntityManager();
		try {
			ServicesReceiver.getInstance().receiveStringMessage(em);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			em.close();
		}
	}

	private static Map<String, String> getProperties() {

		Map<String, String> result = new HashMap<>();
		try {

			result.put(CONNECTION_PROP_URL, PropertiesLoader.getInstance().getKey(DB_URL));
			result.put(CONNECTION_PROP_USR, PropertiesLoader.getInstance().getKey(DB_USR));
			result.put(CONNECTION_PROP_PSW, PropertiesLoader.getInstance().getKey(DB_PSW));

		} catch (IOException e) {
			log.error("Connect failed. Unable to read properties.");
		}

		return result;
	}

}

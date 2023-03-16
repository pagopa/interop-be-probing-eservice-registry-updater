
package it.pagopa.interop.probing.eservice.registry.updater.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.model.Eservice;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class EserviceDao {

	private static EserviceDao instance;

	private static final String CONNECTION_PROP_URL = "javax.persistence.jdbc.url";

	private static final String CONNECTION_PROP_USR = "javax.persistence.jdbc.user";

	private static final String CONNECTION_PROP_PSW = "javax.persistence.jdbc.password";

	private static final String DB_URL = "db.url";

	private static final String DB_USR = "db.user";

	private static final String DB_PSW = "db.password";

	private static final String PERSISTENCE_UNIT_NAME = "hibernate.persist.unit";

	private static final String VERSION_ID_PARAM_NAME = "versionIdParam";

	private static final String SERVICE_ID_PARAM_NAME = "serviceIdParam";

	private static final String FIND_BY_SERVICE_AND_VERSION_ID_QUERY = "SELECT e FROM Eservice e WHERE e.eserviceId = :serviceIdParam AND e.versionId = :versionIdParam";

	EntityManager em;

	private EserviceDao() throws IOException {
		this.em = Persistence.createEntityManagerFactory(PropertiesLoader.getInstance().getKey(PERSISTENCE_UNIT_NAME),
				getProperties()).createEntityManager();
	}

	public static EserviceDao getInstance() throws IOException {
		if (Objects.isNull(instance)) {
			instance = new EserviceDao();
		}
		return instance;
	}

	private Map<String, String> getProperties() {

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

	public Eservice findByEserviceIdAndVersionId(UUID serviceIdParam, UUID versionIdParam) {
		TypedQuery<Eservice> q = em.createQuery(FIND_BY_SERVICE_AND_VERSION_ID_QUERY, Eservice.class);
		q.setParameter(SERVICE_ID_PARAM_NAME, serviceIdParam);
		q.setParameter(VERSION_ID_PARAM_NAME, versionIdParam);
		if (!q.getResultList().isEmpty()) {
			return q.getResultList().get(0);
		} else {
			return null;
		}
	}

	public Long save(Eservice eservice) {
		em.getTransaction().begin();
		if (Objects.isNull(eservice.getId())) {
			em.persist(eservice);
		} else {
			em.merge(eservice);
		}
		em.getTransaction().commit();
		em.clear();
		return eservice.getId();
	}

}

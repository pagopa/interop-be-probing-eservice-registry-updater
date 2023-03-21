
package it.pagopa.interop.probing.eservice.registry.updater.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.model.Eservice;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class EserviceDao {

	private static EserviceDao instance;

	private static final String VERSION_ID_PARAM_NAME = "versionIdParam";

	private static final String SERVICE_ID_PARAM_NAME = "serviceIdParam";

	private static final String FIND_BY_SERVICE_AND_VERSION_ID_QUERY = "SELECT e FROM Eservice e WHERE e.eserviceId = :serviceIdParam AND e.versionId = :versionIdParam";

	private EserviceDao() throws IOException {
	}

	public static EserviceDao getInstance() throws IOException {
		if (Objects.isNull(instance)) {
			instance = new EserviceDao();
		}
		return instance;
	}

	public Eservice findByEserviceIdAndVersionId(UUID serviceIdParam, UUID versionIdParam, EntityManager em) {
		TypedQuery<Eservice> q = em.createQuery(FIND_BY_SERVICE_AND_VERSION_ID_QUERY, Eservice.class);
		q.setParameter(SERVICE_ID_PARAM_NAME, serviceIdParam);
		q.setParameter(VERSION_ID_PARAM_NAME, versionIdParam);
		if (!q.getResultList().isEmpty()) {
			return q.getResultList().get(0);
		} else {
			return null;
		}
	}

	public Long save(Eservice eservice,EntityManager em) {
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

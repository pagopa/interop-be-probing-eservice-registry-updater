/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 10 Mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.dao
* File Name   : EserviceDao.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
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

/** The Constant log. */

/** The Constant log. */

/** The Constant log. */
@Slf4j

/**
 * Gets the em.
 *
 * @return the em
 */

/**
 * Gets the em.
 *
 * @return the em
 */

/**
 * Gets the em.
 *
 * @return the em
 */
@Getter
public class EserviceDao {

	/** The instance. */
	private static EserviceDao instance;

	/** The Constant CONNECTION_PROP_URL. */
	private static final String CONNECTION_PROP_URL = "javax.persistence.jdbc.url";

	/** The Constant CONNECTION_PROP_USR. */
	private static final String CONNECTION_PROP_USR = "javax.persistence.jdbc.user";

	/** The Constant CONNECTION_PROP_PSW. */
	private static final String CONNECTION_PROP_PSW = "javax.persistence.jdbc.password";

	/** The Constant DB_URL. */
	private static final String DB_URL = "db.url";

	/** The Constant DB_USR. */
	private static final String DB_USR = "db.user";

	/** The Constant DB_PSW. */
	private static final String DB_PSW = "db.password";

	/** The Constant PERSISTENCE_UNIT_NAME. */
	private static final String PERSISTENCE_UNIT_NAME = "hibernate.persist.unit";

	/** The Constant VERSION_ID_PARAM_NAME. */
	private static final String VERSION_ID_PARAM_NAME = "versionIdParam";

	/** The Constant SERVICE_ID_PARAM_NAME. */
	private static final String SERVICE_ID_PARAM_NAME = "serviceIdParam";

	/** The Constant FIND_BY_SERVICE_AND_VERSION_ID_QUERY. */
	private static final String FIND_BY_SERVICE_AND_VERSION_ID_QUERY = "SELECT e FROM Eservice e WHERE e.eserviceId = :serviceIdParam AND e.versionId = :versionIdParam";

	/** The em. */
	EntityManager em;

	/**
	 * Instantiates a new eservice dao.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private EserviceDao() throws IOException {
		this.em = Persistence.createEntityManagerFactory(PropertiesLoader.getInstance().getKey(PERSISTENCE_UNIT_NAME),
				getProperties()).createEntityManager();
	}

	/**
	 * Gets the single instance of EserviceDao.
	 *
	 * @return single instance of EserviceDao
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static EserviceDao getInstance() throws IOException {
		if (Objects.isNull(instance)) {
			instance = new EserviceDao();
		}
		return instance;
	}

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
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

	/**
	 * Find by eservice id and version id.
	 *
	 * @param serviceIdParam the service id param
	 * @param versionIdParam the version id param
	 * @return the eservice
	 */
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

	/**
	 * Save.
	 *
	 * @param eservice the eservice
	 * @return the long
	 */
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

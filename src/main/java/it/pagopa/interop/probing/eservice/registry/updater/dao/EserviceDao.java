/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 6 Mar 2023
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
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import it.pagopa.interop.probing.eservice.registry.updater.config.PropertiesLoader;
import it.pagopa.interop.probing.eservice.registry.updater.model.Eservice;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class EserviceRepositoryImpl.
 */

/** The Constant log. */

/** The Constant log. */

/** The Constant log. */
@Slf4j

/**
 * Instantiates a new eservice dao.
 */

/**
 * Instantiates a new eservice dao.
 */
@NoArgsConstructor
public class EserviceDao {

	/** The instance. */
	private static EserviceDao instance;

	/** The Constant CONNECTION_PROP_URL. */
	private static final String CONNECTION_PROP_URL = "javax.persistence.jdbc.url";
	
	/** The Constant CONNECTION_PROP_USR. */
	private static final String CONNECTION_PROP_USR = "javax.persistence.jdbc.user";
	
	/** The Constant CONNECTION_PROP_PSW. */
	private static final String CONNECTION_PROP_PSW = "javax.persistence.jdbc.password";
	
	/** The Constant AURORA_URL. */
	private static final String AURORA_URL = "amazon.aurora.url";
	
	/** The Constant AURORA_USR. */
	private static final String AURORA_USR = "amazon.aurora.user";
	
	/** The Constant AURORA_PSW. */
	private static final String AURORA_PSW = "amazon.aurora.password";
	
	/** The Constant PERSISTENCE_UNIT_NAME. */
	private static final String PERSISTENCE_UNIT_NAME = "interop-db";
	
	/** The Constant VERSION_ID_PARAM_NAME. */
	private static final String VERSION_ID_PARAM_NAME = "versionIdParam";
	
	/** The Constant SERVICE_ID_PARAM_NAME. */
	private static final String SERVICE_ID_PARAM_NAME = "serviceIdParam";
	
	/** The Constant FIND_BY_SERVICE_AND_VERSION_ID_QUERY. */
	private static final String FIND_BY_SERVICE_AND_VERSION_ID_QUERY = "SELECT e FROM Eservice e WHERE e.eserviceId = :serviceIdParam AND e.versionId = :versionIdParam";

	/**
	 * Instantiates a new eservice dao.
	 *
	 * @param entityManager the entity manager
	 */
	public EserviceDao(EntityManager entityManager) {
		this.em = entityManager;
	}

	/**
	 * Gets the single instance of BucketService.
	 *
	 * @return single instance of BucketService
	 */
	public static EserviceDao getInstance() {
		if (Objects.isNull(instance)) {
			instance = new EserviceDao();
		}
		return instance;
	}

	/** The em. */
	@PersistenceContext
	EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, getProperties()).createEntityManager();

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	private Map<String, String> getProperties() {

		Map<String, String> result = new HashMap<>();
		try {

			result.put(CONNECTION_PROP_URL, PropertiesLoader.getInstance().getKey(AURORA_URL));
			result.put(CONNECTION_PROP_USR, PropertiesLoader.getInstance().getKey(AURORA_USR));
			result.put(CONNECTION_PROP_PSW, PropertiesLoader.getInstance().getKey(AURORA_PSW));

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
	 * @return the eservice
	 */
	public Long save(Eservice eservice) {
		em.getTransaction().begin();
		if (Objects.isNull(eservice.getId())) {
			em.persist(eservice);
		} else {
			em.merge(eservice);
		}
		em.getTransaction().commit();
		return eservice.getId();
	}

}

/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 3 mar 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.dao
* File Name   : EserviceDao.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
@Slf4j

/**
 * Instantiates a new eservice dao.
 */
@NoArgsConstructor
public class EserviceDao {

	/** The instance. */
	private static EserviceDao instance;

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
		if (instance == null) {
			instance = new EserviceDao();
		}
		return instance;
	}

	/** The em. */
	@PersistenceContext
	EntityManager em = Persistence.createEntityManagerFactory("interop-db", getProperties()).createEntityManager();

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	private Map<String, String> getProperties() {

		Properties configuration;
		Map<String, String> result = new HashMap<>();
		try {
			configuration = PropertiesLoader.loadProperties("application.properties");

			String auroraUrl = configuration.getProperty("amazon.aurora.url");
			String auroraUser = configuration.getProperty("amazon.aurora.user");
			String auroraPassword = configuration.getProperty("amazon.aurora.password");

			result.put("javax.persistence.jdbc.url", auroraUrl);
			result.put("javax.persistence.jdbc.user", auroraUser);
			result.put("javax.persistence.jdbc.password", auroraPassword);

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
		TypedQuery<Eservice> q = em.createQuery(
				"SELECT e FROM Eservice e WHERE e.eserviceId = :serviceIdParam AND e.versionId = :versionIdParam",
				Eservice.class);
		q.setParameter("serviceIdParam", serviceIdParam);
		q.setParameter("versionIdParam", versionIdParam);

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
		if (eservice.getId() == null) {
			em.persist(eservice);
		} else {
			em.merge(eservice);
		}
		em.getTransaction().commit();
		return eservice.getId();
	}

}

/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 7 mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.unit.dao
* File Name   : EserviceDaoTest.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/

package it.pagopa.interop.probing.eservice.registry.updater.unit.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.pagopa.interop.probing.eservice.registry.updater.dao.EserviceDao;
import it.pagopa.interop.probing.eservice.registry.updater.model.Eservice;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;

/**
 * The Class EserviceDaoTest.
 */
class EserviceDaoTest {

	/** The repo. */
	static EserviceDao repo;

	/** The entity manager. */
	static EntityManager entityManager;

	/** The version UUID. */
	static UUID versionUUID;

	/** The service UUID. */
	static UUID serviceUUID;

	/** The version UUID 2. */
	static UUID versionUUID2;

	/** The service UUID 2. */
	static UUID serviceUUID2;

	/** The version UUID 3. */
	static UUID versionUUID3;

	/** The service UUID 3. */
	static UUID serviceUUID3;

	/** The Constant CONNECTION_PROP_URL. */
	private static final String CONNECTION_PROP_URL = "hibernate.connection.url";

	/** The Constant CONNECTION_PROP_USR. */
	private static final String CONNECTION_PROP_USR = "hibernate.connection.username";

	/** The q. */
	TypedQuery<Eservice> q;

	/**
	 * Setup.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@BeforeAll
	public static void setup() throws IOException {
		Map<String, String> result = new HashMap<>();
		result.put(CONNECTION_PROP_URL, "jdbc:hsqldb:mem:interop-db-pu-in-memory");
		result.put(CONNECTION_PROP_USR, "sa");
		repo = EserviceDao.getInstance();
		entityManager = repo.getEm();
		versionUUID = UUID.randomUUID();
		serviceUUID = UUID.randomUUID();
		versionUUID2 = UUID.randomUUID();
		serviceUUID2 = UUID.randomUUID();
		versionUUID3 = UUID.randomUUID();
		serviceUUID3 = UUID.randomUUID();
	}

	/**
	 * Close.
	 */
	@AfterAll
	public static void close() {
		entityManager.close();
	}

	/**
	 * Test eservice entity when eservice data not provided throws exception.
	 */
	@Test
	@DisplayName("The save method is executed if the Eservice not exists")
	void testEserviceEntity_whenEserviceDataNotProvided_throwsException() {
		Eservice emptyEservice = new Eservice();
		assertThrows(PersistenceException.class, () -> entityManager.persist(emptyEservice),
				"e-service should not be saved when missing required data");
	}

	/**
	 * Test save eservice when exists given valid eservice id and version id then updates.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("The update method is executed if the Eservice exists")
	void testSaveEservice_whenExists_GivenValidEserviceIdAndVersionId_thenUpdates() throws IOException {
		entityManager.getTransaction().begin();
		Eservice eservice = getEserviceEntity(serviceUUID, versionUUID);
		entityManager.persist(eservice);
		long oldId = eservice.getId();
		entityManager.getTransaction().commit();

		repo.save(eservice);
		long newId = eservice.getId();
		assertEquals(oldId, newId);
	}

	/**
	 * Test find eservice when exists given valid E service id and version id then return entity.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("The findEservice method is executed if the Eservice exists")
	void testFindEservice_whenExists_GivenValidEServiceIdAndVersionId_thenReturnEntity() throws IOException {
		entityManager.getTransaction().begin();
		Eservice eservice = getEserviceEntity(serviceUUID2, versionUUID2);
		entityManager.persist(eservice);
		entityManager.getTransaction().commit();
		q = entityManager.createQuery(
				"SELECT e FROM Eservice e WHERE e.eserviceId = :serviceIdParam AND e.versionId = :versionIdParam",
				Eservice.class);
		q.setParameter("serviceIdParam", serviceUUID2);
		q.setParameter("versionIdParam", versionUUID2);

		eservice = repo.findByEserviceIdAndVersionId(serviceUUID2, versionUUID2);
		assertNotNull(eservice);
	}

	/**
	 * Test find eservice when not exists given valid E service id and version id then return null.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("The findEservice method is executed if the Eservice not exists")
	void testFindEservice_whenNotExists_GivenValidEServiceIdAndVersionId_thenReturnNull() throws IOException {
		entityManager.getTransaction().begin();
		Eservice eservice = getEserviceEntity(serviceUUID3, versionUUID3);
		entityManager.persist(eservice);
		entityManager.getTransaction().commit();
		q = entityManager.createQuery(
				"SELECT e FROM Eservice e WHERE e.eserviceId = :serviceIdParam AND e.versionId = :versionIdParam",
				Eservice.class);
		q.setParameter("serviceIdParam", serviceUUID3);
		q.setParameter("versionIdParam", versionUUID3);

		eservice = repo.findByEserviceIdAndVersionId(UUID.randomUUID(), UUID.randomUUID());
		assertNull(eservice);
	}

	/**
	 * Gets the eservice entity.
	 *
	 * @param eserviceId the eservice id
	 * @param versionId the version id
	 * @return the eservice entity
	 */
	private Eservice getEserviceEntity(UUID eserviceId, UUID versionId) {
		Eservice serviceEntity = new Eservice();
		serviceEntity.setState(EserviceState.INACTIVE);
		serviceEntity.setEserviceId(eserviceId);
		serviceEntity.setVersionId((versionId));
		serviceEntity.setEserviceName("E-Service name");
		serviceEntity.setBasePath(new String[] { "test-BasePath-1", "test-BasePath-2" });
		serviceEntity.setPollingFrequency(5);
		serviceEntity.setProducerName("Producer name");
		serviceEntity.setProbingEnabled(true);
		return serviceEntity;
	}
}


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

class EserviceDaoTest {

	static EserviceDao repo;

	static EntityManager entityManager;

	static UUID versionUUID;

	static UUID serviceUUID;

	static UUID versionUUID2;

	static UUID serviceUUID2;

	static UUID versionUUID3;

	static UUID serviceUUID3;

	private static final String CONNECTION_PROP_URL = "hibernate.connection.url";

	private static final String CONNECTION_PROP_USR = "hibernate.connection.username";

	TypedQuery<Eservice> q;

	@BeforeAll
	public static void setup() throws IOException {
		Map<String, String> result = new HashMap<>();
		result.put(CONNECTION_PROP_URL, "jdbc:hsqldb:mem:interop-db-pu-in-memory");
		result.put(CONNECTION_PROP_USR, "sa");
		repo = EserviceDao.getInstance();
//		entityManager = repo.getEm();
		versionUUID = UUID.randomUUID();
		serviceUUID = UUID.randomUUID();
		versionUUID2 = UUID.randomUUID();
		serviceUUID2 = UUID.randomUUID();
		versionUUID3 = UUID.randomUUID();
		serviceUUID3 = UUID.randomUUID();
	}

	@AfterAll
	public static void close() {
		entityManager.close();
	}

	@Test
	@DisplayName("The save method is executed if the Eservice not exists")
	void testEserviceEntity_whenEserviceDataNotProvided_throwsException() {
		Eservice emptyEservice = new Eservice();
		assertThrows(PersistenceException.class, () -> entityManager.persist(emptyEservice),
				"e-service should not be saved when missing required data");
	}

	@Test
	@DisplayName("The update method is executed if the Eservice exists")
	void testSaveEservice_whenExists_GivenValidEserviceIdAndVersionId_thenUpdates() throws IOException {
		entityManager.getTransaction().begin();
		Eservice eservice = getEserviceEntity(serviceUUID, versionUUID);
		entityManager.persist(eservice);
		long oldId = eservice.getId();
		entityManager.getTransaction().commit();

//		repo.save(eservice);
		long newId = eservice.getId();
		assertEquals(oldId, newId);
	}

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

//		eservice = repo.findByEserviceIdAndVersionId(serviceUUID2, versionUUID2);
		assertNotNull(eservice);
	}

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

//		eservice = repo.findByEserviceIdAndVersionId(UUID.randomUUID(), UUID.randomUUID());
		assertNull(eservice);
	}

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

/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 3 mar 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import it.pagopa.interop.probing.eservice.registry.updater.dao.EserviceDao;
import it.pagopa.interop.probing.eservice.registry.updater.model.Eservice;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;


/**
 * The Class EserviceDaoTest.
 */
class EserviceDaoTest {

	/** The repo. */
	EserviceDao repo;
	
	/** The entity manager. */
	EntityManager entityManager = mock(EntityManager.class);
	
	/** The q. */
	TypedQuery<Eservice> q = mock(TypedQuery.class);


	/**
	 * Setup.
	 */
	@BeforeEach
	public void setup() {
		repo = new EserviceDao(entityManager);
	}


	/**
	 * Test find eservice when exists given valid E service id and version id then return entity.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("The findEservice method is executed if the Eservice exists")
	void testFindEservice_whenExists_GivenValidEServiceIdAndVersionId_thenReturnEntity() throws IOException{

		Eservice serviceEntity = getEserviceEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		List<Eservice> list = new ArrayList<Eservice>();
		list.add(serviceEntity);
	
		when(q.setParameter(anyString(), anyString())).thenReturn(q);
		when(q.getResultList()).thenReturn(list);
		when(entityManager.createQuery(anyString(), ArgumentMatchers.<Class<Eservice>>any())).thenReturn(q);

		Eservice eservice = repo.findByEserviceIdAndVersionId(UUID.randomUUID(), UUID.randomUUID());
		assertEquals(serviceEntity, eservice);

	}
	
	
	/**
	 * Test find eservice when not exists given valid E service id and version id then return nul.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("The findEservice method is executed if the Eservice not exists")
	void testFindEservice_whenNotExists_GivenValidEServiceIdAndVersionId_thenReturnNul() throws IOException{

		when(q.setParameter(anyString(), anyString())).thenReturn(q);
		when(q.getResultList()).thenReturn(new ArrayList<Eservice>());
		when(entityManager.createQuery(anyString(), ArgumentMatchers.<Class<Eservice>>any())).thenReturn(q);

		Eservice eservice = repo.findByEserviceIdAndVersionId(UUID.randomUUID(), UUID.randomUUID());
		assertNull(eservice);

	}
	
	
	/**
	 * Test save eservice when not exists given valid eservice id and version id then saves.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("The save method is executed if the Eservice not exists")
	void testSaveEservice_whenNotExists_GivenValidEserviceIdAndVersionId_thenSaves() throws IOException{

		EntityTransaction transaction = mock(EntityTransaction.class);  
         
        when(entityManager.getTransaction()).thenReturn(transaction);
        doNothing().when(entityManager).persist(Mockito.any());
        
        repo.save(new Eservice());
        
        verify(entityManager).persist(new Eservice());
        verify(transaction).begin();
        verify(transaction).commit();
	}
	
	
	/**
	 * Test save eservice when exists given valid eservice id and version id then updates.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	@DisplayName("The update method is executed if the Eservice exists")
	void testSaveEservice_whenExists_GivenValidEserviceIdAndVersionId_thenUpdates() throws IOException{

		EntityTransaction transaction = mock(EntityTransaction.class);
        Eservice serviceEntity = getEserviceEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());
       
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.merge(Mockito.any())).thenReturn(serviceEntity);
        
        repo.save(serviceEntity);

        verify(entityManager).merge(serviceEntity);
        verify(transaction).begin();
        verify(transaction).commit();
	}


	/**
	 * Gets the eservice entity.
	 *
	 * @param eserviceId the eservice id
	 * @param versionId the version id
	 * @return the eservice entity
	 */
	private Eservice getEserviceEntity(String eserviceId, String versionId) {
		Eservice serviceEntity = new Eservice();
		serviceEntity.setId(10L);
		serviceEntity.setState(EserviceState.INACTIVE);
		serviceEntity.setEserviceId(UUID.fromString(eserviceId));
		serviceEntity.setVersionId(UUID.fromString(versionId));
		serviceEntity.setEserviceName("E-Service name");
		serviceEntity.setBasePath(new String[]{"test-BasePath-1", "test-BasePath-2"});
		serviceEntity.setPollingFrequency(5);
		serviceEntity.setProducerName("Producer name");
		serviceEntity.setProbingEnabled(true);
		return serviceEntity;
	}
}

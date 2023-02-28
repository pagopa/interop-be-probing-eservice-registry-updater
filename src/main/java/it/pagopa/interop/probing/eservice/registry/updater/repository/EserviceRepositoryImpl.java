/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 28 feb 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.repository
* File Name   : EserviceRepositoryImpl.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.repository;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.pagopa.interop.probing.eservice.registry.updater.model.Eservice;

/**
 * The Class EserviceRepositoryImpl.
 */
public class EserviceRepositoryImpl implements EserviceRepository{
	
	/** The instance. */
	private static EserviceRepositoryImpl instance;
	
	/** The em. */
	private static EntityManager em;
	
	/**
	 * Gets the single instance of EserviceRepositoryImpl.
	 *
	 * @return single instance of EserviceRepositoryImpl
	 */
	public static EserviceRepositoryImpl getInstance() {
		if (instance == null) {
			instance = new EserviceRepositoryImpl(em);
		}
		return instance;
	}
	 
    /**
     * Instantiates a new eservice repository impl.
     *
     * @param em the em
     */
    public EserviceRepositoryImpl(EntityManager em) {
    }

	/**
	 * Find by eservice id and version id.
	 *
	 * @param serviceIdParam the service id param
	 * @param versionIdParam the version id param
	 * @return the eservice
	 */
	@Override
	public Eservice findByEserviceIdAndVersionId(UUID serviceIdParam, UUID versionIdParam) {
		TypedQuery<Eservice> q = em.createQuery("SELECT e FROM Eservice e WHERE e.eservice_id = :serviceIdParam AND e.version_id = :versionIdParam", Eservice.class);
        q.setParameter("serviceIdParam", serviceIdParam);
        q.setParameter("versionIdParam", versionIdParam);
        return q.getSingleResult();
	}



	/**
	 * Save.
	 *
	 * @param eservice the eservice
	 * @return the eservice
	 */
	public Eservice save(Eservice eservice) {
		if (eservice.getId() == null) {
            em.persist(eservice);
        } else {
        	eservice = em.merge(eservice);
        }
        return eservice;
	}

}

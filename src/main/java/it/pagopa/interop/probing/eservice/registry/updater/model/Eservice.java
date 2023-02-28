/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 28 feb 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.model
* File Name   : Eservice.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.model;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import it.pagopa.interop.probing.eservice.registry.updater.util.EServiceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceType;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the eservices database table.
 *
 */
@Entity
@Table(name="eservices", uniqueConstraints=@UniqueConstraint(columnNames={"eservice_id", "version_id"}))
@TypeDef(name = "basePathType", typeClass = CustomStringArrayType.class)

/**
 * Gets the version id.
 *
 * @return the version id
 */

/**
 * Gets the version id.
 *
 * @return the version id
 */

/**
 * Gets the version id.
 *
 * @return the version id
 */

/**
 * Gets the version id.
 *
 * @return the version id
 */

/**
 * Gets the version id.
 *
 * @return the version id
 */
@Getter

/**
 * Sets the version id.
 *
 * @param versionId the new version id
 */

/**
 * Sets the version id.
 *
 * @param versionId the new version id
 */

/**
 * Sets the version id.
 *
 * @param versionId the new version id
 */

/**
 * Sets the version id.
 *
 * @param versionId the new version id
 */

/**
 * Sets the version id.
 *
 * @param versionId the new version id
 */
@Setter
public class Eservice implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant DEFAULT_POLLING_START_TIME. */
	private static final Time  DEFAULT_POLLING_START_TIME = Time.valueOf("00:00:00");
	
	/** The Constant DEFAULT_POLLING_END_TIME. */
	private static final Time  DEFAULT_POLLING_END_TIME = Time.valueOf("23:59:59");
	
	/** The Constant DEFAULT_POLLING_FREQUENCY. */
	private static final Integer  DEFAULT_POLLING_FREQUENCY = 5;
	
	/** The Constant DEFAULT_PROBING_ENABLED. */
	private static final boolean  DEFAULT_PROBING_ENABLED = true;

	/** The id. */
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "eservice_sequence")
    @SequenceGenerator(name = "eservice_sequence",sequenceName = "eservice_sequence", allocationSize = 1)
    private Long id;

    /** The base path. */
    @Column(name="base_path", columnDefinition = "text[]")
    @Type(type = "basePathType")
    private String[] basePath;

    /** The eservice name. */
    @Column(name="eservice_name")
    private String eserviceName;

    /** The eservice type. */
    @Column(name="eservice_type")
    @Enumerated(EnumType.STRING)
    private EserviceType eserviceType;

    /** The eservice id. */
    @NotNull
    @Column(name="eservice_id")
    private UUID eserviceId;

    /** The last request. */
    @Column(name="last_request")
    private Timestamp lastRequest;

    /** The response received. */
    @Column(name="response_received")
    private Timestamp responseReceived;

    /** The polling end time. */
    @Column(name="polling_end_time")
    private Time pollingEndTime = DEFAULT_POLLING_END_TIME;

    /** The polling frequency. */
    @Column(name="polling_frequency", columnDefinition = "integer default 5")
    private Integer pollingFrequency = DEFAULT_POLLING_FREQUENCY;

    /** The polling start time. */
    @Column(name="polling_start_time")
    private Time pollingStartTime = DEFAULT_POLLING_START_TIME;

    /** The probing enabled. */
    @Column(name="probing_enabled")
    private boolean probingEnabled = DEFAULT_PROBING_ENABLED;

    /** The producer name. */
    @Column(name="producer_name")
    private String producerName;

    /** The state. */
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar default 'ACTIVE'")
    private EServiceState state = EServiceState.ACTIVE;

    /** The version id. */
    @NotNull
    @Column(name="version_id")
    private UUID versionId;
}

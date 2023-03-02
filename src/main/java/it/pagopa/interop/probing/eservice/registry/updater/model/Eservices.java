/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 2 mar 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.model
* File Name   : Eservices.java
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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import it.pagopa.interop.probing.eservice.registry.updater.util.EServiceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the eservices database table.
 *
 */
@Entity
@Table(name="eservices", uniqueConstraints=@UniqueConstraint(columnNames={"eservice_id", "version_id"}))
@TypeDef(name = "basePathType", typeClass = CustomStringArrayType.class)

/**
 * To string.
 *
 * @return the java.lang. string
 */

/**
 * To string.
 *
 * @return the java.lang. string
 */

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Instantiates a new eservice.
 */

/**
 * Instantiates a new eservices.
 */

/**
 * Instantiates a new eservices.
 */
@NoArgsConstructor
public class Eservices implements Serializable {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "eservice_sequence")
    @SequenceGenerator(name = "eservice_sequence",sequenceName = "eservice_sequence", allocationSize = 1)
    @Column(updatable = false)
    private Long id;

    /** The base path. */
    @NotNull
    @Size(max = 255)
    @Basic(optional = false)
    @Column(name="base_path", columnDefinition = "varchar[]")
    @Type(type = "basePathType")
    private String[] basePath;

    /** The eservice name. */
    @NotBlank
    @Size(max = 255)
    @Column(name="eservice_name")
    private String eserviceName;

    /** The eservice type. */
    @NotNull
    @Column(name="eservice_type")
    @Enumerated(EnumType.STRING)
    private EserviceType eserviceType;

    /** The eservice id. */
    @NotNull
    @Column(name="eservice_id")
    private UUID eserviceId;

    /** The last request. */
    @Column(name="last_request", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime lastRequest;

    /** The response received. */
    @Column(name="response_received", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime responseReceived;

    /** The polling end time. */
    @NotNull
    @Column(name="polling_end_time", columnDefinition = "TIMESTAMP with time zone")
    private OffsetDateTime pollingEndTime = OffsetDateTime.of(
			OffsetDateTime.now().getYear(),
			OffsetDateTime.now().getMonthValue(),
			OffsetDateTime.now().getDayOfMonth(),
			23,
			59,
			59,
			0,
			ZoneOffset.UTC
			);

    /** The polling frequency. */
    @NotNull
    @Column(name="polling_frequency", columnDefinition = "integer default 5")
    private Integer pollingFrequency = 5;

    /** The polling start time. */
    @NotNull
    @Column(name="polling_start_time", columnDefinition = "TIMESTAMP with time zone")
    private OffsetDateTime pollingStartTime = OffsetDateTime.of(
			OffsetDateTime.now().getYear(),
			OffsetDateTime.now().getMonthValue(),
			OffsetDateTime.now().getDayOfMonth(),
			0,
			0,
			0,
			0,
			ZoneOffset.UTC
			);

    /** The probing enabled. */
    @NotNull
    @Column(name="probing_enabled")
    private boolean probingEnabled;

    /** The producer name. */
    @NotBlank
    @Size(max = 255)
    @Column(name="producer_name")
    private String producerName;

    /** The state. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private EServiceState state;

    /** The version id. */
    @NotNull
    @Column(name="version_id")
    private UUID versionId;
}

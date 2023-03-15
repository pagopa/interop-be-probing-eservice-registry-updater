/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 7 mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
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
import java.time.OffsetDateTime;
import java.time.OffsetTime;
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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceTechnology;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class Eservice.
 */
@Entity
@Table(name = "eservices", uniqueConstraints = @UniqueConstraint(columnNames = { "eservice_id", "version_id" }))
@TypeDef(name = "basePathType", typeClass = CustomStringArrayType.class)

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Instantiates a new eservice.
 */
@NoArgsConstructor
public class Eservice implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2324588888685572113L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eservice_sequence")
	@SequenceGenerator(name = "eservice_sequence", sequenceName = "eservice_sequence", allocationSize = 1)
	@Column(updatable = false)
	private Long id;

	/** The base path. */
	@NotNull
	@Size(max = 255)
	@Basic(optional = false)
	@Column(name = "base_path", columnDefinition = "varchar(255) array")
	@Type(type = "basePathType")
	private String[] basePath;

	/** The eservice name. */
	@NotBlank
	@Size(max = 255)
	@Column(name = "eservice_name")
	private String eserviceName;

	/** The technology. */
	@NotNull
	@Column(name = "eservice_technology")
	@Enumerated(EnumType.STRING)
	private EserviceTechnology technology;

	/** The eservice id. */
	@NotNull
	@Column(name = "eservice_id")
	private UUID eserviceId;

	@NotNull
	@Column(name = "polling_end_time", columnDefinition = "TIME with time zone")
	private OffsetTime pollingEndTime = OffsetTime.of(23, 59, 0, 0, ZoneOffset.UTC);

	/** The polling frequency. */
	@NotNull
	@Min(1)
	@Column(name = "polling_frequency", columnDefinition = "integer default 5")
	private Integer pollingFrequency = 5;

	@NotNull
	@Column(name = "polling_start_time", columnDefinition = "TIME with time zone")
	private OffsetTime pollingStartTime = OffsetTime.of(0, 0, 0, 0, ZoneOffset.UTC);

	/** The probing enabled. */
	@NotNull
	@Column(name = "probing_enabled")
	private boolean probingEnabled;

	/** The producer name. */
	@NotBlank
	@Size(max = 255)
	@Column(name = "producer_name")
	private String producerName;

	/** The state. */
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "state")
	private EserviceState state;

	/** The version id. */
	@NotNull
	@Column(name = "version_id")
	private UUID versionId;
}

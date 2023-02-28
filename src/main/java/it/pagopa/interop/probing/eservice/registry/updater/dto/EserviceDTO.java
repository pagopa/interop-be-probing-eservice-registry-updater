/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 28 feb 2023
* Author      : dxc technology
* Project Name: interop-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.dto
* File Name   : EserviceDTO.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import it.pagopa.interop.probing.eservice.registry.updater.annotations.ValidateEnum;
import it.pagopa.interop.probing.eservice.registry.updater.util.EServiceState;
import it.pagopa.interop.probing.eservice.registry.updater.util.EserviceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Gets the producer name.
 *
 * @return the producer name
 */

/**
 * Gets the producer name.
 *
 * @return the producer name
 */

/**
 * Gets the producer name.
 *
 * @return the producer name
 */
@Getter

/**
 * Sets the producer name.
 *
 * @param producerName the new producer name
 */

/**
 * Sets the producer name.
 *
 * @param producerName the new producer name
 */

/**
 * Sets the producer name.
 *
 * @param producerName the new producer name
 */
@Setter

/**
 * Instantiates a new eservice DTO.
 */

/**
 * Instantiates a new eservice DTO.
 */

/**
 * Instantiates a new eservice DTO.
 */
@NoArgsConstructor

/**
 * Instantiates a new eservice DTO.
 *
 * @param name the name
 * @param eserviceId the eservice id
 * @param versionId the version id
 * @param type the type
 * @param state the state
 * @param basePath the base path
 * @param producerName the producer name
 */

/**
 * Instantiates a new eservice DTO.
 *
 * @param name the name
 * @param eserviceId the eservice id
 * @param versionId the version id
 * @param type the type
 * @param state the state
 * @param basePath the base path
 * @param producerName the producer name
 */
@AllArgsConstructor
public class EserviceDTO {

		/** The name. */
		@NotBlank(message = "must not be blank")
		private String name;
		
		/** The eservice id. */
		@NotBlank(message = "must not be blank")
		@Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$", message="must respect the UUID regex")
		private String eserviceId;
		
		/** The version id. */
		@Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$", message="must respect the UUID regex")
		@NotBlank(message = "versionId must not be blank")
		private String versionId;
		
		/** The type. */
		@NotBlank(message = "must not be blank")
		@ValidateEnum(
			     enumClass = EserviceType.class,
			     message = "value must be present in the EserviceType enum"
			 )
		private String type;
		
		/** The state. */
		@NotBlank(message = "must not be blank")
		@ValidateEnum(
			     enumClass = EServiceState.class,
			     message = "value must be present in the EServiceState enum"
			 )
		private String state;
		
		/** The base path. */
		@NotEmpty(message = "list cannot be empty")
		private String[] basePath;
		
		/** The producer name. */
		@NotBlank(message = "must not be blank")
		private String producerName;	
		
}

/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 6 Mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.annotations
* File Name   : ValidateStringArraySize.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import it.pagopa.interop.probing.eservice.registry.updater.annotations.validator.StringArrayValidator;

/**
 * The Interface ValidateStringArray.
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringArrayValidator.class)
public @interface ValidateStringArraySize {

	/**
	 * Max size.
	 *
	 * @return the int
	 */
	int maxSize();

	/**
	 * Message.
	 *
	 * @return the string
	 */
	String message() default "One of the strings of the array is more than {maxSize} characters long";

	/**
	 * Groups.
	 *
	 * @return the class[]
	 */
	Class<?>[] groups() default {};

	/**
	 * Payload.
	 *
	 * @return the class<? extends payload>[]
	 */
	Class<? extends Payload>[] payload() default {};
}

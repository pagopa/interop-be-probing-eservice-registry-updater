/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 7 mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.annotations.validator
* File Name   : StringArrayValidator.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.annotations.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import it.pagopa.interop.probing.eservice.registry.updater.annotations.ValidateStringArraySize;

/**
 * The Class StringArrayValidator.
 */
public class StringArrayValidator implements ConstraintValidator<ValidateStringArraySize, String[]> {

	/** The max size. */
	private int maxSize;

	/**
	 * Initialize.
	 *
	 * @param constraintAnnotation the constraint annotation
	 */
	@Override
	public void initialize(ValidateStringArraySize constraintAnnotation) {
		maxSize = constraintAnnotation.maxSize();
	}

	/**
	 * Checks if is valid.
	 *
	 * @param array the array
	 * @param context the context
	 * @return true, if is valid
	 */
	@Override
	public boolean isValid(String[] array, ConstraintValidatorContext context) {
		if (Objects.nonNull(array)) {
			for (String s : array) {
				if (s.length() > maxSize) {
					return false;
				}
			}
		}
		return true;
	}

}
/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 7 mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.annotations.validator
* File Name   : EnumValidator.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import it.pagopa.interop.probing.eservice.registry.updater.annotations.ValidateEnum;

/**
 * The Class EnumValidator.
 */
public class EnumValidator implements ConstraintValidator<ValidateEnum, String> {

	/** The value list. */
	private List<String> valueList;

	/**
	 * Initialize.
	 *
	 * @param constraintAnnotation the constraint annotation
	 */
	@Override
	public void initialize(ValidateEnum constraintAnnotation) {
		valueList = new ArrayList<>();
		Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();

		@SuppressWarnings("rawtypes")
		Enum[] enumValArr = enumClass.getEnumConstants();
		for (Enum<?> enumVal : enumValArr) {
			valueList.add(enumVal.toString().toUpperCase());
		}
	}

	/**
	 * Checks if is valid.
	 *
	 * @param value the value
	 * @param context the context
	 * @return true, if is valid
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return Objects.nonNull(value) ? valueList.contains(value.toUpperCase()) : true;
	}

}
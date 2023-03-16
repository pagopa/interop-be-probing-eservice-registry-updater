package it.pagopa.interop.probing.eservice.registry.updater.annotations.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import it.pagopa.interop.probing.eservice.registry.updater.annotations.ValidateEnum;

public class EnumValidator implements ConstraintValidator<ValidateEnum, String> {

	private List<String> valueList;

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

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return Objects.nonNull(value) ? valueList.contains(value.toUpperCase()) : true;
	}

}
package it.pagopa.interop.probing.eservice.registry.updater.annotations.validator;

import java.util.Objects;

import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import it.pagopa.interop.probing.eservice.registry.updater.annotations.ValidateStringArraySize;

public class StringArrayValidator implements ConstraintValidator<ValidateStringArraySize, String[]> {

	private int maxSize;

	@Override
	public void initialize(ValidateStringArraySize constraintAnnotation) {
		maxSize = constraintAnnotation.maxSize();
	}

	@Override
	public boolean isValid(String[] array, ConstraintValidatorContext context) {
		return Objects.isNull(array) || Stream.of(array).noneMatch(s -> s.length() > maxSize);
	}

}

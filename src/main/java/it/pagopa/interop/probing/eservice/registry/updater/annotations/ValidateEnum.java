
package it.pagopa.interop.probing.eservice.registry.updater.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import it.pagopa.interop.probing.eservice.registry.updater.annotations.validator.EnumValidator;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidateEnum {

	Class<? extends Enum<?>> enumClass();

	String message() default "value must be present in the enum {enumClass}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

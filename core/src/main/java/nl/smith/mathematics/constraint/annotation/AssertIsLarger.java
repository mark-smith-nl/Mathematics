package nl.smith.mathematics.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.smith.mathematics.constraint.AbstractNumberAssertionValidator;
import nl.smith.mathematics.constraint.AssertionValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ArgumentConstraint
public @interface AssertIsLarger {
	String errorFormat() default "The assertion that the number {} is larger then {} is not correct";

	/** Class containing the method to check the assertion */
	Class<? extends AssertionValidator<?>> assertionValidatorClass() default AbstractNumberAssertionValidator.class;

	/** Name of the method which validates the value(s) */
	String validationMethod() default "assertIsLarger";

	String value();

}

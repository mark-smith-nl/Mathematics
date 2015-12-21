package nl.smith.mathematics.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.smith.mathematics.number.NumberOperations;
import nl.smith.mathematics.number.RationalNumber;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface MethodConstraint {
	/**
	 * Class containing the method to convert the string value to the desired
	 * instance
	 */
	Class<? extends NumberOperations<?>> numberType() default RationalNumber.class;

	/** Name of the method which converts a string value to the desired instance */
	String valueOf() default "valueOf";
}

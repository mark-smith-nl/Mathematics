package nl.smith.mathematics.functions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FunctionProperty {
	String simplePropertyName() default "";

	boolean prefixWithCanonicalClassName() default false;

	boolean nullable() default false;
}

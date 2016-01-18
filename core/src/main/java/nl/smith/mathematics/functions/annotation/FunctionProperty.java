package nl.smith.mathematics.functions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.smith.mathematics.functions.AbstractFunction;

/**
 * Annotation to be used on instance properties.<br>
 * These properties can be set during instantiation see: {@link AbstractFunction#setFunctionProperties}.<br>
 * The property should be a private, final instance property.<br>
 * If the class has one or more properties annotated with {@link FunctionProperty} with {@link FunctionProperty#nullable()} set to false, the class should have an associated property file
 * (or system property) unless the property is initialized in the class with a non null value.<br>
 * This file should have the following name: ClassName.properties and should be in the same package as the class.<br>
 * Property values are retrieve from this property file using using the key constructed from the settings as defined in the annotation<br>
 * <br>
 * 
 * @author mark
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FunctionProperty {
	/** If not set or blank the simplePropertyName is set to the property name of the annotated property */
	String simplePropertyName() default "";

	/** If set to true the simplePropertyName is prepended with the canonical class name of the enclosing class */
	boolean prefixWithCanonicalClassName() default false;

	/** If set to true the key does not have to be present in the property file and the property value will be set to null */
	boolean nullable() default false;
}

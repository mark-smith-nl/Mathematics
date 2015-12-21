package nl.smith.mathematics.functions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * Annotation marks a class as containing mathematical functions.<br>
 * Annotation is meant to be used for annotating abstract classes but can be placed on concrete classes as well.<br>
 * Concrete classes extending this abstract class inherit the {@link MathematicalFunctionContainer} from the abstract class.<br>
 * Do <b>not</b> place this annotation on interfaces because inheritance of the annotation will <b>not</b> work.<br>
 * Concrete classes will be instantiated during package scanning which scans on <b>declared</b> annotations.<br>
 * Because of this it is useless to annotate the {@link MathematicalFunctionContainer} annotation with the {@link Component} annotation.<br>
 * In order to be detected during class path scanning, classes inheriting the {@link MathematicalFunctionContainer} annotation should be explicitly annotated with the
 * {@link Component} annotation.<br>
 * <br>
 * Classes containing the {@link MathematicalFunctionContainer} annotation may contain {@link #MathematicalFunction()} annotated method(s). <br>
 * 
 * @author M. Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface MathematicalFunctionContainer {
	/** Name of the function container */
	String name();

	/** Description of the function container */
	String description();
}

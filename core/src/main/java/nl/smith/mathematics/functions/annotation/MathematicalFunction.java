package nl.smith.mathematics.functions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marks a method as a being mathematical function<br>
 * In order to be active the containing class should be annotated with {@link MathematicalFunctionContainer}<br>
 * The method should be a public instance method
 * 
 * @author M. Smith
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface MathematicalFunction {

	/**
	 * Alias of the method name (function)
	 * If the alias is blank i.e. an empty string or null the code using this annotation will use <br>
	 * the name of the method on which the annotation has been set
	 **/
	String methodNameAlias() default "";

	/** Description of the function */
	String description() default "No description specified";

}

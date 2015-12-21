package nl.smith.mathematics.constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.smith.mathematics.constraint.annotation.AssertIsBetween;
import nl.smith.mathematics.constraint.annotation.AssertIsInteger;
import nl.smith.mathematics.constraint.annotation.AssertIsLarger;
import nl.smith.mathematics.constraint.annotation.AssertIsPositive;
import nl.smith.mathematics.constraint.annotation.AssertIsSmaller;
import nl.smith.mathematics.constraint.annotation.MethodConstraint;
import nl.smith.mathematics.number.NumberOperations;
import nl.smith.mathematics.number.RationalNumber;
import nl.smith.mathematics.utility.Resettable;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class using singleton instance.
 * Verifies if method arguments comply to certain constraints. See {@link MethodConstraint}
 * 
 * @author mark
 *
 */
public class AbstractNumberConstraintParameterChecker implements Resettable {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNumberConstraintParameterChecker.class);

	private static AbstractNumberConstraintParameterChecker singletonInstance;

	/** Annotation-{@link AssertionValidator} map. These validators are singleton instances with assertionValidation methods. */
	private Map<Annotation, AssertionValidator<?>> annotationValidatorMap = new HashMap<>();

	/** Annotation-AssertionValidator instance method map. */
	private Map<Annotation, Method> annotationMethodMap = new HashMap<>();

	/** This class is an utility class and can not be (directly) instantiated */
	private AbstractNumberConstraintParameterChecker() {

	}

	public static AbstractNumberConstraintParameterChecker getInstance() {
		if (singletonInstance == null) {
			singletonInstance = new AbstractNumberConstraintParameterChecker();
		}

		return singletonInstance;
	}

	public void allAssertionsApplyToAllArguments(Method numberFactoryMethod, Annotation[][] annotationConstraints, Object... arguments) {
		for (int i = 0; i < arguments.length; i++) {
			LOGGER.debug("Checking parameter {} constrains [{}]...", i, annotationConstraints[i].length);
			allAssertionsApplyToArgument(annotationConstraints[i], arguments[i], numberFactoryMethod);
			LOGGER.debug("Checking parameter {} constrains [{}]...valid", i, annotationConstraints[i].length);
		}
	}

	/**
	 * Method to validate that all constraint apply to an argument
	 * 
	 * @param annotationConstraints
	 *            Array of constraints (i.e. annotations)
	 * @param argument
	 *            The argument to be validated
	 *            This argument can be of any kind: Array, Collection, {@link NumberOperations}, {@link RationalNumber} or even String
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	protected void allAssertionsApplyToArgument(Annotation[] argumentAnnotations, Object argument, Method numberFactoryMethod) {
		for (int i = 0; i < argumentAnnotations.length; i++) {
			LOGGER.debug("Checking constraint {}...", i, i);
			assertionAppliesToArgument(argumentAnnotations[i], argument, numberFactoryMethod);
			LOGGER.debug("Checking constraint {}...valid", i, i);
		}
	}

	/**
	 * Method to validate that a constraint applies to an argument
	 * 
	 * @param annotationConstraint
	 *            The constraint (i.e. annotation)
	 * @param argument
	 *            The argument to be validated
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	protected void assertionAppliesToArgument(Annotation argumentAnnotation, Object argument, Method numberFactoryMethod) {
		Class<? extends Annotation> annotationType = argumentAnnotation.annotationType();

		LOGGER.debug("Checking whether the argument applies to constraint of type {}", annotationType);

		// Annotation properties
		String errorFormat;
		List<NumberOperations<?>> extraArguments = new ArrayList<>();
		Class<? extends AssertionValidator<?>> assertionValidatorClass;
		String validationMethodName;

		if (AssertIsInteger.class == annotationType) {
			AssertIsInteger annotation = (AssertIsInteger) argumentAnnotation;
			errorFormat = annotation.errorFormat();
			assertionValidatorClass = annotation.assertionValidatorClass();
			validationMethodName = annotation.validationMethod();
		} else if (AssertIsPositive.class == annotationType) {
			AssertIsPositive annotation = (AssertIsPositive) argumentAnnotation;
			errorFormat = annotation.errorFormat();
			assertionValidatorClass = annotation.assertionValidatorClass();
			validationMethodName = annotation.validationMethod();
		} else if (AssertIsLarger.class == annotationType) {
			AssertIsLarger annotation = (AssertIsLarger) argumentAnnotation;
			errorFormat = annotation.errorFormat();
			assertionValidatorClass = annotation.assertionValidatorClass();
			validationMethodName = annotation.validationMethod();
			extraArguments.add(getNumberFromFactory(numberFactoryMethod, annotation.value()));
		} else if (AssertIsSmaller.class == annotationType) {
			AssertIsSmaller annotation = (AssertIsSmaller) argumentAnnotation;
			errorFormat = annotation.errorFormat();
			assertionValidatorClass = annotation.assertionValidatorClass();
			validationMethodName = annotation.validationMethod();
			extraArguments.add(getNumberFromFactory(numberFactoryMethod, annotation.value()));
		} else if (AssertIsBetween.class == annotationType) {
			AssertIsBetween annotation = (AssertIsBetween) argumentAnnotation;
			errorFormat = annotation.errorFormat();
			assertionValidatorClass = annotation.assertionValidatorClass();
			validationMethodName = annotation.validationMethod();
			extraArguments.add(getNumberFromFactory(numberFactoryMethod, annotation.floor()));
			extraArguments.add(getNumberFromFactory(numberFactoryMethod, annotation.ceiling()));
		} else {
			LOGGER.debug("Unhandled constraint for annotation {}. Returning true", annotationType);
			return;
		}

		AssertionValidator<?> validator = getValidator(argumentAnnotation, assertionValidatorClass);
		Method method = getValidationMethod(argumentAnnotation, assertionValidatorClass, validationMethodName, extraArguments.size());

		validationMethodInvoker(validator, method, argument, errorFormat, extraArguments);
	}

	protected NumberOperations<?> getNumberFromFactory(Method numberFactoryMethod, String value) {
		try {
			if (numberFactoryMethod == null) {
				throw new IllegalStateException(String.format(
						"\nNo number factory specified to convert the string value '%s' to an %s instance.\nDid you add the annotation %s to your method?", value,
						NumberOperations.class.getCanonicalName(), MethodConstraint.class.getCanonicalName()));
			}
			return (NumberOperations<?>) numberFactoryMethod.invoke(null, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalStateException(String.format("Number factory method %s could not parse %s", numberFactoryMethod.getName(), value));
		}
	}

	private AssertionValidator<?> getValidator(Annotation annotation, Class<? extends AssertionValidator<?>> assertionValidatorClass) {
		AssertionValidator<?> validator = annotationValidatorMap.get(annotation);
		if (validator == null) {
			LOGGER.debug("Trying to retrieve validator instance {})", assertionValidatorClass.getSimpleName());

			Method method;
			try {
				method = assertionValidatorClass.getDeclaredMethod("getInstance");
				validator = (AssertionValidator<?>) method.invoke(null);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO error message
				throw new IllegalStateException();

			}

			annotationValidatorMap.put(annotation, validator);
		}

		return (validator);
	}

	/**
	 * Method to retrieve a specific method in this class. <br>
	 * The methods to be retrieved all have:
	 * <ul>
	 * <li>a {@link NumberOperations}.class as first argument (i.e. the value to be validated)</li>
	 * <li>a string.class as second argument (used to create an error message)</li>
	 * <li>and zero or more {@link NumberOperations}.class arguments (for comparing values)</li>
	 * </ul>
	 * <br>
	 * The return type of the retrieved method should be <b>boolean</b> <br>
	 * If the method can be retrieved it is put in a map with the annotation type as key
	 * 
	 * @param annotationType
	 * @param methodName
	 * @param numberOfExtraNumberArguments
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private Method getValidationMethod(Annotation annotation, Class<? extends AssertionValidator<?>> assertionValidatorClass, String validationMethodName,
			int numberOfExtraNumberArguments) {
		Method method = annotationMethodMap.get(annotation);
		if (method == null) {
			// First argument of the validator method is always the value(s) to be validated
			// The second argument is always the errorformat
			Class<?>[] methodArgumentType = new Class<?>[2 + numberOfExtraNumberArguments];
			int i = 0;
			methodArgumentType[i++] = NumberOperations.class;
			methodArgumentType[i++] = String.class;
			for (int j = 0; j < numberOfExtraNumberArguments; j++) {
				methodArgumentType[i++] = NumberOperations.class;
			}

			LOGGER.debug("Trying to retrieve method {}.{}({})",
					new Object[] { assertionValidatorClass.getSimpleName(), validationMethodName, StringUtils.join(methodArgumentType, ", ") });

			try {
				method = assertionValidatorClass.getDeclaredMethod(validationMethodName, methodArgumentType);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new IllegalStateException(String.format("\nMethod %s.%s(%s) could not be found or accessed.\nPlease add the method.",
						assertionValidatorClass.getSimpleName(), validationMethodName, StringUtils.join(methodArgumentType, ", ")));
			}

			if (!method.getReturnType().equals(void.class)) {
				throw new IllegalStateException(String.format("\nMethod %s.%s(%s) does not return void.", AbstractNumberAssertionValidator.class.getSimpleName(),
						validationMethodName, StringUtils.join(methodArgumentType, ", ")));
			}

			annotationMethodMap.put(annotation, method);
		}

		return method;
	}

	private static Collection<NumberOperations<?>> getNumberCollection(Collection<?> elements) {
		Collection<NumberOperations<?>> numberCollection = new ArrayList<>();
		if (elements.isEmpty()) {
			throw new IllegalStateException("Empty collection supplied");
		}

		for (Object element : elements) {
			if (element == null) {
				throw new IllegalArgumentException("Unspecified element encountered in list");
			}

			if (!(element instanceof NumberOperations)) {
				throw new IllegalArgumentException(String.format("Wrong type of argument. Actual: %s. Expected: %s", element.getClass().getCanonicalName(),
						NumberOperations.class.getCanonicalName()));
			}
			numberCollection.add((NumberOperations<?>) element);

		}

		return numberCollection;
	}

	/**
	 * Method invokes the specified method with the supplied arguments directly if the argument is not a collection or array.
	 * If the argument is a collection or array a wrapper method is invoked using the supplied method
	 * Returns boolean the value of the invoked method
	 * 
	 * If the argument is <b>null</b> not a Number, Collection or array a
	 * 
	 * @param method
	 * @param argument
	 * @param errorFormat
	 * @param extraArguments
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private static void validationMethodInvoker(AssertionValidator<?> validator, Method method, Object argument, String errorFormat, List<NumberOperations<?>> extraArguments) {
		if (argument == null) {
			throw new IllegalStateException("Element not specified");
		}
		if (argument instanceof NumberOperations) {
			Object[] arguments = getJoinedArguments(argument, errorFormat, extraArguments);
			try {
				method.invoke(validator, arguments);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(String.format("Method %s.%s(%s) could not be invoked.", validator.getClass().getSimpleName(), method.getName(),
						StringUtils.join(arguments)));
			} catch (InvocationTargetException e) {
				LOGGER.info(e.getCause().getMessage());
				throw (IllegalArgumentException) e.getCause();
			}
		} else if (argument instanceof Collection) {
			validationMethodInvokerUsingAggregatedArguments(validator, (Collection<?>) argument, errorFormat, method, extraArguments);
		} else if (argument.getClass().isArray()) {
			validationMethodInvokerUsingAggregatedArguments(validator, Arrays.asList((Object[]) argument), errorFormat, method, extraArguments);
		} else {
			throw new IllegalArgumentException(String.format("The argument is not an instance of %s, %s or array. Actual type %s", NumberOperations.class.getSimpleName(),
					Collection.class.getSimpleName(), argument.getClass().getCanonicalName()));
		}
	}

	private static Object[] getJoinedArguments(Object argument, String errorFormat, List<NumberOperations<?>> extraArgument) {
		Object[] arguments = new Object[2 + extraArgument.size()];
		arguments[0] = argument;
		arguments[1] = errorFormat;
		for (int i = 0; i < extraArgument.size(); i++) {
			arguments[2 + i] = extraArgument.get(i);
		}

		return arguments;
	}

	/**
	 * Aggregated
	 * 
	 * @param elements
	 * @param errorFormat
	 * @param method
	 * @param extraArgument
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private static void validationMethodInvokerUsingAggregatedArguments(Object validator, Collection<?> elements, String errorFormat, Method method,
			List<NumberOperations<?>> extraArgument) {
		for (NumberOperations<?> element : getNumberCollection(elements)) {
			Object[] arguments = getJoinedArguments(element, errorFormat, extraArgument);
			try {
				method.invoke(validator, arguments);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalStateException(String.format("Method %s.%s(%s) could not be invoked.", AbstractNumberConstraintParameterChecker.class.getSimpleName(),
						method.getName(), StringUtils.join(arguments)));
			}
		}
	}

	@Override
	public void reset() {
		// For testing: Don't forget to reset the instance fields
		this.annotationValidatorMap = new HashMap<>();
		this.annotationMethodMap = new HashMap<>();
	}

}

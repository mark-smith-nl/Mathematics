package nl.smith.mathematics;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import nl.smith.mathematics.number.NumberOperations;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to uniquely identify an instance method within a class The method
 * should be public and returning an instance (or array)of type
 * {@link NumberOperations}, int or double. If the method accepts arguments they
 * should also be of type {@link NumberOperations}, int or double.
 * 
 * @author M. Smith
 */
public class NumericMethodSignature {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(NumericMethodSignature.class);

	@SuppressWarnings("rawtypes")
	private static final Class<NumberOperations> NUMBER = NumberOperations.class;

	private final Class<?> declaringClass;

	private final String methodName;

	private final String methodAlias;

	private final Class<?> returnType;

	private final Class<?>[] parameterTypes;

	private final int numberOfArguments;

	private final String signature;

	private static class NumericMethodSignatureException extends Exception {
		private static final long serialVersionUID = 1L;

		private NumericMethodSignatureException(String message) {
			super(message);
		}
	}

	private NumericMethodSignature(Class<?> declaringClass, String methodName, String methodAlias, Class<?> returnType, Class<?>[] parameterTypes,
			int numberOfArguments) {
		super();
		this.declaringClass = declaringClass;
		this.methodName = methodName;
		this.methodAlias = methodAlias;
		this.returnType = returnType;
		this.parameterTypes = parameterTypes;

		this.numberOfArguments = numberOfArguments;

		String methodArgumentsAsString;
		if (numberOfArguments >= 0) {
			methodArgumentsAsString = StringUtils.repeat("number", ", ", numberOfArguments);
		} else {
			methodArgumentsAsString = "number" + " ...";
		}

		if (StringUtils.isBlank(methodAlias)) {
			signature = declaringClass.getCanonicalName() + "." + methodName + "(" + methodArgumentsAsString + ")";
		} else {
			signature = methodAlias + "(" + methodArgumentsAsString + ")";
		}
	}

	public static NumericMethodSignature getNumericMethodSignature(Method method) throws NumericMethodSignatureException {
		return getNumericMethodSignature(method, null);
	}

	public static NumericMethodSignature getNumericMethodSignature(Method method, String methodAlias) throws NumericMethodSignatureException {
		if (method == null) {
			throw new IllegalArgumentException("No method specified");
		}

		List<String> errors = new ArrayList<>();
		Class<?> declaringClass = method.getDeclaringClass();

		String methodName = method.getName();

		Class<?> returnType = method.getReturnType();

		Class<?>[] parameterTypes = method.getParameterTypes();
		int modifiers = method.getModifiers();

		if (!Modifier.isPublic(modifiers)) {
			errors.add(String.format("The method %s.%s should be a public method", declaringClass.getCanonicalName(), methodName));
		}

		if (Modifier.isStatic(modifiers)) {
			errors.add(String.format("The method %s.%s should be an instance method", declaringClass.getCanonicalName(), methodName));
		}

		boolean isArray = returnType.isArray();
		if (isArray) {
			returnType = returnType.getComponentType();
		}

		if (!isNumericType(returnType)) {
			errors.add(String.format("The method's %sreturn type (%s) is not derived from %s", isArray ? "array " : "", returnType, NUMBER.getCanonicalName(),
					methodName));
		}

		int numberOfArguments = getNumberOfMethodArguments(parameterTypes, errors);

		if (!errors.isEmpty()) {
			errors.add(0, String.format("\nNumeric method signature for method %s can not be defined", methodName));
			throw new NumericMethodSignatureException(StringUtils.join(errors, "\n"));
		}

		return new NumericMethodSignature(declaringClass, methodName, methodAlias, returnType, parameterTypes, numberOfArguments);
	}

	private static int getNumberOfMethodArguments(Class<?>[] parameterTypes, List<String> errors) {
		int numberOfArguments = parameterTypes.length;

		Class<?> parameterType;
		if (parameterTypes.length == 1) {
			parameterType = parameterTypes[0];
			boolean isArray = parameterType.isArray();
			if (isArray) {
				parameterType = parameterType.getComponentType();
				numberOfArguments = -1;
			}
			if (!isNumericType(parameterType)) {
				errors.add(String.format("The first method %sargument (%s) is not of type %s", isArray ? "array " : "", parameterType.getCanonicalName(),
						NUMBER.getCanonicalName()));
			}

		} else {
			for (int i = 0; i < parameterTypes.length; i++) {
				parameterType = parameterTypes[i];
				if (!isNumericType(parameterType)) {
					errors.add(String.format("The %d'th method argument (%s) is not of type %s", i, parameterType.getCanonicalName(), NUMBER.getCanonicalName()));
				}
			}
		}

		return numberOfArguments;
	}

	private static boolean isNumericType(Class<?> clazz) {
		return NUMBER.isAssignableFrom(clazz) || clazz == int.class || clazz == double.class;
	}

	public Class<?> getDeclaringClass() {
		return declaringClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getMethodAlias() {
		return methodAlias;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public int getNumberOfArguments() {
		return numberOfArguments;
	}

	public String getSignature() {
		return signature;
	}

	@Override
	public String toString() {
		return signature;
	}

}

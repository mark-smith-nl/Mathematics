package nl.smith.mathematics.utility;

import static nl.smith.mathematics.utility.ErrorMessages.CAN_NOT_LOAD_CLASS;
import static nl.smith.mathematics.utility.ErrorMessages.CAN_NOT_OPEN_RESOURCE;
import static nl.smith.mathematics.utility.ErrorMessages.CONVERSION_OF_STRING_TO_SPECIFIED_OBJECT_NOT_SUPPORTED;
import static nl.smith.mathematics.utility.ErrorMessages.METHOD_ARGUMENT_CAN_NOT_BE_NULL;
import static nl.smith.mathematics.utility.ErrorMessages.REQUIRED_CONSTRUCTOR_NOT_RETRIEVED;
import static nl.smith.mathematics.utility.ErrorMessages.REQUIRED_METHOD_NOT_RETRIEVED;
import static nl.smith.mathematics.utility.ErrorMessages.STRING_NUMBER_PARSE_ERROR;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import nl.smith.mathematics.functions.AbstractFunction;
import nl.smith.mathematics.number.NumberOperations;

import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to create a context associated with a class extending {@link AbstractFunction}. <br>
 * The context is retrieved from a property file which resides in the same package as the class associated with the context.<br>
 * An empty context for a class is returned if no property file can be found.<br>
 * If a property file is found it is used to create a Properties instance.<br>
 * If a System property with the same name as defined in the Properties instance is defined, this property is added to the Properties instance.<br>
 * The values in the Properties instance are parsed to result a an instance of the (in the file) defined class.<br>
 * <br>
 * The following syntax is used to define a property value/property type pair in a property file:<br>
 * <br>
 * fullyQualifiedClassName.propertName=value<br>
 * fullyQualifiedClassName.propertName.type=classnameOfThePropertyInTheEnclosingClass<br>
 * <br>
 * The specification of the type is optional. If not specified, a String type in parsing the property value.<br>
 * <br>
 * Example:<br>
 * <br>
 * nl.smith.mathematics.functions.rational.GoniometricFunctionsImpl.angleType= DEG<br>
 * nl.smith.mathematics.functions.rational.GoniometricFunctionsImpl.angleType.type=nl.smith.mathematics.functions.AngleType<br>
 * <br>
 * Accepted types: Enum, Boolean, String, Integer, BigInteger, BigDecimal, {@link DateTime} and types extending {@link NumberOperations} are accepted.<br>
 * <br>
 * In case the specified type extends {@link NumberOperations} the static public method valueOf(String) is used to parse the value.<br>
 */
public class FunctionContextHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(FunctionContextHelper.class);

	private static final String TYPE_SPECIFIER_STRING = ".type";
	private static final String VALUE_SPECIFIER_STRING = ".value";

	private static final String REGEX_DATE = "[0-3]\\d\\-[0-1]\\d-\\d{4}";
	private static final String REGEX_DATE_TIME = REGEX_DATE + " [0-2]\\d:[0-5]\\d:[0-5]\\d";

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy");
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");

	public static final String NUMBER_FACTORY_METHOD_NAME = "valueOf";

	private static final Map<Class<?>, Method> INSTANCE_FACTORY_METHODS = new HashMap<>();

	private static final Map<Class<?>, Constructor<?>> constructorsWithStringArgument = new HashMap<>();

	/** This class is an utility class and can not be instantiated */
	private FunctionContextHelper() {
		throw new IllegalAccessError();
	}

	public static Map<String, Object> makeFunctionContext(Class<?> functionClass) {
		return makeFunctionContext(functionClass, new HashMap<String, Class<?>>(), System.getProperties(), new Properties(), false);
	}

	public static Map<String, Object> makeFunctionContext(Class<?> functionClass, Map<String, Class<?>> providedPropertyTypes, Properties providedProperties, Properties unusedProperties,
			boolean onlyRetrieMapForProvidedPropertyTypes) {
		if (functionClass == null || providedPropertyTypes == null || unusedProperties == null) {
			METHOD_ARGUMENT_CAN_NOT_BE_NULL.throwUncheckedException(IllegalArgumentException.class, "functionClass/providedPropertyTypes/unusedProperties");
		}

		if (providedPropertyTypes.isEmpty()) {

		}

		Map<String, Object> functionContext = new HashMap<>();

		Map<String, Class<?>> propertyTypes = new HashMap<>(providedPropertyTypes);
		Map<String, String> propertyValues = new HashMap<>();

		Properties properties = makePropertiesForClass(functionClass, providedProperties);

		for (Entry<Object, Object> entry : properties.entrySet()) {
			String propertyName = (String) entry.getKey();
			String propertyValue = (String) entry.getValue();
			// Note: propertyName either end with ".value" or ".type"
			if (propertyName.endsWith(VALUE_SPECIFIER_STRING)) {
				propertyName = StringUtils.substringBefore(propertyName, VALUE_SPECIFIER_STRING);
				if (onlyRetrieMapForProvidedPropertyTypes) {
					if (providedPropertyTypes.containsKey(propertyName)) {
						propertyValues.put(propertyName, propertyValue);
					} else {
						unusedProperties.put(entry.getKey(), propertyValue);
					}
				} else {
					propertyValues.put(propertyName, propertyValue);
				}

			} else {
				propertyName = StringUtils.substringBefore(propertyName, TYPE_SPECIFIER_STRING);
				Class<?> propertyType = null;
				if (onlyRetrieMapForProvidedPropertyTypes) {
					if (providedPropertyTypes.containsKey(propertyName)) {
						propertyType = providedPropertyTypes.get(propertyName);
						propertyTypes.put(propertyName, propertyType);
					} else {
						unusedProperties.put(propertyName, (String) entry.getValue());
					}
				} else {
					try {
						propertyType = Class.forName(propertyValue);
						propertyTypes.put(propertyName, propertyType);
					} catch (ClassNotFoundException e) {
						CAN_NOT_LOAD_CLASS.throwUncheckedException(IllegalStateException.class, "propertyType");
					}
				}

			}
		}

		for (Entry<String, String> contextObject : propertyValues.entrySet()) {
			String propertyName = contextObject.getKey();
			Class<?> propertyType = propertyTypes.get(propertyName);
			if (propertyType == null) {
				propertyType = String.class;
			}

			String propertyStringValue = contextObject.getValue();
			functionContext.put(propertyName, stringToObject(propertyType, propertyStringValue));
		}

		return functionContext;
	}

	/**
	 * Method to return a properties object with keys and values of type String.<br>
	 * Keys follow the structure &lt;name&gt;.value or &lt;name&gt;.type
	 * 
	 * @param clazz
	 *            The class the properties are associated with
	 * @return A properties object
	 */
	private static Properties makePropertiesForClass(Class<?> clazz, Properties providedProperties) {
		Properties properties = new Properties();

		String resourceName = clazz.getSimpleName() + ".properties";
		String resourcePath = clazz.getCanonicalName().replaceAll("\\.", "/") + ".properties";
		InputStream resource = clazz.getResourceAsStream(resourceName);

		if (resource != null) {
			LOGGER.info("Load properties from {}", resourcePath);
			try {
				properties.load(resource);
				Enumeration<?> propertyNames = properties.propertyNames();
				while (propertyNames.hasMoreElements()) {
					String propertyName = (String) propertyNames.nextElement();
					if (propertyName.endsWith(VALUE_SPECIFIER_STRING) || propertyName.endsWith(TYPE_SPECIFIER_STRING)) {
						// Only retrieve properties with propetryName <name>.value or <name>.type
						LOGGER.debug("Property {}={}", propertyName, properties.getProperty(propertyName));
					} else {
						properties.remove(propertyName);
					}
				}
			} catch (IOException e) {
				CAN_NOT_OPEN_RESOURCE.throwUncheckedException(IllegalStateException.class, resourcePath);
			}
		} else {
			LOGGER.info("Resource '{}' not found.\nEmpty context will be created.", resourcePath);
		}

		addProvidedProperties(properties, providedProperties);

		return properties;
	}

	private static void addProvidedProperties(Properties properties, Properties providedProperties) {
		LOGGER.info("Adding system properties");

		Set<Entry<Object, Object>> entrySet = providedProperties.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (key instanceof String && value instanceof String) {
				String propertyName = (String) key;
				if (propertyName.endsWith(VALUE_SPECIFIER_STRING) || propertyName.endsWith(TYPE_SPECIFIER_STRING)) {
					// Only retrieve properties with propetryName <name>.value or <name>.type
					String stringValue = (String) value;
					String previousValue = (String) properties.setProperty(propertyName, stringValue);
					if (previousValue == null) {
						LOGGER.debug("Property {}={}", propertyName, stringValue);
					} else {
						LOGGER.info("Property {} is overriden.\n Old value: {}.\nNew value: {}", new Object[] { propertyName, previousValue, stringValue });

					}
				}
			}
		}
	}

	/**
	 * Method to convert a string value to a class instance
	 * 
	 * @param propertyType
	 *            the <b>not null</b> class off the returned instance
	 * @param propertyStringValue
	 *            the string value used to create the instance
	 * @return instance of the specified type (propertyType)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T stringToObject(Class<T> propertyType, String propertyStringValue) {
		if (propertyType == null) {
			METHOD_ARGUMENT_CAN_NOT_BE_NULL.throwUncheckedException(IllegalArgumentException.class, "propertyType");
		}

		if (propertyStringValue == null) {
			METHOD_ARGUMENT_CAN_NOT_BE_NULL.throwUncheckedException(IllegalArgumentException.class, "propertyStringValue");
		}

		T instance = null;

		if (propertyType.equals(DateTime.class)) {
			DateTimeFormatter dtf = null;
			if (Pattern.matches(REGEX_DATE, propertyStringValue)) {
				dtf = DATE_FORMATTER;
			} else if (Pattern.matches(REGEX_DATE_TIME, propertyStringValue)) {
				dtf = DATE_TIME_FORMATTER;
			} else {
				// TODO Error message
				System.out.println("Fout");
			}
			instance = (T) dtf.parseDateTime(propertyStringValue);
		} else if (propertyType.equals(LocalDate.class)) {
			DateTimeFormatter dtf = null;
			if (Pattern.matches(REGEX_DATE, propertyStringValue)) {
				dtf = DATE_FORMATTER;
			} else {
				// TODO Error message
				System.out.println("Fout");
			}
			instance = (T) dtf.parseLocalDate(propertyStringValue);
		} else if (propertyType.isEnum() || propertyType.equals(Boolean.class) || propertyType.equals(Integer.class) || propertyType.equals(Long.class) || propertyType.equals(Double.class)
				|| NumberOperations.class.isAssignableFrom(propertyType)) {
			// Construct an instance using the static factory method valueOf(String) in the specified class
			Method instanceFactoryMethod = getInstanceFactoryMethod(propertyType);
			try {
				instance = (T) instanceFactoryMethod.invoke(null, propertyStringValue);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				STRING_NUMBER_PARSE_ERROR.throwUncheckedException(IllegalArgumentException.class, e, propertyStringValue, propertyType.getCanonicalName());
			}
		} else if (propertyType.equals(String.class) || propertyType.equals(BigInteger.class) || propertyType.equals(BigDecimal.class)) {
			// Construct an instance using the constructor(String) in the specified class
			try {
				instance = (T) getConstructorForTypeWithStringArgument(propertyType).newInstance(propertyStringValue);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				STRING_NUMBER_PARSE_ERROR.throwUncheckedException(IllegalArgumentException.class, e, propertyStringValue, propertyType.getCanonicalName());
			}
		} else {
			CONVERSION_OF_STRING_TO_SPECIFIED_OBJECT_NOT_SUPPORTED.throwUncheckedException(IllegalClassException.class, propertyType.getCanonicalName());
		}

		return instance;

	}

	private static Method getInstanceFactoryMethod(Class<?> clazz) {
		Method instanceFactoryMethod = INSTANCE_FACTORY_METHODS.get(clazz);

		if (instanceFactoryMethod == null) {
			try {
				instanceFactoryMethod = clazz.getDeclaredMethod(NUMBER_FACTORY_METHOD_NAME, String.class);
				if (!Modifier.isStatic(instanceFactoryMethod.getModifiers()) && !Modifier.isPublic(instanceFactoryMethod.getModifiers())) {
					throw new IllegalStateException(String.format("Missing public static method %s.%s(%s)", clazz.getCanonicalName(), NUMBER_FACTORY_METHOD_NAME,
							String.class.getCanonicalName()));
				}
			} catch (NoSuchMethodException | SecurityException e) {
				REQUIRED_METHOD_NOT_RETRIEVED.throwUncheckedException(IllegalStateException.class, e, "public static", clazz.getCanonicalName(), clazz.getCanonicalName(),
						NUMBER_FACTORY_METHOD_NAME, String.class.getCanonicalName());
			}
			INSTANCE_FACTORY_METHODS.put(clazz, instanceFactoryMethod);
		}

		return instanceFactoryMethod;
	}

	private static <T> Constructor<T> getConstructorForTypeWithStringArgument(Class<T> clazz) {
		@SuppressWarnings("unchecked")
		Constructor<T> constructorWithStringArgument = (Constructor<T>) constructorsWithStringArgument.get(clazz);
		if (constructorWithStringArgument == null) {
			try {
				constructorWithStringArgument = clazz.getConstructor(String.class);
				constructorsWithStringArgument.put(clazz, constructorWithStringArgument);
			} catch (NoSuchMethodException | SecurityException e) {
				REQUIRED_CONSTRUCTOR_NOT_RETRIEVED.throwUncheckedException(IllegalArgumentException.class, e, clazz.getCanonicalName(), String.class.getCanonicalName());
			}
		}

		return constructorWithStringArgument;
	}

}

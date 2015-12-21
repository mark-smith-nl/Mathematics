package nl.smith.mathematics.utility;

import java.io.IOException;
import java.io.InputStream;
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

import nl.smith.mathematics.number.NumberOperations;

import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionContextHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(FunctionContextHelper.class);

	private static final String TYPE_SPECIFIER_STRING = ".type";

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd/MM/yyyy");

	private static final String NUMBER_FACTORY_METHOD_NAME = "valueOf";

	private static final Map<Class<? extends NumberOperations<?>>, Method> NUMBER_FACTORY_METHODS = new HashMap<>();

	/** This class is an utility class and can not be instantiated */
	private FunctionContextHelper() {
		throw new IllegalAccessError();
	}

	public static Map<String, Object> makeFunctionContext(Class<?> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("No clazz provided");
		}

		Map<String, Object> functionContext = new HashMap<>();

		Map<String, Class<?>> propertyTypes = new HashMap<>();
		Map<String, String> propertyValues = new HashMap<>();

		Properties properties = makePropertiesForClass(clazz);

		for (Entry<Object, Object> entry : properties.entrySet()) {
			String propertyName = (String) entry.getKey();
			if (propertyName.endsWith(TYPE_SPECIFIER_STRING)) {
				propertyName = StringUtils.substringBefore(propertyName, TYPE_SPECIFIER_STRING);
				Class<?> propertyType;
				try {
					propertyType = Class.forName((String) entry.getValue());
				} catch (ClassNotFoundException e) {
					// TODO
					throw new IllegalArgumentException(e);
				}
				propertyTypes.put(StringUtils.substringBefore(propertyName, TYPE_SPECIFIER_STRING), propertyType);
			} else {
				propertyValues.put(propertyName, (String) entry.getValue());
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

	private static Properties makePropertiesForClass(Class<?> clazz) {
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
					LOGGER.info("Property {}={}", propertyName, properties.getProperty(propertyName));
					String systemProperty = System.getProperty(propertyName);
					if (systemProperty != null) {
						LOGGER.warn("Property {} is overriden", propertyName);
						properties.setProperty(propertyName, systemProperty);
						LOGGER.warn("Property {}={}", propertyName, properties.getProperty(propertyName));
					}
				}
			} catch (IOException e) {
				throw new IllegalStateException(String.format("Can not open resource '%s'", resourcePath), e);
			}
		} else {
			LOGGER.warn("Resource '{}' not found", resourcePath);
			throw new IllegalStateException(String.format("Resource '%s' not found", resourcePath));
		}

		return properties;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> T stringToObject(Class<T> propertyType, String propertyStringValue) {
		if (propertyType.isEnum())
			return (T) Enum.valueOf((Class<Enum>) propertyType, propertyStringValue);

		if (propertyType.equals(Boolean.class))
			return (T) new Boolean(propertyStringValue);

		if (propertyType.equals(String.class))
			return (T) new String(propertyStringValue);

		if (propertyType.equals(Integer.class))
			return (T) new Integer(propertyStringValue);

		if (propertyType.equals(BigInteger.class))
			return (T) new BigInteger(propertyStringValue);

		if (propertyType.equals(BigDecimal.class))
			return (T) new BigDecimal(propertyStringValue);

		if (propertyType.equals(DateTime.class)) {
			String[] propertyStringValues = propertyStringValue.split(":");
			DateTimeFormatter dtf = DATE_TIME_FORMATTER;
			if (propertyStringValues.length > 1) {
				dtf = DateTimeFormat.forPattern(propertyStringValues[1]);
			}
			return (T) dtf.parseDateTime(propertyStringValues[0]);
		}

		if (NumberOperations.class.isAssignableFrom(propertyType)) {
			Class<NumberOperations<?>> clazz = (Class<NumberOperations<?>>) propertyType;
			Method numberFactory = getNumberFactoryMethod(clazz);

			try {
				return (T) numberFactory.invoke(null, propertyStringValue);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalStateException(String.format("Can not invoke %s.%s(\"%s\")", propertyType.getCanonicalName(), NUMBER_FACTORY_METHOD_NAME,
						propertyStringValue), e);
			}
		}

		throw new IllegalClassException("No converter specified to convert a sting into an instance of " + propertyType.getCanonicalName());
	}

	private static Method getNumberFactoryMethod(Class<? extends NumberOperations<?>> clazz) {
		Method numberFactoryMethod = NUMBER_FACTORY_METHODS.get(clazz);

		if (numberFactoryMethod == null) {
			try {
				numberFactoryMethod = clazz.getDeclaredMethod(NUMBER_FACTORY_METHOD_NAME, String.class);
				if (!Modifier.isStatic(numberFactoryMethod.getModifiers()) && !Modifier.isPublic(numberFactoryMethod.getModifiers())) {
					throw new IllegalStateException(String.format("Missing public static method %s.%s(%s)", clazz.getCanonicalName(),
							NUMBER_FACTORY_METHOD_NAME, String.class.getCanonicalName()));
				}
			} catch (NoSuchMethodException | SecurityException e) {
				throw new IllegalStateException(String.format("Missing public static method %s.%s(%s)", clazz.getCanonicalName(), NUMBER_FACTORY_METHOD_NAME,
						String.class.getCanonicalName()), e);
			}
			NUMBER_FACTORY_METHODS.put(clazz, numberFactoryMethod);
		}

		return numberFactoryMethod;
	}

}

package nl.smith.mathematics.utility;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

//import nl.smith.develop.TestPropertyReader;
import nl.smith.mathematics.number.RationalNumber;

import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for storing application settings as objects.
 * <p>
 * This class is only instantiated once, the resulting singleton is associated with the class as a static property. <br>
 * It can only be accessed by a static method {@link ApplicationProperties#getApplicationProperties()}. <br>
 * These instances are intended to contain only data.
 * 
 * @author Mark Smith
 */
public class ApplicationProperties implements Resettable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationProperties.class);

	private static ApplicationProperties applicationProperties;

	private static final ClassLoader CLASS_LOADER = ApplicationProperties.class.getClassLoader();

	private Path pathToExternalPropertyFile;

	private static final Class<String> DEFAULT_PROPERTY_TYPE = String.class;

	/** Unmodifiable application context property map */
	private Map<String, Object> PROPERTIES = new HashMap<>();

	public static ApplicationProperties getApplicationProperties() {
		return getApplicationProperties(null);
	}

	/**
	 * Method that returns an instance of {@link ApplicationProperties} associated
	 * to the {@link ApplicationProperties} class. <br>
	 * If none is associated it is created and returned.
	 * 
	 * @return An instance of {@link ApplicationProperties} associated to the {@link ApplicationProperties} class.
	 */
	public static ApplicationProperties getApplicationProperties(Path pathToExternalPropertyFile) {
		if (applicationProperties == null) {
			LOGGER.info("Creating {} singleton instance", ApplicationProperties.class.getCanonicalName());
			applicationProperties = new ApplicationProperties();
			applicationProperties.pathToExternalPropertyFile = pathToExternalPropertyFile;
			applicationProperties.initialize();
		}

		return applicationProperties;
	}

	public Path getPathToExternalPropertyFile() {
		return pathToExternalPropertyFile;
	}

	public <T> T getObjectProperty(Class<T> propertyType, Class<?> clazz, String propertyName) {
		String namespace = clazz == null ? "" : clazz.getCanonicalName();

		return getObjectProperty(propertyType, namespace, propertyName);
	}

	public <T> T getObjectProperty(Class<T> propertyType, String namespace, String propertyName) {
		String fullyQualifiedPropertyName = (StringUtils.isBlank(namespace) ? "" : namespace + ".") + propertyName;

		return getObjectProperty(propertyType, fullyQualifiedPropertyName);
	}

	/**
	 * Method to get a property as an object derived from the specified system
	 * (String) property.
	 * Uses a default value if none is specified.
	 * 
	 * @param propertyType
	 *            . Return type of the property
	 * @param namespace
	 *            The namespace of the property.
	 *            The fully qualified name in conjunction with the name of the
	 *            property is used as a qualifier.
	 * @param propertyName
	 *            The (simple) name of the property
	 * @return The property as an object derived from the specified system
	 *         (String) property.
	 */
	public <T> T getObjectProperty(Class<T> propertyType, String fullyQualifiedPropertyName) {
		@SuppressWarnings("unchecked")
		T property = (T) PROPERTIES.get(fullyQualifiedPropertyName);
		if (property == null) {
			throw new IllegalStateException(String.format("No default value specified for property with fully qualified name %s and class %s", fullyQualifiedPropertyName,
					propertyType.getCanonicalName()));
		}

		LOGGER.info("\nProperty: {}\nObject value: {}\n", new Object[] { fullyQualifiedPropertyName, property });

		return property;
	}

	public String getProperty(Class<?> clazz, String propertyName) {
		String namespace = clazz == null ? "" : clazz.getCanonicalName();

		return getProperty(namespace, propertyName);
	}

	public String getProperty(String namespace, String propertyName) {
		String fullyQualifiedPropertyName = (StringUtils.isBlank(namespace) ? "" : namespace + ".") + propertyName;

		return getProperty(fullyQualifiedPropertyName);
	}

	public String getProperty(String fullyQualifiedPropertyName) {
		return getObjectProperty(DEFAULT_PROPERTY_TYPE, fullyQualifiedPropertyName);
	}

	@Override
	public void reset() {
		initialize();
	}

	@Override
	public String toString() {
		StringBuffer toString = new StringBuffer();
		ArrayList<String> propertyNames = new ArrayList<>(PROPERTIES.keySet());
		Collections.sort(propertyNames);
		for (String propertyName : propertyNames) {
			toString.append(propertyName + "=" + PROPERTIES.get(propertyName) + "\n");
		}
		return toString.toString();
	}

	/**
	 * Method that initializes the instance.
	 */
	private void initialize() {
		LOGGER.info("Initializing singleton instance {}", ApplicationProperties.class.getCanonicalName());
		Properties applicationProperties = new Properties();

		InputStream resource = CLASS_LOADER.getResourceAsStream(Constants.PROPERTY_FILE_RESOURCE_PATH);
		if (resource == null)
			throw new IllegalStateException(String.format("Missing resource %s.", Constants.PROPERTY_FILE_RESOURCE_PATH));

		// Loading resource property file(s).
		try {
			applicationProperties.load(resource);
			if (pathToExternalPropertyFile != null) {
				try (InputStream in = Files.newInputStream(pathToExternalPropertyFile)) {
					applicationProperties = new Properties(applicationProperties);
					applicationProperties.load(in);
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Properties systemProperties = System.getProperties();
		for (Object key : systemProperties.keySet()) {
			Object systemValue = systemProperties.get(key);
			applicationProperties.put(key, systemValue);
		}

		buildApplicationProperties(applicationProperties);

	}

	private void buildApplicationProperties(Properties applicationProperties) {
		Map<String, Object> properties = new HashMap<>();

		List<String> propertyNames = new ArrayList<>();

		Enumeration<?> names = applicationProperties.propertyNames();
		while (names.hasMoreElements()) {
			String propertyName = (String) names.nextElement();
			if (!propertyName.endsWith(".type"))
				propertyNames.add(propertyName);
		}

		for (String propertyName : propertyNames) {
			String propertyStringValue = applicationProperties.getProperty(propertyName);
			String propertyStringType = applicationProperties.getProperty(propertyName + ".type");
			Class<?> propertyType = DEFAULT_PROPERTY_TYPE;

			if (!StringUtils.isBlank(propertyStringType)) {
				try {
					propertyType = Class.forName(propertyStringType);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}

			properties.put(propertyName, stringToObject(propertyType, propertyStringValue, properties));
		}

		PROPERTIES = Collections.unmodifiableMap(properties);
	}

	@SuppressWarnings("unchecked")
	private static <T> T getProperty(Class<T> propertyType, Map<String, Object> properties) {
		for (Object value : properties.values()) {
			if (value.getClass().equals(propertyType)) {
				return (T) value;
			}
		}

		throw new IllegalStateException(String.format("There is no property of type %s defined or the property is defineded after it is used.", propertyType.getCanonicalName()));

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> T stringToObject(Class<T> propertyType, String propertyStringValue, Map<String, Object> context) {
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

		if (propertyType.equals(RationalNumber.class))
			return (T) RationalNumber.valueOf(propertyStringValue);

		if (propertyType.equals(DateTimeFormatter.class))
			return (T) DateTimeFormat.forPattern(propertyStringValue);

		if (propertyType.equals(DateTime.class)) {
			DateTimeFormatter dtf = getProperty(DateTimeFormatter.class, context);
			return (T) dtf.parseDateTime(propertyStringValue);
		}

		throw new IllegalClassException("No converter specified to convert a sting into an instance of " + propertyType.getCanonicalName());
	}

}

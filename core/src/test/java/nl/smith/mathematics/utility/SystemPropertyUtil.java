package nl.smith.mathematics.utility;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utillity class used in testing.
 * Class controles the modification of System properties
 * 
 * @author Mark Smith
 */
public class SystemPropertyUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemPropertyUtil.class);

	/**
	 * This class is an utility class and can not be (directly) instantiated
	 */
	private SystemPropertyUtil() {
		throw new IllegalAccessError();
	}

	public static void setSystemProperty(String fullyQualifiedPropertyName, String propertyValue) {
		System.setProperty(fullyQualifiedPropertyName, propertyValue);
		LOGGER.info("Set system property {} to {}", fullyQualifiedPropertyName, propertyValue);
	}

	public static void setSystemProperties(Properties properties) {
		setSystemProperties(properties, null);
	}

	public static void setSystemProperties(Properties properties, Resettable resettable) {
		for (String fullyQualifiedPropertyName : properties.stringPropertyNames()) {
			String propertyValue = properties.getProperty(fullyQualifiedPropertyName);
			setSystemProperty(fullyQualifiedPropertyName, propertyValue);
		}

		if (resettable != null) {
			LOGGER.info("Calling reset on {}", resettable.getClass().getCanonicalName());
			resettable.reset();
		}

	}

	public static void copyProperties(Properties from, Properties to) {
		to.clear();
		for (String propertyName : from.stringPropertyNames()) {
			to.setProperty(propertyName, from.getProperty(propertyName));
		}

		return;
	}

}

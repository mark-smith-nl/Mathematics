package nl.smith.mathematics.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore("This is a helper utility")
public class TestUtility {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestUtility.class);

	private static final ClassLoader classLoader = TestUtility.class.getClassLoader();

	/** This class is an utility class and can not be instantiated */
	private TestUtility() {

	}

	public static String getStringFromResource(String name) {
		return getStringFromResource(name, null);
	}

	public static String getStringFromResource(String name, Class<?> associatedClass) {
		String resourcePath = getFullyQualifiedResourceName(name, associatedClass);
		LOGGER.info("Using resource ' {}' ", resourcePath);
		try {
			URL resource = classLoader.getResource(resourcePath);
			if (resource == null) {
				throw new IOException();
			}
			return IOUtils.toString(resource);
		} catch (IOException e) {
			throw new IllegalStateException(String.format("Missing test resource '%s'", resourcePath));
		}
	}

	public static Properties getProperties(String name) {
		return getProperties(name, null);
	}

	public static Properties getProperties(String name, Class<?> associatedClass) {
		String resourcePath = getFullyQualifiedResourceName(name, associatedClass);
		LOGGER.info("Using resource ' {}' ", resourcePath);

		try {
			InputStream inStream = classLoader.getResourceAsStream(resourcePath);
			if (inStream == null) {
				throw new IOException();
			}
			Properties properties = new Properties();
			properties.load(inStream);
			return properties;
		} catch (IOException e) {
			throw new IllegalStateException(String.format("Missing test resource '%s'", resourcePath));
		}
	}

	private static String getFullyQualifiedResourceName(String name, Class<?> associatedClass) {
		String fullyQualifiedResourceName = name;
		if (associatedClass != null) {
			Package currentPackage = associatedClass.getPackage();
			String resourceDir = currentPackage.getName().replaceAll("\\.", "/");
			fullyQualifiedResourceName = resourceDir + "/" + fullyQualifiedResourceName;
		}

		return fullyQualifiedResourceName;
	}
}

package nl.smith.mathematics.utility;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.After;

/**
 * All test classes which change system properties should extend this class. To change a system property
 * 
 * @author mark
 *
 */
public class SystemPropertyChanger {
	private Set<String> addedSystemPropertyNames = new HashSet<>();

	@After
	public void after() {
		Properties properties = System.getProperties();
		for (String addedSystemProperty : addedSystemPropertyNames) {
			properties.remove(addedSystemProperty);
		}
	}

	protected void setSystemProperty(String key, String value) {
		addedSystemPropertyNames.add(key);

		System.setProperty(key, value);

	}
}

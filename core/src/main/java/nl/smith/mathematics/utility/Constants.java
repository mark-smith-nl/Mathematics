package nl.smith.mathematics.utility;

public class Constants {

	public static final String PROPERTY_FILE_SPECIFIER = "propertyFile";

	public static final String PROPERTY_FILE_RESOURCE_PATH = "system.properties";

	public static final String PROPERTY_DEFAULT_NAME = "system.properties";

	public static final String DATA_DIR_SPECIFIER = "dataDir";

	public static final String DATA_DB_NAME = "databaseName";

	public static final String LOGBACK_FILE_SPECIFIER = "logback.configurationFile";

	public static final String LOGBACK_FILE_RESOURCE_PATH = "logback.xml";

	public static final String LOGBACK_FILE_DEFAULT_NAME = "logback.xml";

	public static final String HELP_FILE_SPECIFIER = "helpFile";

	public static final String HELP_FILE_RESOURCE_PATH = "help.txt";

	public static final String HELP_FILE_DEFAULT_NAME = "help.txt";

	/** This class is an utility class and can not be instantiated */
	private Constants() {
		throw new IllegalAccessError();
	}

}

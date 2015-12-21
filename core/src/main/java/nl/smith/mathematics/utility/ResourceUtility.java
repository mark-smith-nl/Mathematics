package nl.smith.mathematics.utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceUtility {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtility.class);

	/** This class is an utility class and can not be instantiated */
	private ResourceUtility() {
		throw new IllegalAccessError();
	}

	public static Path getPathToExternaFile(String propertyName, String resourcePath, String defaultFilePath, boolean acceptMissingResource,
			boolean acceptMissingPropertySpecification) {
		String filePath = ApplicationProperties.getApplicationProperties().getProperty(propertyName);
		Path pathToExternalPropertyFile = null;

		if (filePath == null) {
			LOGGER.info("Property {} not provided as a property. File will not be extracted.", propertyName);
			if (acceptMissingPropertySpecification) {
				System.setProperty(propertyName, "");
				return getPathToExternaFile(propertyName, resourcePath, defaultFilePath, acceptMissingResource, false);
			}
		} else {
			if (StringUtils.isBlank(filePath)) {
				filePath = defaultFilePath;
			}

			if (StringUtils.isBlank(filePath)) {
				LOGGER.info("No file specified\nSpecify file by adding -D{}=<pathToFile> to the command line", propertyName);
			} else {
				Path path = Paths.get(filePath);
				if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
					pathToExternalPropertyFile = getPathFromExistingFile(pathToExternalPropertyFile, path);
				} else {
					pathToExternalPropertyFile = createFileFromResource(resourcePath, acceptMissingResource, pathToExternalPropertyFile, path);
				}
			}
		}

		return pathToExternalPropertyFile;
	}

	private static Path getPathFromExistingFile(Path pathToExternalPropertyFile, Path path) {
		Path pathToExtPropFile = pathToExternalPropertyFile;
		if (Files.isReadable(path)) {
			if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
				LOGGER.debug("File {} does exist, is readable and is a regularfile", path.toString());
				try {
					LOGGER.debug("Using {} as property file", path.toRealPath(LinkOption.NOFOLLOW_LINKS));
				} catch (IOException e) {
					// Exception will not be thrown
				}
				pathToExtPropFile = path;
			} else {
				LOGGER.warn("File {} does exist, is readable but is not a regularfile", path.toString());
			}
		} else {
			LOGGER.warn("File {} does exist but is not readable", path.toString());
		}
		return pathToExtPropFile;
	}

	private static Path createFileFromResource(String resourcePath, boolean acceptMissingResource, Path pathToExternalPropertyFile, Path path) {
		Path pathToExtPropFile = pathToExternalPropertyFile;
		LOGGER.info("File {} does not exists and will be created", path.toString());
		InputStream resource = Application.class.getClassLoader().getResourceAsStream(resourcePath);
		if (resource == null) {
			if (acceptMissingResource) {
				LOGGER.info("The resource {} could not be found.", resourcePath);
			} else {
				throw new IllegalStateException(String.format("Essention resource %s is missing.", resourcePath));
			}
		} else {
			try {
				FileUtils.copyInputStreamToFile(resource, path.toFile());
				pathToExtPropFile = path;
			} catch (IOException e) {
				throw new RuntimeException(String.format("File %s can not be created", path.toString()));
			}
		}
		return pathToExtPropFile;
	}
}

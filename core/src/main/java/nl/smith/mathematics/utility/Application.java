package nl.smith.mathematics.utility;

import static nl.smith.mathematics.utility.Constants.HELP_FILE_DEFAULT_NAME;
import static nl.smith.mathematics.utility.Constants.HELP_FILE_RESOURCE_PATH;
import static nl.smith.mathematics.utility.Constants.HELP_FILE_SPECIFIER;
import static nl.smith.mathematics.utility.Constants.LOGBACK_FILE_DEFAULT_NAME;
import static nl.smith.mathematics.utility.Constants.LOGBACK_FILE_RESOURCE_PATH;
import static nl.smith.mathematics.utility.Constants.LOGBACK_FILE_SPECIFIER;
import static nl.smith.mathematics.utility.Constants.PROPERTY_DEFAULT_NAME;
import static nl.smith.mathematics.utility.Constants.PROPERTY_FILE_RESOURCE_PATH;
import static nl.smith.mathematics.utility.Constants.PROPERTY_FILE_SPECIFIER;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import nl.smith.mathematics.functions.annotation.MathematicalFunction;
import nl.smith.mathematics.functions.annotation.MathematicalFunctionContainer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	private final GenericApplicationContext context;

	private Application() {
		super();
		this.context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
	}

	public static void main(String[] args) {
		Path pathToExternalPropertyFile = getPathToExternalPropertyFile();
		ApplicationProperties.getApplicationProperties(pathToExternalPropertyFile);

		Application application = new Application();
		application.init();
	}

	private static Path getPathToExternalPropertyFile() {
		String filePath = System.getProperty(PROPERTY_FILE_SPECIFIER);
		if (filePath == null)
			return null;

		Path path = Paths.get(filePath);
		if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS))
			return null;

		return path;
	}

	private void init() {

		extractResources();

		Map<String, Object> functionContainers = context.getBeansWithAnnotation(MathematicalFunctionContainer.class);

		getMathematicalFunctions(functionContainers);

		LocalSessionFactoryBean localSessionFactoryBean = context.getBean(LocalSessionFactoryBean.class);
		Properties hibernateProperties = localSessionFactoryBean.getHibernateProperties();
		for (Entry<Object, Object> entry : hibernateProperties.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		System.out.println(hibernateProperties.getProperty("hibernate.hbm2ddl.auto"));
		System.out.println(localSessionFactoryBean);
		System.out.println(hibernateProperties);
	}

	private static List<Method> getMathematicalFunctions(Map<String, Object> functionContainers) {
		List<Method> mathematicalFunctions = new ArrayList<>();

		for (Entry<String, Object> entry : functionContainers.entrySet()) {
			Class<?> clazz = entry.getValue().getClass();
			LOGGER.info("Inspecting {} function container of type {}", entry.getValue(), clazz.getCanonicalName());
			Method[] declaredMethods = clazz.getDeclaredMethods();
			for (Method method : declaredMethods) {
				MathematicalFunction annotation = method.getAnnotation(MathematicalFunction.class);
				if (annotation != null) {
					int modifiers = method.getModifiers();
					if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
						String alias = annotation.methodNameAlias();
						alias = StringUtils.isBlank(alias) ? method.getName() : alias;
						LOGGER.info("Found method {}.{} method should be referenced as {}", new Object[] { clazz.getCanonicalName(), method.getName(), annotation.methodNameAlias() });
						LOGGER.info("Description: {} ", annotation.description());
						mathematicalFunctions.add(method);
					} else {
						throw new IllegalStateException(String.format("The %s annotated method %s.%s should be a public instance method",
								MathematicalFunction.class.getCanonicalName(), clazz.getCanonicalName(), method.getName()));
					}
				}
			}
		}

		return mathematicalFunctions;
	}

	private static void extractResources() {
		ResourceUtility.getPathToExternaFile(HELP_FILE_SPECIFIER, HELP_FILE_RESOURCE_PATH, HELP_FILE_DEFAULT_NAME, false, false);
		ResourceUtility.getPathToExternaFile(LOGBACK_FILE_SPECIFIER, LOGBACK_FILE_RESOURCE_PATH, LOGBACK_FILE_DEFAULT_NAME, false, false);
		ResourceUtility.getPathToExternaFile(PROPERTY_FILE_SPECIFIER, PROPERTY_FILE_RESOURCE_PATH, PROPERTY_DEFAULT_NAME, false, false);
	}

}

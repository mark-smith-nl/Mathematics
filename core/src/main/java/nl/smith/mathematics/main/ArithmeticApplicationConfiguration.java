package nl.smith.mathematics.main;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.smith.mathematics.factory.ArithmeticComponentResolver;
import nl.smith.mathematics.factory.ArithmeticExpressionFactory;
import nl.smith.mathematics.functions.AbstractFunction;
import nl.smith.mathematics.functions.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.invoker.MathematicalFunctionInvoker;
import nl.smith.mathematics.number.NumberOperations;
import nl.smith.mathematics.number.RationalNumber;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;

@Configuration
@ComponentScan(basePackages = { "nl.smith.mathematics.functions" }, nameGenerator = DefaultBeanNameGenerator.class)
public class ArithmeticApplicationConfiguration {

	static {
		// Remove this line for default configuration
		System.setProperty("logback.configurationFile", "logback.file.xml");
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ArithmeticApplicationConfiguration.class);

	private static final String NUMBER_CLASS_PROPERTY_NAME = "numberClass";

	private static final Class<? extends NumberOperations<?>> DEFAULT_NUMBERT_TYPE_CLASS = RationalNumber.class;

	@Autowired
	private ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	@Bean
	public Class<? extends NumberOperations<?>> getNumberClass() {

		Class<? extends NumberOperations<?>> numberClass = null;
		try {
			String numberClassString = System.getProperty(NUMBER_CLASS_PROPERTY_NAME);
			if (!StringUtils.isBlank(numberClassString)) {

				Class<?> clazz = Class.forName(numberClassString);
				if (!NumberOperations.class.isAssignableFrom(clazz)) {
					throw new IllegalStateException(String.format("The class %s is not assignable from %s. The specified class should be of type %1$s",
							NumberOperations.class.getCanonicalName(), clazz.getCanonicalName()));
				}

				if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
					throw new IllegalStateException(String.format("The class %s can not be an interface or an abstract class", clazz.getCanonicalName()));
				}

				numberClass = (Class<? extends NumberOperations<?>>) clazz;
			} else {
				LOGGER.info("No number type specified.\nSpecify number type as System.property ('{}')\nUsing default number type: {}",
						NUMBER_CLASS_PROPERTY_NAME, DEFAULT_NUMBERT_TYPE_CLASS.getCanonicalName());
				numberClass = DEFAULT_NUMBERT_TYPE_CLASS;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new IllegalStateException(e);
		}

		return numberClass;
	}

	@Bean
	public List<AbstractFunction> getMathematicalFunctionContainerClassInstances() {

		List<AbstractFunction> mathematicalFunctionContainerClassInstances = new ArrayList<>();

		// Retrieve all beans (indirectly-inherited) annotated with
		// MathematicalFunctionContainer from the context
		Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MathematicalFunctionContainer.class);

		// Iterate over the beans to see of they are of the right generic type
		// The generic type should be the specified numberType
		for (Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
			Type genericSuperclass = entry.getValue().getClass().getGenericSuperclass();
			if (genericSuperclass instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				if (actualTypeArguments.length == 1) {
					Type actualTypeArgument = actualTypeArguments[0];
					if (getNumberClass() == actualTypeArgument) {
						LOGGER.info("Found bean '{}' using the specified number type. ({})", entry.getKey(), actualTypeArgument);
						mathematicalFunctionContainerClassInstances.add((AbstractFunction) entry.getValue());
					} else {
						LOGGER.debug("Bean '{}' not using specified number type ({})", entry.getKey(), actualTypeArgument);
					}
				}

			}
		}

		return mathematicalFunctionContainerClassInstances;
	}

	@Bean
	public MathematicalFunctionInvoker getMathematicalFunctionInvoker() {
		return new MathematicalFunctionInvoker(getMathematicalFunctionContainerClassInstances(), getNumberClass());
	}

	@Bean
	ArithmeticComponentResolver getArithmeticComponentResolver() {
		return new ArithmeticComponentResolver(getMathematicalFunctionInvoker());
	}

	@Bean
	public ArithmeticExpressionFactory getArithmeticExpressionFactory() {
		return new ArithmeticExpressionFactory(getArithmeticComponentResolver());
	}

	public static void main(String[] args) {
		showLogInfo();
		println("Configure");
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(ArithmeticApplicationConfiguration.class);
		MathematicalFunctionInvoker mathematicalFunctionInvoker = context.getBean(MathematicalFunctionInvoker.class);
		println(StringUtils.repeat("-", 80));
		println("Start calculating");
		println(StringUtils.repeat("-", 80));
		println("Numbertype: " + mathematicalFunctionInvoker.getNumberClass());
		println(StringUtils.repeat("-", 80));
		println(StringUtils.repeat("-", 80));
		String methodNameAlias = "Faculteit";
		println("Testing: " + methodNameAlias);
		RationalNumber[] rationalNumbers = new RationalNumber[] { new RationalNumber(500, 100), new RationalNumber(5), new RationalNumber(-5),
				new RationalNumber(101), RationalNumber.valueOf("2.3{456}R") };
		for (RationalNumber rationalNumber : rationalNumbers) {
			try {
				println("Number argument: " + rationalNumber.toStringExact());
				println("Result: " + ((RationalNumber) mathematicalFunctionInvoker.invoke(methodNameAlias, rationalNumber)).toStringExact());
			} catch (IllegalStateException e) {
				println(e.getMessage());
			}
		}
		println(StringUtils.repeat("-", 80));

		println(StringUtils.repeat("-", 80));
		methodNameAlias = "doIt";
		println("Testing: " + methodNameAlias);
		try {
			RationalNumber rationalNumberOne = new RationalNumber(10, 3);
			RationalNumber rationalNumberTwo = new RationalNumber(12, 3);
			println("Number argument one: " + rationalNumberOne.toStringExact());
			println("Number argument two: " + rationalNumberTwo.toStringExact());
			println("Result: "
					+ ((RationalNumber) mathematicalFunctionInvoker.invoke(methodNameAlias, rationalNumberOne, rationalNumberTwo, "sss")).toStringExact());
		} catch (IllegalStateException e) {
			Throwable cause = e;
			while (cause.getCause() != null) {
				cause = cause.getCause();
			}
			println(cause.getMessage());
		}
		println(StringUtils.repeat("-", 80));

	}

	private static void showLogInfo() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		println(StringUtils.repeat("-", 80));
		println("Logback configuration: " + ConfigurationWatchListUtil.getMainWatchURL(loggerContext));
		ch.qos.logback.classic.Logger logger = loggerContext.getLogger("root");
		for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext();) {
			Appender<ILoggingEvent> appender = index.next();
			if (appender instanceof FileAppender) {
				FileAppender<ILoggingEvent> fileAppender = (FileAppender<ILoggingEvent>) appender;
				println("Logging goes to: " + fileAppender.getFile());
				return;
			}
		}
		println(StringUtils.repeat("-", 80));
	}

	private static void println(String message) {
		System.out.println(message);
		LOGGER.info(message);
	}
}

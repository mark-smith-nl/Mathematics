package nl.smith.mathematics.invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import javax.annotation.PostConstruct;

import nl.smith.mathematics.constraint.annotation.MethodConstraint;
import nl.smith.mathematics.functions.AbstractFunction;
import nl.smith.mathematics.functions.annotation.MathematicalFunction;
import nl.smith.mathematics.functions.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.number.NumberOperations;
import nl.smith.mathematics.utility.ErrorMessages;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which is responsible for invoking methods in associated proxy classes.<br>
 * It creates proxies for all beans of type {@link AbstractFunction} provided to
 * the constructor (Spring beans annotated with
 * {@link MathematicalFunctionContainer}).<br>
 * Methods in these beans which are annotated with {@link MethodConstraint} are
 * being intercepted before invocation.
 * 
 * @author mark
 *
 */
public class MathematicalFunctionInvoker {

	private static final Logger LOGGER = LoggerFactory.getLogger(MathematicalFunctionInvoker.class);

	/** Base class for numeric operations */
	private final Class<? extends NumberOperations<?>> numberClass;

	/**
	 * Collection of spring beans which are of type {@link AbstractFunction}
	 * which are (indirectly) annotated with @link MathematicalFunctionContainer
	 */
	private final List<AbstractFunction> beans;

	private final List<String> beanNames;

	private Map<Class<AbstractFunction>, AbstractFunction> proxies = new HashMap<>();

	private Map<SimpleEntry<String, Integer>, Method> availableMethodAliasMap = new HashMap<>();

	public MathematicalFunctionInvoker(List<AbstractFunction> beans, Class<? extends NumberOperations<?>> numberClass) {
		if (beans == null) {
			throw new IllegalArgumentException("\nNo mathematical function container beans specified");
		}

		if (numberClass == null) {
			throw new IllegalStateException("Number type class is null");
		}

		if (Modifier.isAbstract(numberClass.getModifiers())) {
			throw new IllegalArgumentException("\nThe numberClass specified is abstract");
		}

		LOGGER.info("Base number class for arirthmetic operations: {}", numberClass.getCanonicalName());
		this.numberClass = numberClass;

		this.beans = beans;
		beanNames = new ArrayList<String>();
	}

	@PostConstruct
	private void initialize() {
		LOGGER.debug("Initializing {}", this.getClass().getCanonicalName());

		proxies = makeProxiesForBeans(beans);

		availableMethodAliasMap = makeAvailableMethodAliasMap(proxies);
	}

	/**
	 * Method which returns a proxied class - method pair
	 * 
	 * @param methodAlias
	 * @param numberOfArguments
	 * @return
	 */
	public SimpleEntry<Object, Method> getProxyMethodPairForAlias(String methodAlias, int numberOfArguments) {
		Method method = availableMethodAliasMap.get(new SimpleEntry<String, Integer>(methodAlias, numberOfArguments));
		if (method == null) {
			Class<?> typeOfArgument = NumberOperations.class;
			throw new IllegalStateException(ErrorMessages.UNKNOWN_METHOD.getFormattedErrorMessage(methodAlias, numberOfArguments,
					typeOfArgument.getCanonicalName()));
		}
		Object proxy = proxies.get(method.getDeclaringClass());

		return new SimpleEntry<Object, Method>(proxy, method);
	}

	public Class<? extends NumberOperations<?>> getNumberClass() {
		return numberClass;
	}

	public List<String> getBeanNames() {
		return beanNames;
	}

	public Object invoke(String alias, Object... args) {
		Object value = null;
		checkArgumentTypes(args);
		try {
			SimpleEntry<Object, Method> proxyClassMethodPair = getProxyMethodPairForAlias(alias, args.length);
			value = proxyClassMethodPair.getValue().invoke(proxyClassMethodPair.getKey(), args);
		} catch (Exception e) {
			Throwable cause = getRootCause(e);
			throw new IllegalStateException(cause.getMessage(), e);
		}

		return value;
	}

	private Throwable getRootCause(Exception e) {
		Throwable cause = e;
		while (cause.getCause() != null) {
			cause = cause.getCause();
		}
		return cause;
	}

	private void checkArgumentTypes(Object[] args) {
		if (args == null || args.length == 0) {
			throw new IllegalStateException("No arguments specified for function");
		}
		for (Object arg : args) {
			if (arg == null) {
				throw new IllegalStateException("Null argument specified for function");
			} else {
				Class<?> argClass = arg.getClass();
				if (argClass != numberClass) {
					throw new IllegalStateException("Encountered argument of wrong type: " + argClass.getCanonicalName());
				}
			}
		}

	}

	/**
	 * Protected for test purposes
	 * 
	 * @param beans
	 */
	protected static <T> Map<Class<T>, T> makeProxiesForBeans(List<T> beans) {
		LOGGER.debug("Creating {} proxies for provided beans...", beans.size());

		Map<Class<T>, T> proxies = new HashMap<>();

		for (T bean : beans) {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) bean.getClass();
			LOGGER.debug("Investigating bean of type {}", clazz.getCanonicalName());

			T proxy = createProxyInstance(bean);
			if (proxies.put(clazz, proxy) != null) {
				throw new IllegalStateException(String.format("Duplicate bean detected. A bean of type %s has already been proxied", clazz.getCanonicalName()));
			}
		}

		for (Class<T> clazz : proxies.keySet()) {
			Object proxy = proxies.get(clazz);
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				Class<?> type = field.getType();
				Object object = proxies.get(type);
				if (object != null) {
					LOGGER.debug("Injecting proxy property {} in proxied object of type {}", type.getCanonicalName(), clazz.getCanonicalName());
					field.setAccessible(true);
					try {
						field.set(proxy, object);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO message
						throw new IllegalStateException(e);
					}
				}
			}
		}

		LOGGER.debug("Created {} proxies", proxies.size());

		return Collections.unmodifiableMap(proxies);
	}

	/**
	 * Method populates the availableMethods map In order to be added methods
	 * should be:
	 * <ul>
	 * <li>annotated with {@link MathematicalFunction}</li>
	 * <li>a public instance method</li>
	 * </ul>
	 * Protected for test purposes
	 */
	protected Map<SimpleEntry<String, Integer>, Method> makeAvailableMethodAliasMap(Map<Class<AbstractFunction>, AbstractFunction> proxies) {
		Map<SimpleEntry<String, Integer>, Method> availableMethodAliasMap = new HashMap<>();

		final Pattern REGEX_ALIAS = Pattern.compile("[a-zA-Z_][a-zA-Z_0-9]*");

		for (Class<?> clazz : proxies.keySet()) {
			LOGGER.debug("Checking class {} for methods with annotation {}. ", clazz.getCanonicalName(), MathematicalFunction.class.getCanonicalName());
			Method[] declaredMethods = clazz.getDeclaredMethods();
			// TODO use get all methods
			for (Method method : declaredMethods) {
				MathematicalFunction annotation = method.getAnnotation(MathematicalFunction.class);
				if (annotation != null) {
					isValid(method);
					LOGGER.debug("Found method to be called from an external source: {}.{} ", clazz.getCanonicalName(), method.getName());
					String alias = annotation.methodNameAlias();
					alias = StringUtils.isBlank(alias) ? method.getName() : alias;
					if (!REGEX_ALIAS.matcher(alias).matches()) {
						Class<?>[] parameterTypes = method.getParameterTypes();
						String parameterList = "";
						if (parameterTypes != null) {
							parameterList = StringUtils.join(method.getParameterTypes(), ", ");
						}
						throw new IllegalStateException(String.format("The alias '%s' used for %s.%s(%s) does not comply to the format for an alias '%s.'",
								alias, clazz.getCanonicalName(), method.getName(), parameterList, REGEX_ALIAS.pattern()));
					}
					// String argumentsSignature =
					// getArgumentsSignature(method);
					int numberOfArguments = method.getParameterTypes().length;
					Method put = availableMethodAliasMap.put(new SimpleEntry<String, Integer>(alias, numberOfArguments), method);
					if (put != null) {
						throw new IllegalStateException(String.format("\nThe alias %s used for\n%s.%s has allready been used for\n%s.%s ", alias, method
								.getDeclaringClass().getCanonicalName(), method.getName(), put.getDeclaringClass().getCanonicalName(), put.getName()));
					}
				}
			}
		}

		return Collections.unmodifiableMap(availableMethodAliasMap);
	}

	/**
	 * Method that validates the supplied method<br>
	 * <br>
	 * The method is valid if<br>
	 * <ul>
	 * <li>the method is a public instance method</li>
	 * <li>the method's return type is of the type as specified in
	 * {@link MathematicalFunctionInvoker#numberClass}</li>
	 * <li>the method has one or more arguments</li>
	 * <li>the method's arguments are of the type as specified in
	 * {@link MathematicalFunctionInvoker#numberClass} or in case of a
	 * collection the component type is as specified in
	 * {@link MathematicalFunctionInvoker#numberClass}</li>
	 * </ul>
	 * <br>
	 */
	private void isValid(Method method) {
		LOGGER.info("Validating the method {}.{}", method.getDeclaringClass().getCanonicalName(), method.getName());

		// Check the method's signature
		LOGGER.info("Validating if the method {}.{} is a public instance method", method.getDeclaringClass().getCanonicalName(), method.getName());
		if (Modifier.isPrivate(method.getModifiers()) || Modifier.isStatic(method.getModifiers())) {
			throw new IllegalStateException(ErrorMessages.ANNOTATED_METHOD_NOT_PUBLIC_INSTANCE_METHOD.getFormattedErrorMessage(
					MathematicalFunction.class.getCanonicalName(), method.getDeclaringClass().getCanonicalName(), method.getName()));
		}

		// Check the method's return type
		LOGGER.info("Validating if the method {}.{} return type is {}", new String[] { method.getDeclaringClass().getCanonicalName(), method.getName(),
				numberClass.getCanonicalName() });
		Class<?> actualReturnType = method.getReturnType();
		if (numberClass != actualReturnType) {
			throw new IllegalStateException(ErrorMessages.WRONG_METHOD_RETURN_TYPE.getFormattedErrorMessage(actualReturnType.getCanonicalName(),
					numberClass.getCanonicalName()));
		}

		// Check method's argument types
		LOGGER.info("Validating if the method {}.{} argument types are of type {}",
				new String[] { method.getDeclaringClass().getCanonicalName(), method.getName(), numberClass.getCanonicalName() });
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		if (genericParameterTypes.length == 0) {
			throw new IllegalStateException(ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("Geen argumenten"));
		}

		for (Type genericParameterType : genericParameterTypes) {
			Class<?> actualParameterType = null;
			if (genericParameterType instanceof Class) {
				Class<?> clazz = (Class<?>) genericParameterType;
				actualParameterType = clazz.isArray() ? clazz.getComponentType() : clazz;
			} else if (genericParameterType instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) genericParameterType;
				if (!Collection.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
					throw new IllegalStateException(ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("Wrong generic type"));
				}

				actualParameterType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
			}

			if (numberClass != actualParameterType) {
				throw new IllegalStateException(ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("Wrong number"));
			}
		}

	}

	/**
	 * Method to create a proxied instance of the specified class If the method
	 * in the supplied class contains {@link MethodConstraint} annotated
	 * arguments the method is handled (intercepted) In this case the supplied
	 * arguments are validated before invocation
	 * 
	 * @param clazz
	 *            The class to be proxied
	 * @return The proxied class instance
	 */
	@SuppressWarnings("unchecked")
	private static <T> T createProxyInstance(T bean) {
		final Class<T> clazz = (Class<T>) bean.getClass();

		ProxyFactory factory = new ProxyFactory();
		factory.setSuperclass(clazz);

		final MethodConstraint classAnnotation = clazz.getAnnotation(MethodConstraint.class);

		factory.setFilter(new MethodFilter() {

			@Override
			public boolean isHandled(Method m) {
				if (m.isBridge() || m.getDeclaringClass() != clazz) {
					return false;
				}

				if (classAnnotation != null || (m.getAnnotation(MethodConstraint.class) != null)) {
					LOGGER.debug("Method {}.{}(...) arguments are validated", m.getDeclaringClass().getSimpleName(), m.getName());
					return true;
				}

				return false;
			}

		});

		// A number factory method is a method to convert string properties
		// defined in an annotation into a number
		Method numberFactoryMethod = null;
		if (classAnnotation != null) {
			Class<?> numberFactoryContainingClass = classAnnotation.numberType();
			String methodname = classAnnotation.valueOf();
			try {
				numberFactoryMethod = numberFactoryContainingClass.getDeclaredMethod(methodname, String.class);
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		MethodHandler methodHandler = new MathematicalFunctionHandler(numberFactoryMethod);
		T proxy = null;
		try {
			proxy = (T) factory.create(new Class<?>[] { clazz }, new Object[] { bean }, methodHandler);
		} catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return proxy;
	}

	public Map<Class<AbstractFunction>, AbstractFunction> getProxies() {
		return Collections.unmodifiableMap(proxies);
	}

	@Override
	public String toString() {
		return String.format("MathematicalFunctionInvoker\nArithmetic function groups: %s\nBase number for arithmetic operations: %s",
				StringUtils.join(getBeanNames(), ", "), numberClass.getCanonicalName());
	}

}

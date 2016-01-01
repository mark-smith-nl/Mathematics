package nl.smith.mathematics.functions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;

import nl.smith.mathematics.functions.annotation.FunctionProperty;
import nl.smith.mathematics.utility.FunctionContextHelper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFunction {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFunction.class);

	public AbstractFunction() {
		Class<? extends AbstractFunction> clazz = this.getClass();
		LOGGER.info("Create instance of class {}", clazz.getCanonicalName());
		setFunctionProperties(clazz);
	}

	// Constructor for instantiating proxy
	// public<T extends AbstractFunction> AbstractFunction(T baseObject ) {
	// Class<? extends AbstractFunction> clazz = this.getClass();
	// LOGGER.info("Create instance proxy instance of class {}", clazz.getCanonicalName());
	// copyFunctionProperties(clazz);
	// }

	@SuppressWarnings("unchecked")
	private void setFunctionProperties(Class<? extends AbstractFunction> clazz) {
		while (clazz != AbstractFunction.class) {
			Map<String, Object> functionContext = Collections.unmodifiableMap(FunctionContextHelper.makeFunctionContext(clazz));
			setFunctionProperties(clazz, functionContext);
			clazz = (Class<? extends AbstractFunction>) clazz.getSuperclass();
		}
	}

	private void setFunctionProperties(Class<? extends AbstractFunction> clazz, Map<String, Object> functionContext) {
		LOGGER.info("Setting function properties for class {}", clazz);

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			FunctionProperty annotation = field.getAnnotation(FunctionProperty.class);
			if (annotation != null) {
				LOGGER.info("Setting function property for {}.{}", clazz, field.getName());
				String simplePropertyName = annotation.simplePropertyName();
				if (StringUtils.isBlank(simplePropertyName)) {
					LOGGER.debug("No name specified for property {}.{}.", clazz, field.getName());
					simplePropertyName = field.getName();
				}

				String canonicalPropertyName = simplePropertyName;
				if (annotation.prefixWithCanonicalClassName()) {
					canonicalPropertyName = clazz.getCanonicalName() + "." + canonicalPropertyName;
				}

				try {
					LOGGER.info("Retrieving string value for property with key {}.", canonicalPropertyName);
					Object propertyValue = functionContext.get(canonicalPropertyName);
					if (propertyValue == null && !annotation.nullable()) {
						// TODO implement message
						throw new IllegalStateException(String.format("Fout waarde niet gevonden voor: '%s.%s'.\nProperty key: '%s'", clazz.getCanonicalName(), field.getName(),
								canonicalPropertyName));
					}

					int modifiers = field.getModifiers();
					if (Modifier.isFinal(modifiers)) {
						throw new IllegalStateException("Fout property is final voor: " + field.getName());
					}
					if (Modifier.isStatic(modifiers)) {
						throw new IllegalStateException("Fout property is static voor: " + field.getName());
					}
					boolean isPublic = Modifier.isPublic(modifiers);

					if (!isPublic) {
						field.setAccessible(true);
					}
					field.set(this, propertyValue);
					if (!isPublic) {
						field.setAccessible(false);
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO implement message
					throw new IllegalStateException("Fout", e);
				}
			}
		}
	}

}

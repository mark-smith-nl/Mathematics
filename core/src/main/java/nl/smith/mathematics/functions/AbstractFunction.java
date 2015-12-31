package nl.smith.mathematics.functions;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import nl.smith.mathematics.functions.annotation.FunctionProperty;
import nl.smith.mathematics.utility.FunctionContextHelper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFunction {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFunction.class);

	private final Map<String, Object> functionContext;

	public AbstractFunction() {
		Class<? extends AbstractFunction> clazz = this.getClass();
		LOGGER.info("Create context for class {}", clazz.getCanonicalName());
		functionContext = Collections.unmodifiableMap(FunctionContextHelper.makeFunctionContext(clazz));
		setFunctionProperties(clazz);
	}

	public Map<String, Object> getFunctionContext() {
		return functionContext;
	}

	private void setFunctionProperties(Class<? extends AbstractFunction> clazz) {
		LOGGER.info("Setting function properties for class {}", clazz);

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			FunctionProperty annotation = field.getAnnotation(FunctionProperty.class);
			if (annotation != null) {
				LOGGER.info("Setting function property for {}.{}", clazz, field.getName());
				String simplePropertyName = annotation.simplePropertyName();
				if (StringUtils.isBlank(simplePropertyName)) {
					LOGGER.info("No name specified for property {}.{}.", clazz, field.getName());
					simplePropertyName = field.getName();
				}

				String canonicalPropertyName = simplePropertyName;
				if (annotation.prefixWithCanonicalClassName()) {
					canonicalPropertyName = clazz.getCanonicalName() + "." + canonicalPropertyName;
				}

				try {
					LOGGER.info("Retrieving string value for property with name {}.", canonicalPropertyName);
					Object propertyValue = getFunctionContext().get(canonicalPropertyName);
					if (propertyValue == null && !annotation.nullable()) {
						// TODO implement message
						throw new IllegalStateException("Fout" + field.getName());
					}

					field.setAccessible(true);
					field.set(this, propertyValue);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO implement message
					throw new IllegalStateException("Fout", e);
				}
			}
		}
	}

}

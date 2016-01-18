package nl.smith.mathematics.functions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import nl.smith.mathematics.functions.annotation.FunctionProperty;
import nl.smith.mathematics.utility.FunctionContextHelper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFunction<T extends AbstractFunction<?>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFunction.class);

	protected void setFunctionProperties() {
		Class<?> clazz = this.getClass();
		Properties providedProperties = System.getProperties();

		while (clazz != AbstractFunction.class) {
			Properties unusedProperties = new Properties();
			Map<String, Field> canonicalPropertyNameFieldMap = new HashMap<>();
			Map<String, Boolean> canonicalPropertyNameIsNullableMap = new HashMap<>();
			Map<String, Class<?>> canonicalPropertyNameFieldTypeMap = new HashMap<>();
			fillCanonicalPropertyNameFieldAndFieldTypeMaps(clazz, canonicalPropertyNameFieldMap, canonicalPropertyNameIsNullableMap, canonicalPropertyNameFieldTypeMap);

			Map<String, Object> functionContext = FunctionContextHelper.makeFunctionContext(clazz, canonicalPropertyNameFieldTypeMap, providedProperties, unusedProperties, true);

			providedProperties = unusedProperties;

			setFunctionProperties(clazz, canonicalPropertyNameFieldMap, canonicalPropertyNameIsNullableMap, functionContext);

			clazz = clazz.getSuperclass();
		}
	}

	/**
	 * Method copies all instance values of the base object to the properties of the current instance (this). Note: Both the baseobject and the current instance (this) are of the same type.
	 * 
	 * @param baseObject
	 */
	protected void setFunctionProperties(T baseObject) {
		Class<?> clazz = this.getClass();

		while (clazz != AbstractFunction.class) {
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				int modifiers = field.getModifiers();
				if (!Modifier.isStatic(modifiers)) {
					boolean isPublic = Modifier.isPublic(modifiers);
					if (!isPublic) {
						field.setAccessible(true);
					}
					try {
						field.set(this, field.get(baseObject));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (!isPublic) {
						field.setAccessible(false);
					}
				}

			}
			clazz = clazz.getSuperclass();
		}
	}

	private void fillCanonicalPropertyNameFieldAndFieldTypeMaps(Class<?> clazz, Map<String, Field> canonicalPropertyNameFieldMap, Map<String, Boolean> canonicalPropertyNameIsNullableMap,
			Map<String, Class<?>> canonicalPropertyNameFieldTypeMap) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			FunctionProperty annotation = field.getAnnotation(FunctionProperty.class);
			if (annotation != null) {
				int modifiers = field.getModifiers();
				if (Modifier.isStatic(modifiers) || !Modifier.isFinal(modifiers)) {
					throw new IllegalStateException(String.format("Error. Property: '%s.%s' is not a final instance property.", clazz.getCanonicalName(), field.getName()));
				}

				String simplePropertyName = annotation.simplePropertyName();
				if (StringUtils.isBlank(simplePropertyName)) {
					LOGGER.debug("No name specified for property {}.{}.", clazz, field.getName());
					simplePropertyName = field.getName();
				}

				String canonicalPropertyName = simplePropertyName;
				if (annotation.prefixWithCanonicalClassName()) {
					canonicalPropertyName = clazz.getCanonicalName() + "." + canonicalPropertyName;
				}

				Field fieldMappedToCanonicalPropertyName = canonicalPropertyNameFieldMap.put(canonicalPropertyName, field);
				if (fieldMappedToCanonicalPropertyName != null) {
					throw new IllegalStateException(String.format("Property name %s used for %s.%s is already used for %2$s.%s", canonicalPropertyName, clazz.getCanonicalName(),
							field.getName(), fieldMappedToCanonicalPropertyName.getName()));
				}

				canonicalPropertyNameIsNullableMap.put(canonicalPropertyName, annotation.nullable());

				canonicalPropertyNameFieldTypeMap.put(canonicalPropertyName, field.getType());
			}
		}
	}

	private void setFunctionProperties(Class<?> clazz, Map<String, Field> canonicalPropertyNameFieldMap, Map<String, Boolean> canonicalPropertyNameIsNullableMap,
			Map<String, Object> functionContext) {
		LOGGER.info("Setting function properties for class {}", clazz);

		for (String canonicalPropertyName : canonicalPropertyNameFieldMap.keySet()) {
			Field field = canonicalPropertyNameFieldMap.get(canonicalPropertyName);
			Boolean isNullable = canonicalPropertyNameIsNullableMap.get(canonicalPropertyName);
			LOGGER.info("Retrieving string value for property with key {}.", canonicalPropertyName);
			Object propertyValue = functionContext.get(canonicalPropertyName);
			Object initialValue = null;
			try {
				int modifiers = field.getModifiers();
				boolean isPublic = Modifier.isPublic(modifiers);

				if (!isPublic) {
					field.setAccessible(true);
				}

				initialValue = field.get(this);

				if (!isPublic) {
					field.setAccessible(true);
				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new IllegalStateException(String.format("Error. Initial value could not be found for '%s.%s'.", clazz.getCanonicalName(), field.getName()), e);
			}

			if (initialValue == null) {
				if (propertyValue == null && !isNullable) {
					// TODO implement message
					throw new IllegalStateException(String.format("Error. No value specified for: '%s.%s'.\nProperty key: '%s'.\nNo initial value specified.\n"
							+ "Please add: %3$s.value=<propertyValue> and %3$s.typee=<propertyType> to property file or add as system properties.", clazz.getCanonicalName(),
							field.getName(), canonicalPropertyName));
				} else {
					setPropertyValue(field, propertyValue);
				}

			} else if (propertyValue != null) {
				// No initial value specified in the code. A propertyValue could be retrieved from the context.
				setPropertyValue(field, propertyValue);
			}
		}
	}

	private void setPropertyValue(Field field, Object propertyValue) {
		int modifiers = field.getModifiers();

		if (Modifier.isStatic(modifiers)) {
			throw new IllegalStateException("Error property is static for: " + field.getName());
		}

		boolean isPublic = Modifier.isPublic(modifiers);

		if (!isPublic) {
			field.setAccessible(true);
		}

		try {
			field.set(this, propertyValue);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException(String.format("Could not set value  '%s.%s' to %s.", this.getClass().getCanonicalName(), field.getName(), propertyValue), e);
		}

		if (!isPublic) {
			field.setAccessible(false);
		}
	}

}

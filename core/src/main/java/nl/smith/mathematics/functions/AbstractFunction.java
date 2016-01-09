package nl.smith.mathematics.functions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import nl.smith.mathematics.functions.annotation.FunctionProperty;
import nl.smith.mathematics.utility.FunctionContextHelper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFunction<T extends AbstractFunction<?>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFunction.class);

	public AbstractFunction() {
		Class<?> clazz = this.getClass();
		LOGGER.info("Create instance of class {}", clazz.getCanonicalName());
		setFunctionProperties(clazz);
	}

	private void setFunctionProperties(Class<?> clazz) {
		while (clazz != AbstractFunction.class) {
			Map<String, Field> canonicalPropertyNameFieldMap = new HashMap<>();
			Map<String, Boolean> canonicalPropertyNameIsNullableMap = new HashMap<>();
			Map<String, Class<?>> canonicalPropertyNameFieldTypeMap = new HashMap<>();
			setCanonicalPropertyNameFieldAndFieldTypeMaps(clazz, canonicalPropertyNameFieldMap, canonicalPropertyNameIsNullableMap, canonicalPropertyNameFieldTypeMap);
			Map<String, Object> functionContext = FunctionContextHelper.makeFunctionContext(clazz, canonicalPropertyNameFieldTypeMap);
			setFunctionProperties(clazz, canonicalPropertyNameFieldMap, canonicalPropertyNameIsNullableMap, functionContext);
			clazz = clazz.getSuperclass();
		}
	}

	private void setCanonicalPropertyNameFieldAndFieldTypeMaps(Class<?> clazz, Map<String, Field> canonicalPropertyNameFieldMap, Map<String, Boolean> canonicalPropertyNameIsNullableMap,
			Map<String, Class<?>> canonicalPropertyNameFieldTypeMap) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			FunctionProperty annotation = field.getAnnotation(FunctionProperty.class);
			if (annotation != null) {
				String simplePropertyName = annotation.simplePropertyName();
				if (StringUtils.isBlank(simplePropertyName)) {
					LOGGER.debug("No name specified for property {}.{}.", clazz, field.getName());
					simplePropertyName = field.getName();
				}

				String canonicalPropertyName = simplePropertyName;
				if (annotation.prefixWithCanonicalClassName()) {
					canonicalPropertyName = clazz.getCanonicalName() + "." + canonicalPropertyName;
				}

				if (canonicalPropertyNameFieldMap.put(canonicalPropertyName, field) != null) {
					throw new IllegalStateException("Implement message");
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
			try {
				LOGGER.info("Retrieving string value for property with key {}.", canonicalPropertyName);
				Object propertyValue = functionContext.get(canonicalPropertyName);
				if (propertyValue == null && !isNullable) {
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

	// Constructor for instantiating proxy
	public AbstractFunction(T baseObject) {
		Class<?> clazz = this.getClass();
		LOGGER.info("Create instance proxy instance of class {} using {}", clazz.getCanonicalName(), baseObject.toString());

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

}

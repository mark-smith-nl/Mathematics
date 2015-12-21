package nl.smith.mathematics.constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.smith.mathematics.constraint.annotation.AssertIsBetween;
import nl.smith.mathematics.constraint.annotation.AssertIsInteger;
import nl.smith.mathematics.constraint.annotation.AssertIsLarger;
import nl.smith.mathematics.constraint.annotation.AssertIsPositive;
import nl.smith.mathematics.number.RationalNumber;

import org.junit.Before;
import org.junit.Test;

public class AbstractNumberConstraintParameterCheckerTest {

	/** Sut */
	private AbstractNumberConstraintParameterChecker abstractNumberConstraintParameterChecker = AbstractNumberConstraintParameterChecker.getInstance();

	@Before
	public void init() {
		abstractNumberConstraintParameterChecker.reset();
	}

	@Test
	public void constraintAppliesToArgumentUsingIllegalAnnotation() {
		Map<String, Object> methodReturnValueMap = new HashMap<>();

		Annotation annotation = getAnnotationProxy(Override.class, methodReturnValueMap);
		RationalNumber rationalNumber = new RationalNumber(500, 2);

		abstractNumberConstraintParameterChecker.assertionAppliesToArgument(annotation, rationalNumber, getNumberFactoryMethod());
	}

	@Test
	public void constraintAppliesToArgumentIsBetween() {
		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);

		Annotation annotation = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);
		RationalNumber rationalNumber = new RationalNumber(500, 2);

		abstractNumberConstraintParameterChecker.assertionAppliesToArgument(annotation, rationalNumber, getNumberFactoryMethod());
	}

	@Test
	public void constraintAppliesToArgumentIsBetweenUsingArray() {
		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);
		Annotation annotation = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);
		RationalNumber[] rationalNumbers = new RationalNumber[] { new RationalNumber(398), new RationalNumber(399), new RationalNumber(400) };

		abstractNumberConstraintParameterChecker.assertionAppliesToArgument(annotation, rationalNumbers, getNumberFactoryMethod());
	}

	@Test
	public void constraintAppliesToArgumentIsBetweenUsingList() {
		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);

		Annotation annotation = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);
		RationalNumber[] rationalNumbers = new RationalNumber[] { new RationalNumber(398), new RationalNumber(399), new RationalNumber(400) };
		List<RationalNumber> rationalNumberList = Arrays.asList(rationalNumbers);

		abstractNumberConstraintParameterChecker.assertionAppliesToArgument(annotation, rationalNumberList, getNumberFactoryMethod());
	}

	@Test
	public void constraintAppliesToArgumentIsBetweenUsingSet() {
		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);

		Annotation annotation = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);
		RationalNumber[] rationalNumbers = new RationalNumber[] { new RationalNumber(398), new RationalNumber(399), new RationalNumber(400), new RationalNumber(400) };
		Set<RationalNumber> rationalNumberList = new HashSet<>(Arrays.asList(rationalNumbers));

		abstractNumberConstraintParameterChecker.assertionAppliesToArgument(annotation, rationalNumberList, getNumberFactoryMethod());
	}

	@Test(expected = IllegalStateException.class)
	public void constraintAppliesToArgumentIsBetweenUsingNull() {
		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);

		Annotation annotation = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);

		abstractNumberConstraintParameterChecker.assertionAppliesToArgument(annotation, null, getNumberFactoryMethod());
	}

	@Test(expected = IllegalStateException.class)
	public void constraintAppliesToArgumentIsBetweenUsingEmptyArray() {
		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);

		Annotation annotation = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);
		RationalNumber[] rationalNumbers = new RationalNumber[0];

		abstractNumberConstraintParameterChecker.assertionAppliesToArgument(annotation, rationalNumbers, getNumberFactoryMethod());
	}

	@Test(expected = IllegalArgumentException.class)
	public void constraintAppliesToArgumentIsBetweenUsingArrayContainingNull() {
		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);

		Annotation annotation = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);
		RationalNumber[] rationalNumbers = new RationalNumber[] { new RationalNumber(398), new RationalNumber(399), new RationalNumber(400), null };

		abstractNumberConstraintParameterChecker.assertionAppliesToArgument(annotation, rationalNumbers, getNumberFactoryMethod());
	}

	@Test(expected = IllegalArgumentException.class)
	public void constraintAppliesToArgumentIsBetweenUsingArrayContainingIllegalType() {
		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);

		Annotation annotation = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);
		Object[] rationalNumbers = new Object[] { new RationalNumber(398), new RationalNumber(399), new RationalNumber(400), "" };

		abstractNumberConstraintParameterChecker.assertionAppliesToArgument(annotation, rationalNumbers, getNumberFactoryMethod());
	}

	@Test(expected = IllegalArgumentException.class)
	public void constraintAppliesToArgumentIsBetweenUsingString() {
		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);

		Annotation annotation = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);

		abstractNumberConstraintParameterChecker.assertionAppliesToArgument(annotation, "", getNumberFactoryMethod());
	}

	@Test
	public void allConstraintsApplyToArgument() {
		Annotation[] annotations = new Annotation[3];

		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);
		annotations[0] = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);

		methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("errorFormat", "The number {} is not a positive number");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsPositive");
		methodReturnValueMap.put("hashCode", 1);
		annotations[1] = getAnnotationProxy(AssertIsPositive.class, methodReturnValueMap);

		methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("errorFormat", "The number {} is not an integer number");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsInteger");
		methodReturnValueMap.put("hashCode", 2);
		annotations[2] = getAnnotationProxy(AssertIsInteger.class, methodReturnValueMap);

		List<RationalNumber> rationalNumbers = new ArrayList<>();
		rationalNumbers.add(new RationalNumber(5));
		rationalNumbers.add(new RationalNumber(6));
		rationalNumbers.add(new RationalNumber(7));
		rationalNumbers.add(new RationalNumber(80, 5));
		rationalNumbers.add(new RationalNumber(8));
		abstractNumberConstraintParameterChecker.allAssertionsApplyToArgument(annotations, rationalNumbers, getNumberFactoryMethod());
	}

	@Test
	public void allConstraintsApplyToArgumentUsingNotInteger() {
		Annotation[] annotations = new Annotation[3];

		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);
		annotations[0] = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);

		methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("errorFormat", "The number {} is not a positive number");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsPositive");
		methodReturnValueMap.put("hashCode", 1);
		annotations[1] = getAnnotationProxy(AssertIsPositive.class, methodReturnValueMap);

		methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("errorFormat", "The number {} is not an integer number");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsInteger");
		methodReturnValueMap.put("hashCode", 2);
		annotations[2] = getAnnotationProxy(AssertIsInteger.class, methodReturnValueMap);

		List<RationalNumber> rationalNumbers = new ArrayList<>();
		rationalNumbers.add(new RationalNumber(5));
		rationalNumbers.add(new RationalNumber(6));
		rationalNumbers.add(new RationalNumber(7));
		rationalNumbers.add(new RationalNumber(80, 5));
		rationalNumbers.add(new RationalNumber(8));

		abstractNumberConstraintParameterChecker.allAssertionsApplyToArgument(annotations, rationalNumbers, getNumberFactoryMethod());
	}

	@Test
	public void allConstraintsApplyToAllArguments() {
		Annotation[][] allArgumentAnotations = new Annotation[2][];

		Annotation[] firstArgumentAnotations = new Annotation[3];

		Map<String, Object> methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("floor", "2");
		methodReturnValueMap.put("ceiling", "401");
		methodReturnValueMap.put("errorFormat", "{} is not in domain {} - {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsBetween");
		methodReturnValueMap.put("hashCode", 0);
		firstArgumentAnotations[0] = getAnnotationProxy(AssertIsBetween.class, methodReturnValueMap);

		methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("errorFormat", "The number {} is not a positive number");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsPositive");
		methodReturnValueMap.put("hashCode", 1);
		firstArgumentAnotations[1] = getAnnotationProxy(AssertIsPositive.class, methodReturnValueMap);

		methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("errorFormat", "The number {} is not an integer number");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsInteger");
		methodReturnValueMap.put("hashCode", 2);
		firstArgumentAnotations[2] = getAnnotationProxy(AssertIsInteger.class, methodReturnValueMap);

		allArgumentAnotations[0] = firstArgumentAnotations;

		Annotation[] secondArgumentAnotations = new Annotation[1];
		methodReturnValueMap = new HashMap<>();
		methodReturnValueMap.put("value", "300");
		methodReturnValueMap.put("errorFormat", "{} is not larger then {}");
		methodReturnValueMap.put("assertionValidatorClass", AbstractNumberAssertionValidator.class);
		methodReturnValueMap.put("validationMethod", "assertIsLarger");
		methodReturnValueMap.put("hashCode", 3);
		secondArgumentAnotations[0] = getAnnotationProxy(AssertIsLarger.class, methodReturnValueMap);

		allArgumentAnotations[1] = secondArgumentAnotations;

		Object[] arguments = new Object[2];

		List<RationalNumber> rationalNumbers = new ArrayList<>();
		arguments[0] = rationalNumbers;
		rationalNumbers.add(new RationalNumber(5));
		rationalNumbers.add(new RationalNumber(6));
		rationalNumbers.add(new RationalNumber(7));
		rationalNumbers.add(new RationalNumber(80, 5));
		rationalNumbers.add(new RationalNumber(8));

		rationalNumbers = new ArrayList<>();
		arguments[1] = rationalNumbers;
		rationalNumbers.add(new RationalNumber(305));
		rationalNumbers.add(new RationalNumber(306));
		rationalNumbers.add(new RationalNumber(307));
		rationalNumbers.add(new RationalNumber(8000, 6));
		rationalNumbers.add(new RationalNumber(8000, 10));

		abstractNumberConstraintParameterChecker.allAssertionsApplyToAllArguments(getNumberFactoryMethod(), allArgumentAnotations, arguments);
	}

	private static <T extends Annotation> T getAnnotationProxy(final Class<T> annotationClass, final Map<String, Object> methodReturnValueMap) {
		@SuppressWarnings("unchecked")
		T annotation = (T) Proxy.newProxyInstance(AbstractNumberConstraintParameterCheckerTest.class.getClassLoader(), new Class[] { annotationClass }, new InvocationHandler() {

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (method.getName().equals("annotationType")) {
					return annotationClass;
				}
				Object object = methodReturnValueMap.get(method.getName());
				return object;
			}
		});
		return annotation;
	}

	private static Method getNumberFactoryMethod() {
		try {
			return RationalNumber.class.getMethod("valueOf", String.class);
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}

}

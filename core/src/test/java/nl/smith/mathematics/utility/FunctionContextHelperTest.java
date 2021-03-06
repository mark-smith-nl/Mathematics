package nl.smith.mathematics.utility;

import static nl.smith.mathematics.utility.ErrorMessages.CONVERSION_OF_STRING_TO_SPECIFIED_OBJECT_NOT_SUPPORTED;
import static nl.smith.mathematics.utility.ErrorMessages.METHOD_ARGUMENT_CAN_NOT_BE_NULL;
import static nl.smith.mathematics.utility.ErrorMessages.REQUIRED_METHOD_NOT_RETRIEVED;
import static nl.smith.mathematics.utility.ErrorMessages.STRING_NUMBER_PARSE_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import nl.smith.mathematics.functions.AbstractFunction;
import nl.smith.mathematics.number.NumberOperations;
import nl.smith.mathematics.number.RationalNumber;

import org.apache.commons.lang.IllegalClassException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

/**
 * Test for {@link FunctionContextHelper}
 * 
 * @author mark
 *
 */
public class FunctionContextHelperTest extends SystemPropertyChanger {

	public static enum SEXE {
		MALE, FEMALE

	}

	// TestFunction has no associated property file.
	private static class TestFunction extends AbstractFunction<TestFunction> {

	}

	// TestFunction has an associated property file.
	private static class AnotherTestFunction extends AbstractFunction<AnotherTestFunction> {

	}

	private static class TestNumberOperations implements NumberOperations<TestNumberOperations> {

		@Override
		public int compareTo(TestNumberOperations o) {
			return 0;
		}

		@Override
		public boolean isNaturalNumber() {
			return false;
		}

		@Override
		public boolean isPositive() {
			return false;
		}

		@Override
		public boolean isNegative() {
			return false;
		}

		@Override
		public TestNumberOperations add(TestNumberOperations augend) {
			return null;
		}

		@Override
		public TestNumberOperations add(BigInteger augend) {
			return null;
		}

		@Override
		public TestNumberOperations add(long augend) {
			return null;
		}

		@Override
		public TestNumberOperations add(double augend) {
			return null;
		}

		@Override
		public TestNumberOperations subtract(TestNumberOperations subtrahend) {
			return null;
		}

		@Override
		public TestNumberOperations subtract(BigInteger subtrahend) {
			return null;
		}

		@Override
		public TestNumberOperations subtract(long subtrahend) {
			return null;
		}

		@Override
		public TestNumberOperations subtract(double subtrahend) {
			return null;
		}

		@Override
		public TestNumberOperations multiply(TestNumberOperations multiplicand) {
			return null;
		}

		@Override
		public TestNumberOperations multiply(BigInteger multiplicand) {
			return null;
		}

		@Override
		public TestNumberOperations multiply(long multiplicand) {
			return null;
		}

		@Override
		public TestNumberOperations multiply(double multiplicand) {
			return null;
		}

		@Override
		public TestNumberOperations divide(TestNumberOperations divisor) {
			return null;
		}

		@Override
		public TestNumberOperations divide(BigInteger divisor) {
			return null;
		}

		@Override
		public TestNumberOperations divide(long divisor) {
			return null;
		}

		@Override
		public TestNumberOperations divide(double divisor) {
			return null;
		}

		@Override
		public TestNumberOperations abs() {
			return null;
		}

		@Override
		public TestNumberOperations getReciprocalValue() {
			return null;
		}

		@Override
		public TestNumberOperations getIntegerPart() {
			return null;
		}

		@Override
		public TestNumberOperations getFractionalPart() {
			return null;
		}

		@Override
		public BigInteger getIntegerPartAsBigInteger() {
			return null;
		}

		@Override
		public TestNumberOperations negate() {
			return null;
		}

		@Override
		public String toStringExact() {
			return null;
		}

		@Override
		public String toString(int numberOfDigitsAfterDecimalPoint, boolean showDelta) {
			return null;
		}

		@Override
		public TestNumberOperations toStringGetDelta(StringBuffer resultBuffer, int numberOfDigitsAfterDecimalPoint, boolean showDelta) {
			return null;
		}

	}

	@Test(expected = IllegalArgumentException.class)
	public void stringToObjectNoClassSpecified() {
		try {
			FunctionContextHelper.stringToObject(null, "Test");
		} catch (RuntimeException e) {
			checkExceptionAndRethrow(e, IllegalArgumentException.class, METHOD_ARGUMENT_CAN_NOT_BE_NULL.getFormattedErrorMessage("propertyType"));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void stringToObjectNoPopertyStringValueSpecified() {
		try {
			FunctionContextHelper.stringToObject(String.class, null);
		} catch (RuntimeException e) {
			checkExceptionAndRethrow(e, IllegalArgumentException.class, METHOD_ARGUMENT_CAN_NOT_BE_NULL.getFormattedErrorMessage("propertyStringValue"));
		}
	}

	@Test(expected = IllegalClassException.class)
	public void stringToObjectUnrecognizedPropertyType() {
		try {
			FunctionContextHelper.stringToObject(FunctionContextHelper.class, "Test");
		} catch (RuntimeException e) {
			checkExceptionAndRethrow(e, IllegalClassException.class,
					CONVERSION_OF_STRING_TO_SPECIFIED_OBJECT_NOT_SUPPORTED.getFormattedErrorMessage(FunctionContextHelper.class.getCanonicalName()));
		}

	}

	@Test(expected = IllegalArgumentException.class)
	public void stringToObjectPopertyStringValueCanNotBeParsed() {
		String propertyStringValue = "hundred";
		Class<?> propertyType = Integer.class;
		try {
			FunctionContextHelper.stringToObject(propertyType, propertyStringValue);
		} catch (RuntimeException e) {
			checkExceptionAndRethrow(e, IllegalArgumentException.class, InvocationTargetException.class,
					STRING_NUMBER_PARSE_ERROR.getFormattedErrorMessage(propertyStringValue, propertyType.getCanonicalName()));
		}
	}

	@Test
	public void stringToObjectSimpleTypes() {
		String propertyStringValue = "tRuE";
		Class<?> propertyType = Boolean.class;
		assertEquals(Boolean.TRUE, FunctionContextHelper.stringToObject(propertyType, propertyStringValue));
		// A new instance was created
		assertFalse(Boolean.FALSE == FunctionContextHelper.stringToObject(propertyType, propertyStringValue));

		propertyStringValue = "Test";
		propertyType = String.class;
		assertEquals(propertyStringValue, FunctionContextHelper.stringToObject(propertyType, propertyStringValue));
		assertFalse(propertyStringValue == FunctionContextHelper.stringToObject(propertyType, propertyStringValue));

		propertyStringValue = "123";
		propertyType = Integer.class;
		assertEquals(new Integer(propertyStringValue), FunctionContextHelper.stringToObject(propertyType, propertyStringValue));

		propertyStringValue = "123";
		propertyType = BigInteger.class;
		assertEquals(new BigInteger(propertyStringValue), FunctionContextHelper.stringToObject(propertyType, propertyStringValue));

		propertyStringValue = "123.4";
		propertyType = BigDecimal.class;
		assertEquals(new BigDecimal(propertyStringValue), FunctionContextHelper.stringToObject(propertyType, propertyStringValue));
	}

	@Test
	public void stringToObjectEnumTypes() {
		String propertyStringValue = "MALE";
		Class<?> propertyType = SEXE.class;
		assertEquals(SEXE.MALE, FunctionContextHelper.stringToObject(propertyType, propertyStringValue));
		assertTrue(SEXE.MALE == FunctionContextHelper.stringToObject(propertyType, propertyStringValue));
	}

	@Test(expected = IllegalArgumentException.class)
	public void stringToObjectEnumTypesUsingUnregognizedValue() {
		String propertyStringValue = "ENUMVALUETHREE";
		Class<?> propertyType = SEXE.class;
		try {
			FunctionContextHelper.stringToObject(propertyType, propertyStringValue);
		} catch (RuntimeException e) {
			checkExceptionAndRethrow(e, IllegalArgumentException.class, InvocationTargetException.class,
					STRING_NUMBER_PARSE_ERROR.getFormattedErrorMessage(propertyStringValue, propertyType.getCanonicalName()));
		}
	}

	@Test
	public void stringToObjectUsingDateTimePropertyValue() {
		String propertyStringValue = "21-12-2015";
		Class<?> propertyType = DateTime.class;
		assertEquals(new DateTime(2015, 12, 21, 0, 0, 0), FunctionContextHelper.stringToObject(propertyType, propertyStringValue));

		propertyStringValue = "21-12-2015 13:10:05";
		propertyType = DateTime.class;
		assertEquals(new DateTime(2015, 12, 21, 13, 10, 5, 0), FunctionContextHelper.stringToObject(propertyType, propertyStringValue));

		propertyStringValue = "21-12-2015 03:10:05";
		propertyType = DateTime.class;
		assertEquals(new DateTime(2015, 12, 21, 3, 10, 5), FunctionContextHelper.stringToObject(propertyType, propertyStringValue));
	}

	@Test
	public void stringToObjectUsingLocalDatePropertyValue() {
		String propertyStringValue = "21-12-2015";
		Class<LocalDate> propertyType = LocalDate.class;

		assertEquals(new LocalDate(2015, 12, 21), FunctionContextHelper.stringToObject(propertyType, propertyStringValue));
	}

	@Test
	public void stringToObjectUsingNumberFactory() {
		String propertyStringValue = "1.2{345}R";
		Class<?> propertyType = RationalNumber.class;
		FunctionContextHelper.stringToObject(propertyType, propertyStringValue);
	}

	@Test(expected = IllegalArgumentException.class)
	public void stringToObjectUsingNumberFactoryStringValueCanNotBeParsed() {
		String propertyStringValue = "01.2{345}R";
		Class<?> propertyType = RationalNumber.class;
		try {
			FunctionContextHelper.stringToObject(propertyType, propertyStringValue);
		} catch (RuntimeException e) {
			checkExceptionAndRethrow(e, IllegalArgumentException.class, InvocationTargetException.class,
					STRING_NUMBER_PARSE_ERROR.getFormattedErrorMessage(propertyStringValue, propertyType.getCanonicalName()));
		}
	}

	@Test(expected = IllegalStateException.class)
	public void stringToObjectNumberFactoryNotFound() {
		String propertyStringValue = "01.2{345}R";
		Class<?> propertyType = TestNumberOperations.class;
		try {
			FunctionContextHelper.stringToObject(propertyType, propertyStringValue);
		} catch (RuntimeException e) {
			checkExceptionAndRethrow(e, IllegalStateException.class, NoSuchMethodException.class, REQUIRED_METHOD_NOT_RETRIEVED.getFormattedErrorMessage("public static",
					propertyType.getCanonicalName(), propertyType.getCanonicalName(), FunctionContextHelper.NUMBER_FACTORY_METHOD_NAME, String.class.getCanonicalName()));
		}
	}

	@Test
	public void makeFunctionContextWithoutPropertyFile() {
		Class<? extends AbstractFunction<TestFunction>> clazz = TestFunction.class;

		Map<String, Object> functionContext = FunctionContextHelper.makeFunctionContext(clazz);

		assertEquals(0, functionContext.size());
	}

	@Test
	public void makeFunctionContextWithPropertyFile() {
		Class<? extends AbstractFunction<AnotherTestFunction>> clazz = AnotherTestFunction.class;

		Map<String, Class<?>> providedPropertyTypes = new HashMap<>();
		providedPropertyTypes.put("myName", String.class);
		providedPropertyTypes.put("myGender", SEXE.class);
		providedPropertyTypes.put("myBirthDate", LocalDate.class);

		Properties providedProperties = System.getProperties();
		Properties unusedProperties = new Properties();

		Map<String, Object> functionContext = FunctionContextHelper.makeFunctionContext(clazz, providedPropertyTypes, providedProperties, unusedProperties, true);

		assertEquals(3, functionContext.size());
		assertEquals("Mark Smith", functionContext.get("myName"));
		assertEquals(SEXE.MALE, functionContext.get("myGender"));
		assertEquals(new LocalDate(1965, 7, 28), functionContext.get("myBirthDate"));

	}

	@Test
	public void makeFunctionContextWithPropertyFileWithSystemProperty() {
		Class<? extends AbstractFunction<AnotherTestFunction>> clazz = AnotherTestFunction.class;

		Map<String, Class<?>> providedPropertyTypes = new HashMap<>();
		providedPropertyTypes.put("myName", String.class);
		providedPropertyTypes.put("myGender", SEXE.class);
		providedPropertyTypes.put("myBirthDate", LocalDate.class);

		setSystemProperty("myGender.value", "FEMALE");

		Properties providedProperties = System.getProperties();
		Properties unusedProperties = new Properties();

		Map<String, Object> functionContext = FunctionContextHelper.makeFunctionContext(clazz, providedPropertyTypes, providedProperties, unusedProperties, true);

		assertEquals(3, functionContext.size());
		assertEquals("Mark Smith", functionContext.get("myName"));
		assertEquals(SEXE.FEMALE, functionContext.get("myGender"));
		assertEquals(new LocalDate(1965, 7, 28), functionContext.get("myBirthDate"));

	}

	private static <T extends RuntimeException> void checkExceptionAndRethrow(T e, Class<? extends RuntimeException> clazz, String formattedErrorMessage) throws T {
		checkExceptionAndRethrow(e, clazz, null, formattedErrorMessage);
	}

	private static <T extends RuntimeException> void checkExceptionAndRethrow(T e, Class<? extends RuntimeException> expectedRuntimeExceptionClazz,
			Class<? extends Throwable> expectedCauseClass, String formattedErrorMessage) throws T {
		assertTrue(String.format("Expected class: %s. Actual class: %s", expectedRuntimeExceptionClazz.getCanonicalName(), e.getClass().getCanonicalName()),
				e.getClass() == expectedRuntimeExceptionClazz);
		Throwable cause = e.getCause();
		Class<? extends Throwable> causeClass = cause == null ? null : cause.getClass();
		assertTrue(String.format("Expected cause class: %s. Actual class: %s", expectedCauseClass == null ? "none" : expectedCauseClass.getCanonicalName(), causeClass == null ? "none"
				: causeClass.getCanonicalName()), expectedCauseClass == causeClass);

		assertEquals(formattedErrorMessage, e.getMessage());
		throw e;
	}

}

package nl.smith.mathematics.utility;

import static nl.smith.mathematics.utility.ErrorMessages.CONVERSION_OF_STRING_TO_SPECIFIED_OBJECT_NOT_SUPPORTED;
import static nl.smith.mathematics.utility.ErrorMessages.METHOD_ARGUMENT_CAN_NOT_BE_NULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import nl.smith.mathematics.functions.AbstractFunction;

import org.apache.commons.lang.IllegalClassException;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Test for {@link FunctionContextHelper}
 * 
 * @author mark
 *
 */
public class FunctionContextHelperTest {

	private static enum ANENUM {
		ENUMVALUEONE, ENUMVALUETWO

	}

	// TestFunction has no associated property file.
	private static class TestFunction extends AbstractFunction {

	}

	// TestFunction has no associated property file.
	private static class AnotherTestFunction extends AbstractFunction {

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

	@Test
	public void stringToObjectSimpleTypes() {
		String propertyStringValue = "tRuE";
		Class<?> propertyType = Boolean.class;
		assertEquals(Boolean.TRUE, FunctionContextHelper.stringToObject(propertyType, propertyStringValue));
		assertFalse(Boolean.TRUE == FunctionContextHelper.stringToObject(propertyType, propertyStringValue));

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
		String propertyStringValue = "ENUMVALUEONE";
		Class<?> propertyType = ANENUM.class;
		assertEquals(ANENUM.ENUMVALUEONE, FunctionContextHelper.stringToObject(propertyType, propertyStringValue));
		assertTrue(ANENUM.ENUMVALUEONE == FunctionContextHelper.stringToObject(propertyType, propertyStringValue));
	}

	@Test(expected = IllegalArgumentException.class)
	public void stringToObjectCanNotParsePropertyStringValue() {
		String propertyStringValue = "a123";
		Class<?> propertyType = BigInteger.class;
		FunctionContextHelper.stringToObject(propertyType, propertyStringValue);
	}

	@Test(expected = IllegalArgumentException.class)
	public void stringToObjectEnumTypesUsingUnregognizedValue() {
		String propertyStringValue = "ENUMVALUETHREE";
		Class<?> propertyType = ANENUM.class;
		FunctionContextHelper.stringToObject(propertyType, propertyStringValue);
	}

	@Test
	public void stringToObjectUsingDateTimePropertyValue() {
		String propertyStringValue = "21-12-2015";
		Class<?> propertyType = DateTime.class;
		FunctionContextHelper.stringToObject(propertyType, propertyStringValue);
		assertEquals(new DateTime(2015, 12, 21, 0, 0, 0), FunctionContextHelper.stringToObject(propertyType, propertyStringValue));

		propertyStringValue = "21-12-2015 13:10:05";
		propertyType = DateTime.class;
		FunctionContextHelper.stringToObject(propertyType, propertyStringValue);
		assertEquals(new DateTime(2015, 12, 21, 13, 10, 5, 0), FunctionContextHelper.stringToObject(propertyType, propertyStringValue));

		propertyStringValue = "21-12-2015 03:10:05";
		propertyType = DateTime.class;
		FunctionContextHelper.stringToObject(propertyType, propertyStringValue);
		assertEquals(new DateTime(2015, 12, 21, 3, 10, 5), FunctionContextHelper.stringToObject(propertyType, propertyStringValue));
	}

	@Test
	public void makeFunctionContextWithoutPropertyFile() {
		Class<? extends AbstractFunction> clazz = TestFunction.class;

		Map<String, Object> functionContext = FunctionContextHelper.makeFunctionContext(clazz);
		assertEquals(0, functionContext.size());
	}

	@Test
	public void makeFunctionContextWithPropertyFile() {
		Class<? extends AbstractFunction> clazz = AnotherTestFunction.class;

		Map<String, Object> functionContext = FunctionContextHelper.makeFunctionContext(clazz);
		assertEquals(1, functionContext.size());
	}

	private static <T extends RuntimeException> void checkExceptionAndRethrow(T e, Class<? extends RuntimeException> clazz, String formattedErrorMessage) throws T {
		checkExceptionAndRethrow(e, clazz, null, formattedErrorMessage);
	}

	private static <T extends RuntimeException> void checkExceptionAndRethrow(T e, Class<? extends RuntimeException> clazz, Class<? extends Throwable> expectedCauseClass,
			String formattedErrorMessage) throws T {
		System.out.println(e.getClass() == clazz);
		assertTrue(String.format("Expected class: %s. Actual class: %s", clazz.getCanonicalName(), e.getClass().getCanonicalName()), e.getClass() == clazz);
		Throwable cause = e.getCause();
		if (cause == null) {
			assertNull(expectedCauseClass);
		} else {
			assertTrue(cause.getClass() == expectedCauseClass);
		}
		assertEquals(formattedErrorMessage, e.getMessage());
		throw e;
	}
}

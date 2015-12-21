package nl.smith.mathematics.factory.constant;

import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.CIPHRE;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.DECIMAL_NUMBER;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.DECIMAL_NUMBER_WITH_FRACTION_REPEATING;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.FRACTION;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.FRACTION_NON_REPEATING;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.FRACTION_REPEATING;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.INTEGER;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.NON_ZERO_CIPHRE;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.POSITIVE_INTEGER;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.SCIENTIFIC_NUMBER;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.SCIENTIFIC_NUMBER_WITH_FRACTION_REPEATING;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.UNARY_OPERATION;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression.UNSIGNED_INTEGER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test of the enum-patterns {@link ArithmeticComponentRegularExpression}
 * 
 * @author mark
 *
 */
public class ArithmeticComponentRegularExpressionTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArithmeticComponentRegularExpressionTest.class);

	@Test
	public void testRegexCiphre() {
		ArithmeticComponentRegularExpression regexNumber = CIPHRE;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(0, regexNumber.getGroupCount());
		assertEquals(0, regularExpressionNumberStructures.size());
		assertEquals(regexNumber.getGroupCount(), ArithmeticComponentStructure.getNumberOfElements(regularExpressionNumberStructures));

		Pattern pattern = regexNumber.pattern();
		for (int i = 0; i < 10; i++) {
			String value = String.valueOf(i);
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = -10; i < 0; i++) {
			String value = String.valueOf(i);
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = 10; i < 21; i++) {
			String value = String.valueOf(i);
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}
	}

	@Test
	public void testRegexNonZeroCiphre() {
		ArithmeticComponentRegularExpression regexNumber = NON_ZERO_CIPHRE;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(0, regexNumber.getGroupCount());
		assertEquals(0, regularExpressionNumberStructures.size());
		assertEquals(regexNumber.getGroupCount(), ArithmeticComponentStructure.getNumberOfElements(regularExpressionNumberStructures));

		Pattern pattern = regexNumber.pattern();
		for (int i = 1; i < 10; i++) {
			String value = String.valueOf(i);
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = -10; i < 1; i++) {
			String value = String.valueOf(i);
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = 10; i < 21; i++) {
			String value = String.valueOf(i);
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}
	}

	@Test
	public void testPositiveInteger() {
		ArithmeticComponentRegularExpression regexNumber = POSITIVE_INTEGER;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(0, regexNumber.getGroupCount());
		assertEquals(0, regularExpressionNumberStructures.size());
		assertEquals(regexNumber.getGroupCount(), ArithmeticComponentStructure.getNumberOfElements(regularExpressionNumberStructures));

		Pattern pattern = regexNumber.pattern();
		for (int i = 1; i < 100; i++) {
			String value = String.valueOf(i);
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = -10; i < 1; i++) {
			String value = String.valueOf(i);
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = 1; i < 100; i++) {
			String value = "0" + String.valueOf(i);
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}
	}

	@Test
	public void testUnsignedInteger() {
		ArithmeticComponentRegularExpression regexNumber = UNSIGNED_INTEGER;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(1, regexNumber.getGroupCount());
		assertEquals(1, regularExpressionNumberStructures.size());
		assertEquals(ArithmeticComponentName.UNSIGNED_INTEGER, regularExpressionNumberStructures.get(0).getArithmeticComponentName());
		assertEquals(regexNumber.getGroupCount(), ArithmeticComponentStructure.getNumberOfElements(regularExpressionNumberStructures));

		Pattern pattern = regexNumber.pattern();
		for (int i = 0; i < 100; i++) {
			String value = String.valueOf(i);
			assertTrue(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = -10; i < 0; i++) {
			String value = String.valueOf(i);
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = 0; i < 100; i++) {
			String value = "0" + String.valueOf(i);
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}
	}

	@Test
	public void testFractionNonRepeating() {
		ArithmeticComponentRegularExpression regexNumber = FRACTION_NON_REPEATING;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(0, regexNumber.getGroupCount());
		assertEquals(0, regularExpressionNumberStructures.size());
		assertEquals(regexNumber.getGroupCount(), ArithmeticComponentStructure.getNumberOfElements(regularExpressionNumberStructures));

		Pattern pattern = regexNumber.pattern();

		for (int i = 0; i < 2; i++) {
			String leadingZeroPrefix = StringUtils.repeat("0", i);
			for (int j = 1; j < 100; j++) {
				String value = leadingZeroPrefix + String.valueOf(j) + "1";
				assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			}
		}

		for (int i = 0; i < 2; i++) {
			String leadingZeroPrefix = StringUtils.repeat("0", i);
			for (int j = 1; j < 100; j++) {
				String value = leadingZeroPrefix + String.valueOf(j) + "0";
				assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
			}
		}
	}

	@Test
	public void testFractionRepeating() {
		ArithmeticComponentRegularExpression regexNumber = FRACTION_REPEATING;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(0, regexNumber.getGroupCount());
		assertEquals(0, regularExpressionNumberStructures.size());
		assertEquals(regexNumber.getGroupCount(), ArithmeticComponentStructure.getNumberOfElements(regularExpressionNumberStructures));

		Pattern pattern = regexNumber.pattern();

		for (int i = 0; i < 2; i++) {
			String leadingZeroPrefix = StringUtils.repeat("0", i);
			for (int j = 1; j < 100; j++) {
				String value = leadingZeroPrefix + String.valueOf(j);
				assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			}
		}

		for (int i = 0; i < 2; i++) {
			String leadingZeroPrefix = StringUtils.repeat("0", i);
			String value = leadingZeroPrefix;
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}
	}

	@Test
	public void testFraction() {
		ArithmeticComponentRegularExpression regexNumber = FRACTION;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(2, regexNumber.getGroupCount());
		assertEquals(2, regularExpressionNumberStructures.size());
		assertEquals(regexNumber.getGroupCount(), ArithmeticComponentStructure.getNumberOfElements(regularExpressionNumberStructures));
		assertEquals(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK, regularExpressionNumberStructures.get(0).getArithmeticComponentName());
		assertEquals(ArithmeticComponentName.FRACTION_REPEATING_BLOCK, regularExpressionNumberStructures.get(1).getArithmeticComponentName());

		Pattern pattern = regexNumber.pattern();

		for (int i = 1; i < 100; i++) {
			for (int j = 0; j < 2; j++) {
				String leadingZeroPrefix = StringUtils.repeat("0", j);

				for (String repeating : new String[] { "{3}R", "{03}R", "{030}R" }) {
					String value = leadingZeroPrefix + String.valueOf(j) + repeating;
					assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
					value = repeating;
					assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
				}
			}
		}

		for (int i = 0; i < 2; i++) {
			String leadingZeroPrefix = StringUtils.repeat("0", i);
			for (int j = 1; j < 100; j++) {
				for (String repeating : new String[] { "{0}R", "{00}R", "{}R", "" }) {
					String value = leadingZeroPrefix + String.valueOf(j) + repeating;
					assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
				}
			}
		}

		String value = "0{2}R";
		assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
	}

	@Test
	public void testInteger() {
		ArithmeticComponentRegularExpression regexNumber = INTEGER;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(0, regexNumber.getGroupCount());
		assertEquals(0, regularExpressionNumberStructures.size());

		Pattern pattern = regexNumber.pattern();

		for (int i = -100; i < 100; i++) {
			String value = String.valueOf(i);
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = -100; i < 100; i++) {
			String value = "0" + String.valueOf(i);
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = -100; i < 100; i++) {
			String value = String.valueOf(i) + ".1";
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}

		for (String value : new String[] { "-01", "00", "-0" }) {
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}
	}

	@Test
	public void testDecimal() {
		ArithmeticComponentRegularExpression regexNumber = DECIMAL_NUMBER;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(3, regexNumber.getGroupCount());
		assertEquals(2, regularExpressionNumberStructures.size());
		assertEquals(regexNumber.getGroupCount(), ArithmeticComponentStructure.getNumberOfElements(regularExpressionNumberStructures));
		assertEquals(ArithmeticComponentName.INTEGER_NUMBER, regularExpressionNumberStructures.get(0).getArithmeticComponentName());
		assertEquals(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK, regularExpressionNumberStructures.get(1).getArithmeticComponentName());
		assertEquals(ArithmeticComponentName.UNSIGNED_INTEGER, regularExpressionNumberStructures.get(0).getArithmeticComponentStructures().get(0).getArithmeticComponentName());

		Pattern pattern = regexNumber.pattern();

		for (int i = -100; i < 100; i++) {
			String value = String.valueOf(i) + ".1";
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = -100; i < 100; i++) {
			String value = String.valueOf(i);
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = -100; i < 100; i++) {
			String value = String.valueOf(i) + ".0";
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}

	}

	@Test
	public void testDecimalWithFractionRepeating() {
		ArithmeticComponentRegularExpression regexNumber = DECIMAL_NUMBER_WITH_FRACTION_REPEATING;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(4, regexNumber.getGroupCount());
		assertEquals(3, regularExpressionNumberStructures.size());
		assertEquals(regexNumber.getGroupCount(), ArithmeticComponentStructure.getNumberOfElements(regularExpressionNumberStructures));
		assertEquals(ArithmeticComponentName.INTEGER_NUMBER, regularExpressionNumberStructures.get(0).getArithmeticComponentName());
		assertEquals(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK, regularExpressionNumberStructures.get(1).getArithmeticComponentName());
		assertEquals(ArithmeticComponentName.FRACTION_REPEATING_BLOCK, regularExpressionNumberStructures.get(2).getArithmeticComponentName());

		Pattern pattern = regexNumber.pattern();

		for (int i = -100; i < 100; i++) {
			String value = String.valueOf(i) + ".1{1}R";
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = -100; i < 100; i++) {
			String value = String.valueOf(i);
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}

		for (int i = -100; i < 100; i++) {
			String value = String.valueOf(i) + ".1";
			assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		}
	}

	@Test
	public void testScientificNumber() {
		ArithmeticComponentRegularExpression regexNumber = SCIENTIFIC_NUMBER;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(4, regexNumber.getGroupCount());
		assertEquals(3, regularExpressionNumberStructures.size());
		assertEquals(ArithmeticComponentName.INTEGER_NUMBER, regularExpressionNumberStructures.get(0).getArithmeticComponentName());
		assertEquals(ArithmeticComponentName.NON_FUNCTIONAL, regularExpressionNumberStructures.get(1).getArithmeticComponentName());
		assertEquals(ArithmeticComponentName.EXPONENT, regularExpressionNumberStructures.get(2).getArithmeticComponentName());

		Pattern pattern = regexNumber.pattern();

		for (int i = 1; i < 10; i++) {
			String value = String.valueOf(i) + "E1";
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = "-" + value;
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = String.valueOf(i) + "E-1";
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = "-" + value;
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());

			value = String.valueOf(i) + ".1E1";
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = "-" + value;
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = String.valueOf(i) + ".1E-1";
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = "-" + value;
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
		}

		// Integer part of mantissa is [1..9]
		int i = 10;
		String value = String.valueOf(i) + "E1";
		assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		value = "-" + value;
		assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		value = String.valueOf(i) + "E-1";
		assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		value = "-" + value;
		assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());

		value = String.valueOf(i) + ".1E1";
		assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		value = "-" + value;
		assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		value = String.valueOf(i) + ".1E-1";
		assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());
		value = "-" + value;
		assertFalse(getErrorMessage(value, false, regexNumber), pattern.matcher(value).matches());

	}

	@Test
	public void testScientificNumberWithRepeatingFraction() {
		ArithmeticComponentRegularExpression regexNumber = SCIENTIFIC_NUMBER_WITH_FRACTION_REPEATING;

		log(regexNumber);

		List<ArithmeticComponentStructure> regularExpressionNumberStructures = regexNumber.getArithmeticComponentStructures();

		assertEquals(4, regexNumber.getGroupCount());
		assertEquals(4, regularExpressionNumberStructures.size());
		assertEquals(ArithmeticComponentName.INTEGER_NUMBER, regularExpressionNumberStructures.get(0).getArithmeticComponentName());
		assertEquals(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK, regularExpressionNumberStructures.get(1).getArithmeticComponentName());
		assertEquals(ArithmeticComponentName.FRACTION_REPEATING_BLOCK, regularExpressionNumberStructures.get(2).getArithmeticComponentName());
		assertEquals(ArithmeticComponentName.EXPONENT, regularExpressionNumberStructures.get(3).getArithmeticComponentName());

		Pattern pattern = regexNumber.pattern();

		for (int i = 1; i < 10; i++) {
			String value = String.valueOf(i) + ".{1}RE1";
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = "-" + value;
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = String.valueOf(i) + ".{1}RE-1";
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = "-" + value;
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());

			value = String.valueOf(i) + ".1{1}RE1";
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = "-" + value;
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = String.valueOf(i) + ".1{1}RE-1";
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
			value = "-" + value;
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
		}
	}

	@Test
	public void testUnaryOperation() {
		ArithmeticComponentRegularExpression regexNumber = UNARY_OPERATION;

		log(regexNumber);

		Pattern pattern = regexNumber.pattern();

		for (String value : new String[] {
				"1", "-5", "--4",
				"1.1", "-5.1", "--4.1",
				"variable", "$", "function$",
				"-2.45{34}RE4",
				"-2.45{34}R" }) {
			assertTrue(getErrorMessage(value, true, regexNumber), pattern.matcher(value).matches());
		}
	}

	private static void log(ArithmeticComponentRegularExpression regexNumber) {
		LOGGER.info("Testing {}\nDescription: {}\nRegex: {}\nResolved regex: {}",
				new Object[] { regexNumber.name(), regexNumber.getDescription(), regexNumber.getInitialRegex(), regexNumber.regex() });
	}

	private static String getErrorMessage(String value, boolean valid, ArithmeticComponentRegularExpression regexNumber) {
		return String.format("The value %s should %s fit the pattern %s", valid ? "" : "not ", value, regexNumber.regex());
	}
}

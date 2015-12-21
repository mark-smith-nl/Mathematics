package nl.smith.mathematics.utility;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import nl.smith.mathematics.constraint.AbstractNumberAssertionValidator;
import nl.smith.mathematics.number.NumberOperations;
import nl.smith.mathematics.number.RationalNumber;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleAssertionCheckerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAssertionCheckerTest.class);

	private AbstractNumberAssertionValidator validator = AbstractNumberAssertionValidator.getInstance();

	@Test
	public void assertIsInteger() {
		List<NumberOperations<?>> numbers = new ArrayList<>();
		String errorFormat = "The assertion that the number {} is an integer number is not correct";
		for (int i = -10; i < 10; i++) {
			numbers.add(RationalNumber.valueOf(String.valueOf(i) + ".1"));
		}
		numbers.add(RationalNumber.valueOf("12.345{678}R"));
		numbers.add(RationalNumber.valueOf("-12.345{678}R"));
		numbers.add(new RationalNumber(9, 4));
		for (NumberOperations<?> number : numbers) {
			try {
				validator.assertIsInteger(number, errorFormat);
				assertTrue("Expected exception was not thrown.\n Expected exception:\n" + getExpectedErrorMessage(errorFormat, number), false);
			} catch (IllegalArgumentException e) {
				LOGGER.info("\nExpected exception thrown:\n" + e.getMessage());
			}
		}
	}

	@Test
	public void assertIsPositive() {
		List<NumberOperations<?>> numbers = new ArrayList<>();
		String errorFormat = "The assertion that the number {} is a positive number is not correct";
		for (int i = -1; i > -10; i--) {
			numbers.add(new RationalNumber(i));
			numbers.add(RationalNumber.valueOf(String.valueOf(i) + ".1"));
		}
		numbers.add(new RationalNumber(0));
		numbers.add(new RationalNumber(-77, 10));
		numbers.add(RationalNumber.valueOf("-20"));
		for (NumberOperations<?> number : numbers) {
			try {
				validator.assertIsPositive(number, errorFormat);
				assertTrue("Expected exception was not thrown.\n Expected exception:\n" + getExpectedErrorMessage(errorFormat, number), false);
			} catch (IllegalArgumentException e) {
				LOGGER.info("\nExpected exception thrown:\n" + e.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void assertIsLarger() {
		NumberOperations<?> floor = new RationalNumber(100);
		String errorFormat = "The assertion that the number {} is larger then {} is not correct";
		List<NumberOperations<?>> numbers = new ArrayList<NumberOperations<?>>();
		for (int i = 1; i < 100; i++) {
			numbers.add(new RationalNumber(i));
			numbers.add(RationalNumber.valueOf(String.valueOf(i) + ".1"));
		}
		numbers.add(new RationalNumber(77, 10));
		numbers.add(RationalNumber.valueOf("20"));
		for (NumberOperations<?> number : numbers) {
			try {
				validator.assertIsLarger((NumberOperations<RationalNumber>) number, errorFormat, (NumberOperations<RationalNumber>) floor);
				assertTrue("Expected exception was not thrown.\n Expected exception:\n" + getExpectedErrorMessage(errorFormat, number, floor), false);
			} catch (IllegalArgumentException e) {
				LOGGER.info("\nExpected exception thrown:\n" + e.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void assertIsSmaller() {
		NumberOperations<?> ceiling = new RationalNumber(-100);
		String errorFormat = "The assertion that the number {} is smaller then {} is not correct";
		List<NumberOperations<?>> numbers = new ArrayList<NumberOperations<?>>();
		for (int i = 1; i < 10; i++) {
			numbers.add(new RationalNumber(i));
			numbers.add(RationalNumber.valueOf(String.valueOf(i) + ".1"));
		}
		numbers.add(new RationalNumber(77, 10));
		numbers.add(RationalNumber.valueOf("20"));
		for (NumberOperations<?> number : numbers) {
			try {
				validator.assertIsSmaller((NumberOperations<RationalNumber>) number, errorFormat, (NumberOperations<RationalNumber>) ceiling);
				assertTrue("Expected exception was not thrown.\n Expected exception:\n" + getExpectedErrorMessage(errorFormat, number, ceiling), false);
			} catch (IllegalArgumentException e) {
				LOGGER.info("\nExpected exception thrown:\n" + e.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void assertIsBetween() {
		NumberOperations<?> floor = new RationalNumber(100);
		NumberOperations<?> ceiling = new RationalNumber(200);
		String errorFormat = "The assertion that the number {} lies between {} and {} is not correct";
		List<NumberOperations<?>> numbers = new ArrayList<NumberOperations<?>>();
		for (int i = 1; i < 10; i++) {
			numbers.add(new RationalNumber(i));
			numbers.add(RationalNumber.valueOf(String.valueOf(i) + ".1"));
		}
		numbers.add(new RationalNumber(77, 10));
		numbers.add(RationalNumber.valueOf("100"));
		numbers.add(RationalNumber.valueOf("200"));
		for (NumberOperations<?> number : numbers) {
			try {
				validator.assertIsBetween((NumberOperations<RationalNumber>) number, errorFormat, (NumberOperations<RationalNumber>) floor,
						(NumberOperations<RationalNumber>) ceiling);
				assertTrue("Expected exception was not thrown.\n Expected exception:\n" + getExpectedErrorMessage(errorFormat, number, floor, ceiling), false);
			} catch (IllegalArgumentException e) {
				LOGGER.info("\nExpected exception thrown:\n" + e.getMessage());

			}
		}
	}

	/**
	 * Convenience method to create an errormessage using a log4J messageformat and an array of {@link NumberOperations} arguments
	 * 
	 * @param errorFormat
	 *            The log4J errorformat
	 * @param numbers
	 *            The arguments
	 * @return
	 *         An error message
	 */
	private static String getExpectedErrorMessage(String errorFormat, NumberOperations<?>... numbers) {
		String renderedErrorFormat = errorFormat.replaceAll("\\{\\}", "%s");
		List<String> arguments = new ArrayList<>();
		if (numbers != null) {
			for (NumberOperations<?> number : numbers) {
				arguments.add(number == null ? null : number.toString(-1, false));
			}
		}
		return String.format(renderedErrorFormat, arguments.toArray());
	}

}

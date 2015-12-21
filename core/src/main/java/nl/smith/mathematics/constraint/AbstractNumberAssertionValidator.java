package nl.smith.mathematics.constraint;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import nl.smith.mathematics.constraint.exception.AbstractNumberAssertionException;
import nl.smith.mathematics.number.NumberOperations;
import nl.smith.mathematics.number.RationalNumber;

/**
 * Validator which validates simple assertions about {@link NumberOperations}
 * 
 * Validator methods are addressed within the corresponding annotation.
 * 
 * @author mark
 *
 */
public class AbstractNumberAssertionValidator implements AssertionValidator<NumberOperations<?>> {

	private static AbstractNumberAssertionValidator singletonInstance;

	private final Class<AbstractNumberAssertionException> exceptionClass = AbstractNumberAssertionException.class;

	private final Constructor<AbstractNumberAssertionException> exceptionConstructor;

	private AbstractNumberAssertionValidator() {
		try {
			exceptionConstructor = exceptionClass.getConstructor(String.class, NumberOperations[].class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Unexpected exception: Exception constructor can not be found");
		}
	}

	public static AbstractNumberAssertionValidator getInstance() {
		if (singletonInstance == null) {
			singletonInstance = new AbstractNumberAssertionValidator();
		}

		return singletonInstance;
	}

	/**
	 * Method checks if a number is an integer number
	 * 
	 * @param number
	 *            The number to check. <b>Not null</b>
	 * @param errorFormat
	 *            Format string for log4j
	 */
	public <N> void assertIsInteger(NumberOperations<N> number, String errorFormat) {
		if (!number.isNaturalNumber()) {
			throw getWrappedAssertionException(errorFormat, number);
		}
	}

	/**
	 * Method checks if a number is a positive number
	 * 
	 * @param number
	 *            The number to check. <b>Not null</b>
	 * @param errorFormat
	 *            Format string for log4j
	 */
	public <N> void assertIsPositive(NumberOperations<N> number, String errorFormat) {
		if (!number.isPositive()) {
			throw getWrappedAssertionException(errorFormat, number);
		}
	}

	/**
	 * Method checks if a number is larger then the supplied number
	 * 
	 * @param number
	 *            The number to check. <b>Not null</b>
	 * @param errorFormat
	 *            Format string for log4j
	 * @param floor
	 *            The number to compare with. <b>Not null</b>
	 */
	@SuppressWarnings("unchecked")
	public <N> void assertIsLarger(NumberOperations<N> number, String errorFormat, NumberOperations<N> floor) {
		if (number.compareTo((N) floor) < 1) {
			throw getWrappedAssertionException(errorFormat, number, floor);
		}
	}

	/**
	 * Method checks if a number is smaller then the supplied number
	 * 
	 * @param number
	 *            The number to check. <b>Not null</b>
	 * @param errorFormat
	 *            Format string for log4j
	 * @param ceiling
	 *            The number to compare with. <b>Not null</b>
	 */
	@SuppressWarnings("unchecked")
	public <N> void assertIsSmaller(NumberOperations<N> number, String errorFormat, NumberOperations<N> ceiling) {
		if (number.compareTo((N) ceiling) > -1) {
			throw getWrappedAssertionException(errorFormat, number, ceiling);
		}
	}

	/**
	 * Method checks if a number is larger then the first supplied number and smaller then the second
	 * 
	 * @param number
	 *            The number to check. <b>Not null</b>
	 * @param errorFormat
	 *            Format string for log4j
	 * @param floor
	 *            The (lower) number to compare with. <b>Not null</b>
	 * @param ceiling
	 *            The (larger) number to compare with. <b>Not null</b>
	 */
	@SuppressWarnings("unchecked")
	public <N> void assertIsBetween(NumberOperations<N> number, String errorFormat, NumberOperations<N> floor, NumberOperations<N> ceiling) {
		if (number.compareTo((N) floor) < 1 || number.compareTo((N) ceiling) > -1) {
			throw getWrappedAssertionException(errorFormat, number, floor, ceiling);
		}
	}

	public static void main(String[] args) {
		AbstractNumberAssertionValidator abstractNumberAssertionChecker = AbstractNumberAssertionValidator.getInstance();
		abstractNumberAssertionChecker.assertIsPositive(new RationalNumber(-5), "Is nie goed {} {} {}");
	}

	@Override
	public IllegalArgumentException getWrappedAssertionException(String errorFormat, NumberOperations<?>... numbers) {
		try {
			AbstractNumberAssertionException newInstance = exceptionConstructor.newInstance(errorFormat, numbers);
			return new IllegalArgumentException(newInstance);
		} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Unexpected exception");
		}
	}

}

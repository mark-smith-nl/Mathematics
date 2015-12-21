package nl.smith.mathematics.number;

import java.math.BigInteger;

/**
 * Default set of operations for numbers
 * 
 * @author mark
 *
 */
public interface NumberOperations<T> extends Comparable<T> {

	boolean isNaturalNumber();

	boolean isPositive();

	boolean isNegative();

	T add(T augend);

	T add(BigInteger augend);

	T add(long augend);

	T add(double augend);

	T subtract(T subtrahend);

	T subtract(BigInteger subtrahend);

	T subtract(long subtrahend);

	T subtract(double subtrahend);

	T multiply(T multiplicand);

	T multiply(BigInteger multiplicand);

	T multiply(long multiplicand);

	T multiply(double multiplicand);

	T divide(T divisor);

	T divide(BigInteger divisor);

	T divide(long divisor);

	T divide(double divisor);

	T abs();

	T getReciprocalValue();

	T getIntegerPart();

	T getFractionalPart();

	BigInteger getIntegerPartAsBigInteger();

	T negate();

	@Override
	String toString();

	/** Returns the exact presentation of a number as a single line */
	String toStringExact();

	/** Returns the exact presentation of a number as a multiple lines */
	String toString(int numberOfDigitsAfterDecimalPoint, boolean showDelta);

	T toStringGetDelta(StringBuffer resultBuffer, int numberOfDigitsAfterDecimalPoint, boolean showDelta);
}

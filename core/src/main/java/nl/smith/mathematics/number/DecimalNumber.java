package nl.smith.mathematics.number;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Immutable class for storing numbers.
 * 
 * Decimal numbers contain one component of type {@link BigDecimal}.
 * 
 * @author M. Smith
 */
public class DecimalNumber implements NumberOperations<DecimalNumber> {

	private final BigDecimal bigDecimal;

	public DecimalNumber(long value) {
		this(new BigDecimal(value));
	}

	public DecimalNumber(double value) {
		this(new BigDecimal(value));
	}

	public DecimalNumber(String value) {
		this(new BigDecimal(value));
	}

	public DecimalNumber(BigInteger value) {
		this(new BigDecimal(value));
	}

	public DecimalNumber(BigDecimal value) {
		if (value == null) {
			throw new IllegalArgumentException("Null argument is not allowed");
		}

		bigDecimal = value;
	}

	@Override
	public int compareTo(DecimalNumber o) {
		return bigDecimal.compareTo(o.bigDecimal);
	}

	@Override
	public boolean isNaturalNumber() {
		return bigDecimal.stripTrailingZeros().scale() <= 0;
	}

	@Override
	public boolean isPositive() {
		return BigDecimal.ZERO.compareTo(bigDecimal) < 1;
	}

	@Override
	public boolean isNegative() {
		return BigDecimal.ZERO.compareTo(bigDecimal) == 1;
	}

	@Override
	public DecimalNumber add(DecimalNumber augend) {
		return new DecimalNumber(bigDecimal.add(augend.bigDecimal));
	}

	@Override
	public DecimalNumber add(BigInteger augend) {
		return add(new DecimalNumber(augend));
	}

	@Override
	public DecimalNumber add(long augend) {
		return add(new DecimalNumber(augend));
	}

	@Override
	public DecimalNumber add(double augend) {
		return add(new DecimalNumber(augend));
	}

	@Override
	public DecimalNumber subtract(DecimalNumber subtrahend) {
		return new DecimalNumber(bigDecimal.subtract(subtrahend.bigDecimal));
	}

	@Override
	public DecimalNumber subtract(BigInteger subtrahend) {
		return subtract(new DecimalNumber(subtrahend));
	}

	@Override
	public DecimalNumber subtract(long subtrahend) {
		return subtract(new DecimalNumber(subtrahend));
	}

	@Override
	public DecimalNumber subtract(double augend) {
		return subtract(new DecimalNumber(augend));
	}

	@Override
	public DecimalNumber multiply(DecimalNumber multiplicand) {
		return new DecimalNumber(bigDecimal.multiply(multiplicand.bigDecimal));
	}

	@Override
	public DecimalNumber multiply(BigInteger multiplicand) {
		return multiply(new DecimalNumber(multiplicand));
	}

	@Override
	public DecimalNumber multiply(long multiplicand) {
		return multiply(new DecimalNumber(multiplicand));
	}

	@Override
	public DecimalNumber multiply(double multiplicand) {
		return multiply(new DecimalNumber(multiplicand));
	}

	@Override
	public DecimalNumber divide(DecimalNumber divisor) {
		return null;
	}

	@Override
	public DecimalNumber divide(BigInteger divisor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecimalNumber divide(long divisor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecimalNumber divide(double divisor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecimalNumber abs() {
		return new DecimalNumber(bigDecimal.abs());
	}

	@Override
	public DecimalNumber getReciprocalValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecimalNumber getIntegerPart() {
		return new DecimalNumber(bigDecimal.toBigInteger());
	}

	@Override
	public DecimalNumber getFractionalPart() {
		return subtract(getIntegerPart());
	}

	@Override
	public BigInteger getIntegerPartAsBigInteger() {
		return bigDecimal.toBigInteger();
	}

	@Override
	public DecimalNumber negate() {
		return new DecimalNumber(bigDecimal.negate());
	}

	@Override
	public String toStringExact() {
		return bigDecimal.toPlainString();
	}

	@Override
	public String toString(int numberOfDigitsAfterDecimalPoint, boolean showDelta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecimalNumber toStringGetDelta(StringBuffer resultBuffer, int numberOfDigitsAfterDecimalPoint, boolean showDelta) {
		// TODO Auto-generated method stub
		return null;
	}

}

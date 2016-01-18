package nl.smith.mathematics.number;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import nl.smith.mathematics.factory.ArithmeticComponentResolver;
import nl.smith.mathematics.factory.constant.ArithmeticComponentName;

import org.apache.commons.lang.StringUtils;

/**
 * Immutable class for storing rational numbers.
 * 
 * Rational numbers contain two components the numerator and denominator. The numerator and denominator are <b>NOT</b> normalized. The denominator is N+
 * 
 * @author M. Smith
 */
public class RationalNumber implements NumberOperations<RationalNumber> {

	private final BigInteger numerator;

	/** Note the denominator is always a positive integer */
	private final BigInteger denominator;

	/**
	 * Create an instance using 1 as denominator
	 * 
	 * @param numerator
	 */
	public RationalNumber(BigInteger numerator) {
		this(numerator, ONE, true);
	}

	/**
	 * Create an instance using 1 as denominator
	 * 
	 * @param numerator
	 */
	public RationalNumber(long numerator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(1), false);
	}

	/**
	 * 
	 * @param numerator
	 * @param denominator
	 */
	public RationalNumber(long numerator, long denominator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator), true);
	}

	/**
	 * 
	 * @param numerator
	 * @param denominator
	 */

	public RationalNumber(BigInteger numerator, BigInteger denominator) {
		this(numerator, denominator, true);
	}

	public RationalNumber(Map<ArithmeticComponentName, String> numberElements) {
		String signedIntegerPart = numberElements.get(ArithmeticComponentName.INTEGER_NUMBER);
		String nonRepeatingFractionalPart = numberElements.get(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK);
		String repeatingFractionalPart = numberElements.get(ArithmeticComponentName.FRACTION_REPEATING_BLOCK);
		String exponentPart = numberElements.get(ArithmeticComponentName.EXPONENT);

		nonRepeatingFractionalPart = nonRepeatingFractionalPart == null ? "" : nonRepeatingFractionalPart;
		repeatingFractionalPart = repeatingFractionalPart == null ? "0" : repeatingFractionalPart;
		exponentPart = exponentPart == null ? "0" : exponentPart;

		BigInteger numerator = new BigInteger(signedIntegerPart + nonRepeatingFractionalPart + repeatingFractionalPart).subtract(new BigInteger(signedIntegerPart
				+ nonRepeatingFractionalPart));

		BigInteger denominator = getTenPowExponent(nonRepeatingFractionalPart.length() + repeatingFractionalPart.length()).subtract(getTenPowExponent(nonRepeatingFractionalPart.length()));

		int exponent = Integer.valueOf(exponentPart);

		if (exponent < 0) {
			denominator = denominator.multiply(getTenPowExponent(-exponent));
		} else {
			numerator = numerator.multiply(getTenPowExponent(exponent));
		}

		BigInteger gcd = numerator.gcd(denominator);

		this.numerator = numerator.divide(gcd);
		this.denominator = denominator.divide(gcd);

		return;
	}

	private RationalNumber(BigInteger numerator, BigInteger denominator, boolean checkArguments) {
		if (checkArguments) {
			checkArguments(numerator, denominator);
		}

		if (denominator.signum() == -1) {
			this.numerator = ZERO.subtract(numerator);
			this.denominator = denominator.abs();
		} else {
			this.numerator = numerator;
			this.denominator = denominator;
		}
	}

	public BigInteger getNumerator() {
		return numerator;
	}

	public BigInteger getDenominator() {
		return denominator;
	}

	/** Method for testing constructor arguments */
	private static void checkArguments(BigInteger numerator, BigInteger denominator) {
		if (numerator == null || denominator == null) {
			throw new IllegalArgumentException("Null arguments are not allowed");
		}

		if (denominator.equals(ZERO)) {
			throw new IllegalArgumentException("Denominator can not be zero");
		}
	}

	@Override
	public boolean isNaturalNumber() {
		return getNormalizedRationalNumber().denominator.equals(ONE);
	}

	@Override
	public boolean isPositive() {
		return getNormalizedRationalNumber().numerator.compareTo(ONE) >= 0;
	}

	@Override
	public boolean isNegative() {
		return getNormalizedRationalNumber().numerator.compareTo(ZERO) == -1;
	}

	@Override
	public RationalNumber add(RationalNumber augend) {
		BigInteger numerator = this.numerator.multiply(augend.denominator).add(augend.numerator.multiply(this.denominator));
		BigInteger denominator = this.denominator.multiply(augend.denominator);

		return new RationalNumber(numerator, denominator, false);
	}

	@Override
	public RationalNumber add(BigInteger augend) {
		BigInteger numerator = this.numerator.add(augend.multiply(this.denominator));
		BigInteger denominator = this.denominator;

		return new RationalNumber(numerator, denominator, false);
	}

	@Override
	public RationalNumber add(long augend) {
		return add(BigInteger.valueOf(augend));
	}

	@Override
	public RationalNumber add(double augend) {
		return add(RationalNumber.valueOf(String.valueOf(augend)));
	}

	@Override
	public RationalNumber subtract(RationalNumber subtrahend) {
		BigInteger numerator = this.numerator.multiply(subtrahend.denominator).subtract(subtrahend.numerator.multiply(this.denominator));
		BigInteger denominator = this.denominator.multiply(subtrahend.denominator);

		return new RationalNumber(numerator, denominator, false);
	}

	@Override
	public RationalNumber subtract(BigInteger subtrahend) {
		BigInteger numerator = this.numerator.subtract(subtrahend.multiply(this.denominator));
		BigInteger denominator = this.denominator;

		return new RationalNumber(numerator, denominator, false);
	}

	@Override
	public RationalNumber subtract(long subtrahend) {
		return subtract(BigInteger.valueOf(subtrahend));
	}

	@Override
	public RationalNumber subtract(double subtrahend) {
		return subtract(RationalNumber.valueOf(String.valueOf(subtrahend)));
	}

	@Override
	public RationalNumber multiply(RationalNumber multiplicand) {
		BigInteger numerator = this.numerator.multiply(multiplicand.numerator);
		BigInteger denominator = this.denominator.multiply(multiplicand.denominator);

		return new RationalNumber(numerator, denominator, false);
	}

	@Override
	public RationalNumber multiply(BigInteger multiplicand) {
		BigInteger numerator = this.numerator.multiply(multiplicand);
		BigInteger denominator = this.denominator;

		return new RationalNumber(numerator, denominator, false);
	}

	@Override
	public RationalNumber multiply(long multiplicand) {
		return multiply(BigInteger.valueOf(multiplicand));
	}

	@Override
	public RationalNumber multiply(double multiplicand) {
		return multiply(RationalNumber.valueOf(String.valueOf(multiplicand)));
	}

	@Override
	public RationalNumber divide(RationalNumber divisor) {
		return multiply(divisor.getReciprocalValue());
	}

	@Override
	public RationalNumber divide(BigInteger divisor) {
		BigInteger numerator = this.numerator;
		BigInteger denominator = this.denominator.multiply(divisor);

		return new RationalNumber(numerator, denominator, true);
	}

	@Override
	public RationalNumber divide(long divisor) {
		return divide(BigInteger.valueOf(divisor));
	}

	@Override
	public RationalNumber divide(double divisor) {
		return divide(RationalNumber.valueOf(String.valueOf(divisor)));
	}

	@Override
	public RationalNumber abs() {
		return new RationalNumber(numerator.abs(), denominator);
	}

	@Override
	public RationalNumber getReciprocalValue() {
		if (denominator == ZERO) {
			throw new ArithmeticException("Divide by zero");
		}

		return new RationalNumber(denominator, numerator, false);
	}

	@Override
	public RationalNumber getIntegerPart() {
		return new RationalNumber(getIntegerPartAsBigInteger());

	}

	@Override
	public BigInteger getIntegerPartAsBigInteger() {
		return numerator.divide(denominator);
	}

	@Override
	public RationalNumber getFractionalPart() {
		return new RationalNumber(numerator.remainder(denominator), denominator);
	}

	@Override
	public RationalNumber negate() {
		return new RationalNumber(numerator.negate(), denominator, false);
	}

	@Override
	public int compareTo(RationalNumber o) {
		if (o == null) {
			throw new IllegalArgumentException("Null argument is not allowed in the comparation of RationalNumbers");
		}

		return this.subtract(o).numerator.compareTo(ZERO);
	}

	private boolean isNormalized() {
		return numerator.gcd(denominator).equals(ONE);
	}

	public RationalNumber getNormalizedRationalNumber() {
		BigInteger gcd = numerator.gcd(denominator);

		return new RationalNumber(numerator.divide(gcd), denominator.divide(gcd), false);
	}

	@Override
	public String toStringExact() {
		StringBuffer resultBuffer = new StringBuffer();
		RationalNumber fractionalPart = getFractionalPart();
		divisionAsString(resultBuffer, fractionalPart.numerator.abs(), fractionalPart.denominator);
		if (resultBuffer.length() > 0) {
			resultBuffer.insert(0, ".");
		}
		String integerPart = getIntegerPartAsBigInteger().toString();
		resultBuffer.insert(0, integerPart);
		if (isNegative() && integerPart.equals("0")) {
			resultBuffer.insert(0, "-");
		}
		return resultBuffer.toString();
	}

	@Override
	public String toString(int numberOfDigitsAfterDecimalPoint, boolean showDelta) {

		StringBuffer resultBuffer = new StringBuffer();

		RationalNumber delta = toStringGetDelta(resultBuffer, numberOfDigitsAfterDecimalPoint, showDelta);

		if (showDelta) {
			if (!delta.numerator.equals(ZERO)) {
				resultBuffer.append("\n+Delta:\n").append(delta);
			}
		}

		return resultBuffer.toString();
	}

	@Override
	public RationalNumber toStringGetDelta(StringBuffer resultBuffer, int numberOfDigitsAfterDecimalPoint, boolean showDelta) {
		if (resultBuffer == null) {
			throw new IllegalArgumentException("A string buffer has to be specified");
		}

		if (numberOfDigitsAfterDecimalPoint < 0) {
			throw new IllegalArgumentException(String.format("The specified number of digits after the decimal point is negative.", numberOfDigitsAfterDecimalPoint));
		}

		RationalNumber delta = null;

		RationalNumber r = this.abs();
		if (!showDelta) {
			r = r.add(new RationalNumber(5).divide(getTenPowExponent(numberOfDigitsAfterDecimalPoint + 1)));
		}

		r = r.multiply(getTenPowExponent(numberOfDigitsAfterDecimalPoint));
		resultBuffer.append(r.getIntegerPartAsBigInteger().toString());
		if (resultBuffer.length() <= numberOfDigitsAfterDecimalPoint) {
			resultBuffer.insert(0, StringUtils.repeat("0", numberOfDigitsAfterDecimalPoint + 1 - resultBuffer.length()));
		}

		if (numberOfDigitsAfterDecimalPoint > 0) {
			resultBuffer.insert(resultBuffer.length() - numberOfDigitsAfterDecimalPoint, ".");
		}

		if (isNegative()) {
			resultBuffer.insert(0, "-");
			r = r.multiply(-1);
		}

		if (showDelta) {
			delta = r.getFractionalPart().divide(getTenPowExponent(numberOfDigitsAfterDecimalPoint)).getNormalizedRationalNumber();
		}

		return delta;
	}

	/**
	 * Returns the numerator and denominator as well as the normalized numerator and denominator as a string. Example the rational number 2/6 will be shown as: Numerator: 2 Denominator: 6
	 * Normalized: Numerator: 1 Denominator: 3
	 */
	@Override
	public String toString() {
		String toString = "Numerator: " + numerator + "\nDenominator: " + denominator;

		if (!isNormalized()) {
			toString += "\nNormalized" + getNormalizedRationalNumber().toString();
		}

		return toString;
	}

	public String toStringFormatted(int numberBeforeDecimalPoint, int numberAfterDecimalPoint) {
		if (numberBeforeDecimalPoint < 1 || numberAfterDecimalPoint < 1) {
			throw new IllegalArgumentException("The numberBeforeDecimalPoint and numberAfterDecimalPoint should be positive integers");
		}
		Map<ArithmeticComponentName, String> numberElements = ArithmeticComponentResolver.getNumberElements(toStringExact());

		String integerPart = StringUtils.repeat(" ", numberBeforeDecimalPoint - numberElements.get(ArithmeticComponentName.INTEGER_NUMBER).length())
				+ numberElements.get(ArithmeticComponentName.INTEGER_NUMBER);

		StringBuffer fractionalPart = new StringBuffer(numberElements.get(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK));
		while (fractionalPart.length() <= numberAfterDecimalPoint) {
			fractionalPart.append(numberElements.get(ArithmeticComponentName.FRACTION_REPEATING_BLOCK));
		}

		return integerPart + "." + fractionalPart.substring(0, numberAfterDecimalPoint);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != RationalNumber.class) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		RationalNumber otherRationalNumber = (RationalNumber) obj;
		return numerator.multiply(otherRationalNumber.denominator).equals(denominator.multiply(otherRationalNumber.numerator));
	}

	/**
	 * Method for calculating the digits after the decimal points of the division remainder/denominator. <br>
	 * The result exactly describes the devision If the result can not be represented as a finite number of digits the repeating part of digits is represented as a list of one ore more
	 * digits enclosed in curly braces and appended with an R. <br>
	 * At least one of the digits in the digits between braces is not <b>0.</b> <br>
	 * <br>
	 * Protected for test purposes
	 * 
	 * @param remainder
	 *            remainder element N+ or zero
	 * @param denominator
	 *            RationalNumber minusOneDivSeven = new RationalNumber(ONE, BigInteger.valueOf(-7)); assertEquals("-0.1", minusOneDivSeven.toString(1, showDelta)); denominator N+
	 * @return
	 */
	protected static void divisionAsString(StringBuffer resultBuffer, BigInteger remainder, BigInteger denominator) {
		BigInteger rest = remainder;
		Map<BigInteger, Integer> divisions = new HashMap<>();
		Integer repeatAfterPosition = null;
		int position = 0;

		while (rest != ZERO && repeatAfterPosition == null) {
			rest = rest.multiply(TEN);
			repeatAfterPosition = divisions.get(rest);
			if (repeatAfterPosition == null) {
				divisions.put(rest, position);
				BigInteger[] divideAndRemainder = rest.divideAndRemainder(denominator);
				resultBuffer.append(divideAndRemainder[0].toString());
				++position;
				rest = divideAndRemainder[1];
			}
		}

		if (repeatAfterPosition != null) {
			resultBuffer.insert(repeatAfterPosition, "{").append("}R");
		}
	}

	public static RationalNumber valueOf(String numberAsString) {
		if (StringUtils.isBlank(numberAsString)) {
			throw new IllegalArgumentException("\nNull or blank numberAsString is not accepted");
		}

		Map<ArithmeticComponentName, String> numberElements = ArithmeticComponentResolver.getNumberElements(numberAsString);

		return new RationalNumber(numberElements);
	}

	private static BigInteger getTenPowExponent(int exponent) {
		if (exponent < 0) {
			throw new IllegalArgumentException(String.format("\nThe supplied exponent %d does not match the constraint that exponents are not negative", exponent));
		}

		return new BigInteger("1" + StringUtils.repeat("0", exponent));
	}

}

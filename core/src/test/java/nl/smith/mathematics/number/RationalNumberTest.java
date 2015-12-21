package nl.smith.mathematics.number;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static java.math.BigInteger.ZERO;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.smith.mathematics.factory.constant.ArithmeticComponentName;

import org.junit.Test;

public class RationalNumberTest {

	@Test(expected = IllegalArgumentException.class)
	public void constructUsingNulls() {
		new RationalNumber(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructUsingZero() {
		new RationalNumber(ONE, ZERO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructUsingZeros() {
		new RationalNumber(ZERO, ZERO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructUsingZeroLong() {
		new RationalNumber(1, 0);
	}

	@Test
	public void constructRationalNumber() {
		new RationalNumber(ZERO, ONE);
		new RationalNumber(ZERO, TEN);
		new RationalNumber(ZERO, new BigInteger("2"));
		new RationalNumber(new BigInteger("2"), new BigInteger("3"));
		new RationalNumber(new BigInteger("-2"), new BigInteger("3"));
		new RationalNumber(new BigInteger("2"), new BigInteger("-3"));
		new RationalNumber(new BigInteger("-2"), new BigInteger("-3"));

	}

	@Test
	public void equals() {
		assertEquals(new RationalNumber(new BigInteger("2"), new BigInteger("3")), new RationalNumber(new BigInteger("2"), new BigInteger("3")));
		assertEquals(new RationalNumber(new BigInteger("-2"), new BigInteger("-3")), new RationalNumber(new BigInteger("2"), new BigInteger("3")));
		assertEquals(new RationalNumber(new BigInteger("2"), new BigInteger("-3")), new RationalNumber(new BigInteger("-2"), new BigInteger("3")));
		assertEquals(new RationalNumber(ZERO, ONE), new RationalNumber(ZERO, TEN));
		assertEquals(new RationalNumber(ZERO, ONE), new RationalNumber(ZERO, new BigInteger("2")));
		assertEquals(new RationalNumber(4, 8), new RationalNumber(-12, -24));
	}

	@Test
	public void normalize() {
		assertEquals(new RationalNumber(ZERO, ONE).getNormalizedRationalNumber(), new RationalNumber(ZERO, TEN).getNormalizedRationalNumber());
		assertEquals(new RationalNumber(ZERO, ONE).getNormalizedRationalNumber(), new RationalNumber(ZERO, new BigInteger("2")).getNormalizedRationalNumber());

		assertEquals(new RationalNumber(new BigInteger("2"), new BigInteger("3")).getNormalizedRationalNumber(),
				new RationalNumber(new BigInteger("4"), new BigInteger("6")).getNormalizedRationalNumber());
	}

	@Test
	public void toExactString() {
		RationalNumber oneDivSeven = new RationalNumber(ONE, BigInteger.valueOf(7));
		assertEquals("0.{142857}R", oneDivSeven.toStringExact());
		RationalNumber twoDivFourteen = new RationalNumber(BigInteger.valueOf(2), BigInteger.valueOf(14));
		assertEquals("0.{142857}R", twoDivFourteen.toStringExact());
		RationalNumber minusOneDivSeven = new RationalNumber(ONE, BigInteger.valueOf(-7));
		assertEquals("-0.{142857}R", minusOneDivSeven.toStringExact());
		RationalNumber minusTwoDivFourteen = new RationalNumber(BigInteger.valueOf(2), BigInteger.valueOf(-14));
		assertEquals("-0.{142857}R", minusTwoDivFourteen.toStringExact());
		assertEquals("10", new RationalNumber(TEN).toStringExact());
		assertEquals("-10", new RationalNumber(BigInteger.valueOf(-10)).toStringExact());
	}

	@Test
	public void toStringWithLengthArgumentNoDelta() {
		boolean showDelta = false;
		RationalNumber oneDivSeven = new RationalNumber(ONE, BigInteger.valueOf(7));
		assertEquals("0.1", oneDivSeven.toString(1, showDelta));
		assertEquals("0.14", oneDivSeven.toString(2, showDelta));
		assertEquals("0.143", oneDivSeven.toString(3, showDelta));
		assertEquals("0.1429", oneDivSeven.toString(4, showDelta));
		assertEquals("0.14286", oneDivSeven.toString(5, showDelta));
		assertEquals("0.142857", oneDivSeven.toString(6, showDelta));
		assertEquals("0.1428571", oneDivSeven.toString(7, showDelta));

		RationalNumber minusOneDivSeven = new RationalNumber(ONE, BigInteger.valueOf(-7));
		assertEquals("-0.1", minusOneDivSeven.toString(1, showDelta));
		assertEquals("-0.14", minusOneDivSeven.toString(2, showDelta));
		assertEquals("-0.143", minusOneDivSeven.toString(3, showDelta));
		assertEquals("-0.1429", minusOneDivSeven.toString(4, showDelta));
		assertEquals("-0.14286", minusOneDivSeven.toString(5, showDelta));
		assertEquals("-0.142857", minusOneDivSeven.toString(6, showDelta));
		assertEquals("-0.1428571", minusOneDivSeven.toString(7, showDelta));

		RationalNumber fifteenDivSeven = new RationalNumber(BigInteger.valueOf(15), BigInteger.valueOf(7));
		assertEquals("2.1", fifteenDivSeven.toString(1, showDelta));
		assertEquals("2.14", fifteenDivSeven.toString(2, showDelta));
		assertEquals("2.143", fifteenDivSeven.toString(3, showDelta));
		assertEquals("2.1429", fifteenDivSeven.toString(4, showDelta));
		assertEquals("2.14286", fifteenDivSeven.toString(5, showDelta));
		assertEquals("2.142857", fifteenDivSeven.toString(6, showDelta));
		assertEquals("2.1428571", fifteenDivSeven.toString(7, showDelta));

		RationalNumber minusFifteenDivSeven = new RationalNumber(BigInteger.valueOf(-15), BigInteger.valueOf(7));
		assertEquals("-2.1", minusFifteenDivSeven.toString(1, showDelta));
		assertEquals("-2.14", minusFifteenDivSeven.toString(2, showDelta));
		assertEquals("-2.143", minusFifteenDivSeven.toString(3, showDelta));
		assertEquals("-2.1429", minusFifteenDivSeven.toString(4, showDelta));
		assertEquals("-2.14286", minusFifteenDivSeven.toString(5, showDelta));
		assertEquals("-2.142857", minusFifteenDivSeven.toString(6, showDelta));
		assertEquals("-2.1428571", minusFifteenDivSeven.toString(7, showDelta));
	}

	@Test
	public void toStringWithLengthArgumentDelta() {
		boolean showDelta = true;
		RationalNumber oneDivSeven = new RationalNumber(ONE, BigInteger.valueOf(7));
		assertEquals("0.1\n+Delta:\nNumerator: 3\nDenominator: 70", oneDivSeven.toString(1, showDelta));
		assertEquals("0.14\n+Delta:\nNumerator: 1\nDenominator: 350", oneDivSeven.toString(2, showDelta));
		assertEquals("0.142\n+Delta:\nNumerator: 3\nDenominator: 3500", oneDivSeven.toString(3, showDelta));
		assertEquals("0.1428\n+Delta:\nNumerator: 1\nDenominator: 17500", oneDivSeven.toString(4, showDelta));
		assertEquals("0.14285\n+Delta:\nNumerator: 1\nDenominator: 140000", oneDivSeven.toString(5, showDelta));
		assertEquals("0.142857\n+Delta:\nNumerator: 1\nDenominator: 7000000", oneDivSeven.toString(6, showDelta));
		assertEquals("0.1428571", oneDivSeven.toString(7, false));
	}

	@Test
	public void toStringGetDelta() {
		boolean showDelta = true;

		int maximumNumberOfDigitsAfterDecimalPoint = 3;
		int steps = 1;
		for (int i = 0; i < maximumNumberOfDigitsAfterDecimalPoint; i++) {
			steps *= 10;
		}
		StringBuffer resultBuffer = new StringBuffer();

		for (int i = 0; i < 11; i++) {
			RationalNumber expected = new RationalNumber(i);

			for (int j = 0; j < steps; j++) {
				for (int numberOfDigitsAfterDecimalPoint = 0; numberOfDigitsAfterDecimalPoint <= maximumNumberOfDigitsAfterDecimalPoint + 1; numberOfDigitsAfterDecimalPoint++) {
					RationalNumber delta = expected.toStringGetDelta(resultBuffer, numberOfDigitsAfterDecimalPoint, showDelta);
					RationalNumber actual = new RationalNumber(makeNumberElements(resultBuffer.toString())).add(delta);
					assertEquals(expected, actual);
					resultBuffer.delete(0, resultBuffer.length());

					RationalNumber negateExpected = expected.negate();
					delta = negateExpected.toStringGetDelta(resultBuffer, numberOfDigitsAfterDecimalPoint, showDelta);
					RationalNumber negateActual = new RationalNumber(makeNumberElements(resultBuffer.toString())).add(delta);
					assertEquals(negateExpected, negateActual);
					resultBuffer.delete(0, resultBuffer.length());
				}
				expected = expected.add(new RationalNumber(1, steps));
			}
		}

	}

	@Test()
	public void toFormattedString() {
		RationalNumber rationalNumber = new RationalNumber(-15, 7).add(new RationalNumber(12, 100));
		assertEquals("        -2.02285", rationalNumber.toStringFormatted(10, 5));
		assertEquals("        -2.022857", rationalNumber.toStringFormatted(10, 6));
		assertEquals("        -2.0228571", rationalNumber.toStringFormatted(10, 7));
		assertEquals("        -2.02285714", rationalNumber.toStringFormatted(10, 8));
		assertEquals("        -2.022857142", rationalNumber.toStringFormatted(10, 9));
		assertEquals("        -2.0228571428", rationalNumber.toStringFormatted(10, 10));
		assertEquals("        -2.02285714285", rationalNumber.toStringFormatted(10, 11));
		assertEquals("        -2.022857142857", rationalNumber.toStringFormatted(10, 12));
		assertEquals("        -2.0228571428571", rationalNumber.toStringFormatted(10, 13));
		assertEquals("-2.0228571428571", rationalNumber.toStringFormatted(1, 13));
		assertEquals("-2.0228571428571", rationalNumber.toStringFormatted(2, 13));
		assertEquals(" -2.0228571428571", rationalNumber.toStringFormatted(3, 13));
	}

	@Test(expected = IllegalArgumentException.class)
	public void toFormattedStringWithIllegalArgument() {
		RationalNumber rationalNumber = new RationalNumber(-15, 7).add(new RationalNumber(12, 100));
		assertEquals("-2.0228571428571", rationalNumber.toStringFormatted(0, 13));

	}

	@Test
	public void compareTo() {
		RationalNumber rationalNumberFirst = new RationalNumber(2, 4);
		RationalNumber rationalNumberSecond = new RationalNumber(2, 4);

		assertEquals(rationalNumberFirst.compareTo(rationalNumberSecond), 0);
		assertEquals(rationalNumberSecond.compareTo(rationalNumberFirst), 0);

		rationalNumberSecond = new RationalNumber(2, 5);
		assertEquals(rationalNumberFirst.compareTo(rationalNumberSecond), 1);
		assertEquals(rationalNumberSecond.compareTo(rationalNumberFirst), -1);

		rationalNumberFirst = new RationalNumber(0, 4);
		rationalNumberSecond = new RationalNumber(0, 14);

		assertEquals(rationalNumberFirst.compareTo(rationalNumberSecond), 0);
		assertEquals(rationalNumberSecond.compareTo(rationalNumberFirst), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void compareToWithIllegalArgument() {
		RationalNumber rationalNumberFirst = new RationalNumber(2, 4);
		RationalNumber rationalNumberSecond = null;

		assertEquals(rationalNumberFirst.compareTo(rationalNumberSecond), 0);
	}

	@Test()
	public void getIntegerPart() {
		RationalNumber rationalNumber = new RationalNumber(0, 1);
		assertEquals(rationalNumber.getIntegerPart(), new RationalNumber(0));

		rationalNumber = new RationalNumber(25, 2);
		assertEquals(rationalNumber.getIntegerPart(), new RationalNumber(12));

		rationalNumber = new RationalNumber(-25, 2);
		assertEquals(rationalNumber.getIntegerPart(), new RationalNumber(-12));

		rationalNumber = new RationalNumber(345, 1000);
		assertEquals(rationalNumber.getIntegerPart(), new RationalNumber(0));

		rationalNumber = new RationalNumber(-345, 1000);
		assertEquals(rationalNumber.getIntegerPart(), new RationalNumber(0));

		rationalNumber = new RationalNumber(12345, 1000);
		assertEquals(rationalNumber.getIntegerPart(), new RationalNumber(12));

		rationalNumber = new RationalNumber(-12345, 1000);
		assertEquals(rationalNumber.getIntegerPart(), new RationalNumber(-12));

		rationalNumber = new RationalNumber(1000, 7);
		assertEquals(rationalNumber.getIntegerPart(), new RationalNumber(142));

		rationalNumber = new RationalNumber(-1000, 7);
		assertEquals(rationalNumber.getIntegerPart(), new RationalNumber(-142));
	}

	@Test()
	public void getFractionalPart() {
		RationalNumber rationalNumber = new RationalNumber(0, 1);
		assertEquals(rationalNumber.getFractionalPart(), new RationalNumber(0));

		rationalNumber = new RationalNumber(25, 2);
		assertEquals(rationalNumber.getFractionalPart(), new RationalNumber(1, 2));

		rationalNumber = new RationalNumber(-25, 2);
		assertEquals(rationalNumber.getFractionalPart(), new RationalNumber(-1, 2));

		rationalNumber = new RationalNumber(345, 1000);
		assertEquals(rationalNumber.getFractionalPart(), new RationalNumber(345, 1000));

		rationalNumber = new RationalNumber(-345, 1000);
		assertEquals(rationalNumber.getFractionalPart(), new RationalNumber(-345, 1000));

		rationalNumber = new RationalNumber(12345, 1000);
		assertEquals(rationalNumber.getFractionalPart(), new RationalNumber(345, 1000));

		rationalNumber = new RationalNumber(-12345, 1000);
		assertEquals(rationalNumber.getFractionalPart(), new RationalNumber(-345, 1000));

		rationalNumber = new RationalNumber(1000, 7);
		assertEquals(rationalNumber.getFractionalPart(), new RationalNumber(6, 7));

		rationalNumber = new RationalNumber(-1000, 7);
		assertEquals(rationalNumber.getFractionalPart(), new RationalNumber(-6, 7));
	}

	/* Method for manual checking rational numbers. */
	

	private static Map<String, Object> geParametersForJasperReport() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("ReportTitle", "Rationale getallen");
		parameters.put("Author", "Mark Smith");
		parameters.put("TimeStamp", new Date());

		return parameters;
	}

	private static List<RationalNumber> getEntitiesForJasperReport() {
		List<RationalNumber> numbers = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			numbers.add(new RationalNumber(1, i));
		}

		return numbers;
	}

	private static Map<ArithmeticComponentName, String> makeNumberElements(String constructorArgument) {
		String strippedConstructorArgument = constructorArgument;
		String sign = "";

		if (strippedConstructorArgument.startsWith("-")) {
			strippedConstructorArgument = strippedConstructorArgument.substring(1, strippedConstructorArgument.length());
			sign = "-";
		}
		String[] split = strippedConstructorArgument.split("\\.");

		String integerPart = split[0].replaceFirst("^0*", "");
		String fractionPart = split.length == 1 ? "" : split[1].replaceFirst("0*$", "");

		String resultSignPart = integerPart.length() > 0 || fractionPart.length() > 0 ? sign : "";
		String resultIntegerPart = integerPart.length() == 0 ? "0" : integerPart;
		String resultFractionPart = fractionPart.length() == 0 ? "" : fractionPart;

		Map<ArithmeticComponentName, String> numberElements = new HashMap<>();

		numberElements.put(ArithmeticComponentName.INTEGER_NUMBER, resultSignPart + resultIntegerPart);
		numberElements.put(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK, resultFractionPart);
		numberElements.put(ArithmeticComponentName.FRACTION_REPEATING_BLOCK, null);
		numberElements.put(ArithmeticComponentName.EXPONENT, null);

		return numberElements;
	}
}

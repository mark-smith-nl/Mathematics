package nl.smith.mathematics.functions.rational;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;

import nl.smith.mathematics.functions.AngleType;
import nl.smith.mathematics.functions.GoniometricFunctions;
import nl.smith.mathematics.functions.annotation.FunctionProperty;
import nl.smith.mathematics.functions.annotation.MathematicalFunction;
import nl.smith.mathematics.number.RationalNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GoniometricFunctionsImpl extends GoniometricFunctions<RationalNumber> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(GoniometricFunctionsImpl.class);

	private static final BigInteger TWO = BigInteger.valueOf(2);

	private static final BigInteger FOUR = BigInteger.valueOf(4);

	@FunctionProperty(simplePropertyName = "taylorNumber", prefixWithCanonicalClassName = false)
	private BigInteger taylorNumber;

	@FunctionProperty
	private AngleType angleType;

	@FunctionProperty
	private RationalNumber pi;

	/** Spring instantiated bean */
	public GoniometricFunctionsImpl() {

	}

	// Constructor for instantiating proxy
	public GoniometricFunctionsImpl(GoniometricFunctionsImpl goniometricFunctionsImpl) {
		System.out.println("Yessssssssssssssssssssssssssssssssssssssssssssss");
		taylorNumber = goniometricFunctionsImpl.taylorNumber;
		angleType = goniometricFunctionsImpl.angleType;
		// pi = goniometricFunctionsImpl.pi;
	}

	public BigInteger getTaylorNumber() {
		return taylorNumber;
	}

	@Override
	public AngleType getAngleType() {
		return angleType;
	}

	@Override
	public RationalNumber convertAngle(AngleType from, AngleType to) {
		// TODO
		return null;
	}

	@Override
	public RationalNumber getPi() {
		return pi;
	}

	@SuppressWarnings("unused")
	private RationalNumber calculatePi() {
		BigInteger i = ONE;
		RationalNumber pi = new RationalNumber(BigInteger.valueOf(3));
		boolean substact = false;
		while (i.compareTo(taylorNumber) < 0) {
			BigInteger offset = TWO.multiply(i);
			BigInteger denominator = offset.multiply(offset.add(ONE)).multiply(offset.add(TWO));
			pi = substact ? pi.subtract(new RationalNumber(FOUR, denominator)) : pi.add(new RationalNumber(FOUR, denominator));
			substact = !substact;
			i = i.add(ONE);
		}

		return pi;
	}

	/**
	 * Taylor series: Sin(x) = 0 + x - x^3/3! + x^5/5! - x^7/7! + ... Sum(T(i)) T(0) = x T(i) = -x^2/((2i +1)* 2i)
	 */
	@Override
	@MathematicalFunction(methodNameAlias = "sin")
	public RationalNumber sin(RationalNumber x) {
		BigInteger i = ONE;
		RationalNumber t = x;
		RationalNumber sum = t;
		RationalNumber squareX = x.multiply(x);
		boolean subtract = true;

		while (i.compareTo(taylorNumber) < 0) {
			i = i.add(ONE);
			BigInteger j = i.multiply(TWO).subtract(ONE);
			BigInteger k = j.subtract(ONE);
			t = t.multiply(squareX).divide(j).divide(k);
			sum = subtract ? sum.subtract(t) : sum.add(t);
			sum = sum.getNormalizedRationalNumber();
			subtract = !subtract;
		}
		return sum;
	}

	/**
	 * Taylor series: Cos(x) = 1 - x^2/2! + x^4/4! - x^6/6! + ...
	 */
	@Override
	@MathematicalFunction
	public RationalNumber cos(RationalNumber x) {
		BigInteger i = ZERO;
		RationalNumber t = new RationalNumber(ONE);
		RationalNumber sum = new RationalNumber(ONE);
		RationalNumber squareX = x.multiply(x);
		boolean subtract = true;
		BigInteger times = taylorNumber.subtract(ONE);
		while (i.compareTo(times) < 0) {
			i = i.add(ONE);
			BigInteger j = i.multiply(TWO);
			BigInteger k = j.subtract(ONE);
			t = t.multiply(squareX).divide(j).divide(k);
			sum = subtract ? sum.subtract(t) : sum.add(t);
			sum = sum.getNormalizedRationalNumber();
			subtract = !subtract;
		}
		return sum;
	}

}

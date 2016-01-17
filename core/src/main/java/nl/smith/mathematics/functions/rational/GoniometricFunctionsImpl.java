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

	private static final Logger LOGGER = LoggerFactory.getLogger(GoniometricFunctionsImpl.class);

	private static final BigInteger TWO = BigInteger.valueOf(2);

	private static final BigInteger FOUR = BigInteger.valueOf(4);

	@FunctionProperty
	private final AngleType ANGELTYPE = null;

	@FunctionProperty
	private final RationalNumber PI = new RationalNumber(314, 100);

	/** Spring instantiated bean */
	public GoniometricFunctionsImpl() {
		setFunctionProperties();
	}

	/** Constructor for instantiating proxy */
	public GoniometricFunctionsImpl(GoniometricFunctionsImpl baseObject) {
		LOGGER.info("Create instance proxy instance of class {} using {}", this.getClass().getCanonicalName(), baseObject.toString());
		setFunctionProperties(baseObject);
	}

	@Override
	public AngleType getANGELTYPE() {
		return ANGELTYPE;
	}

	@Override
	public RationalNumber convertAngle(AngleType from, AngleType to) {
		// TODO
		return null;
	}

	@Override
	public RationalNumber getPI() {
		return PI;
	}

	@SuppressWarnings("unused")
	private RationalNumber calculatePi() {
		BigInteger i = ONE;
		RationalNumber PI = new RationalNumber(BigInteger.valueOf(3));
		boolean substact = false;
		while (i.compareTo(getTAYLORNUMBER()) < 0) {
			BigInteger offset = TWO.multiply(i);
			BigInteger denominator = offset.multiply(offset.add(ONE)).multiply(offset.add(TWO));
			PI = substact ? PI.subtract(new RationalNumber(FOUR, denominator)) : PI.add(new RationalNumber(FOUR, denominator));
			substact = !substact;
			i = i.add(ONE);
		}

		return PI;
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

		while (i.compareTo(getTAYLORNUMBER()) < 0) {
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
		BigInteger times = getTAYLORNUMBER().subtract(ONE);
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

	@Override
	public String toString() {
		return "GoniometricFunctionsImpl [TAYLORNUMBER=" + getTAYLORNUMBER() + ", ANGELTYPE=" + getANGELTYPE() + ", PI=" + getPI() + "]";
	}

}

package nl.smith.mathematics.functions.rational;

import static java.math.BigInteger.ONE;

import java.math.BigInteger;

import nl.smith.mathematics.functions.LogarithmicFunctions;
import nl.smith.mathematics.functions.annotation.FunctionProperty;
import nl.smith.mathematics.functions.annotation.MathematicalFunction;
import nl.smith.mathematics.number.RationalNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogarithmicFunctionsImpl extends LogarithmicFunctions<RationalNumber> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogarithmicFunctionsImpl.class);

	@FunctionProperty
	private RationalNumber eulersNumber;

	/** Spring instantiated bean */
	public LogarithmicFunctionsImpl() {
		setFunctionProperties();
	}

	/** Constructor for instantiating proxy */
	public LogarithmicFunctionsImpl(LogarithmicFunctionsImpl baseObject) {
		LOGGER.info("Create instance proxy instance of class {} using {}", this.getClass().getCanonicalName(), baseObject.toString());
		setFunctionProperties(baseObject);
	}

	public RationalNumber getEulersNumber() {
		return eulersNumber;
	}

	/**
	 * this.getClass().getCanonicalName(), baseObject.toString()); setFunctionProperties(baseObject); }
	 * 
	 * /** exp(x) = 1 + x/1! + x^2/2! + x^3/3! + x^4/4! ... T(i) = x*T(i-1)/i T(0) = 1
	 */
	@Override
	@MathematicalFunction
	public RationalNumber exp(RationalNumber number) {
		BigInteger i = ONE;
		RationalNumber sum = new RationalNumber(ONE);
		RationalNumber t = new RationalNumber(ONE);
		while (i.compareTo(getTAYLORNUMBER()) < 0) {
			t = number.multiply(t).divide(i);
			sum = sum.add(t);
			sum = sum.getNormalizedRationalNumber();
			i = i.add(ONE);
		}
		return sum;
	}

	/**
	 * ln(1 + x) = 0 + x - x^2/2 + x^3/3 - x^4/4 ...
	 */
	@Override
	@MathematicalFunction
	public RationalNumber ln(RationalNumber number) {
		RationalNumber x = number.subtract(1);
		BigInteger i = ONE;
		RationalNumber sum = x;
		RationalNumber t = x;
		boolean subtract = true;
		while (i.compareTo(getTAYLORNUMBER()) < 0) {
			i = i.add(ONE);
			t = t.multiply(x);
			RationalNumber augend = t.divide(i);
			sum = subtract ? sum.subtract(augend) : sum.add(augend);
			sum = sum.getNormalizedRationalNumber();
			subtract = !subtract;
		}
		return sum;
	}

	@Override
	public String toString() {
		return "LogarithmicFunctionsImpl [TAYLORNUMBER=" + getTAYLORNUMBER() + ", eulersNumber=" + eulersNumber + "]";
	}

}

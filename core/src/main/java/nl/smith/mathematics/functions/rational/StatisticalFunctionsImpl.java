package nl.smith.mathematics.functions.rational;

import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.List;

import nl.smith.mathematics.functions.StatisticalFunctions;
import nl.smith.mathematics.functions.annotation.MathematicalFunction;
import nl.smith.mathematics.number.RationalNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticalFunctionsImpl extends StatisticalFunctions<RationalNumber> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticalFunctionsImpl.class);

	private SimpleArithmeticFunctionsImpl simpleArithmeticFunctionsImpl;

	/** Spring instantiated bean */
	@Autowired
	public StatisticalFunctionsImpl(SimpleArithmeticFunctionsImpl simpleArithmeticFunctionsImpl) {
		this.simpleArithmeticFunctionsImpl = simpleArithmeticFunctionsImpl;
		setFunctionProperties();
	}

	/** Constructor for instantiating proxy */
	public StatisticalFunctionsImpl(StatisticalFunctionsImpl baseObject) {
		LOGGER.info("Create instance proxy instance of class {} using {}", this.getClass().getCanonicalName(), baseObject.toString());
		setFunctionProperties(baseObject);
	}

	@Override
	@MathematicalFunction(methodNameAlias = "gemiddelde")
	public RationalNumber average(RationalNumber... numbers) {
		return simpleArithmeticFunctionsImpl.sum(numbers).divide(BigInteger.valueOf(numbers.length));
	}

	@Override
	@MathematicalFunction(methodNameAlias = "gemiddelde_2")
	public RationalNumber average(List<RationalNumber> numbers) {
		return simpleArithmeticFunctionsImpl.sum(numbers).divide(BigInteger.valueOf(numbers.size()));
	}

	@Override
	@MathematicalFunction(methodNameAlias = "deviatie")
	public RationalNumber deviation(RationalNumber... numbers) {
		if (numbers == null || numbers.length == 0) {
			throw new IllegalArgumentException("No numbers specified");
		}

		RationalNumber average = average(numbers);

		RationalNumber sum = new RationalNumber(ZERO);
		for (RationalNumber number : numbers) {
			RationalNumber delta = number.subtract(average);
			sum = sum.add(delta.multiply(delta));
		}

		return sum.divide(BigInteger.valueOf(numbers.length));
	}

	@Override
	public String toString() {
		return "StatisticalFunctionsImpl [simpleArithmeticFunctionsImpl=" + simpleArithmeticFunctionsImpl.toString() + "]";
	}

}

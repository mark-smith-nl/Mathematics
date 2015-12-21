package nl.smith.mathematics.functions.rational;

import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.List;

import nl.smith.mathematics.functions.StatisticalFunctions;
import nl.smith.mathematics.functions.annotation.MathematicalFunction;
import nl.smith.mathematics.number.RationalNumber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticalFunctionsImpl extends StatisticalFunctions<RationalNumber> {

	private final SimpleArithmeticFunctionsImpl simpleArithmeticFunctionsImpl;

	/** Spring instantiated bean */
	@Autowired
	public StatisticalFunctionsImpl(SimpleArithmeticFunctionsImpl simpleArithmeticFunctionsImpl) {
		this.simpleArithmeticFunctionsImpl = simpleArithmeticFunctionsImpl;
	}

	// Constructor for instantiating proxy
	public StatisticalFunctionsImpl(StatisticalFunctionsImpl statisticalFunctionsImpl) {
		simpleArithmeticFunctionsImpl = statisticalFunctionsImpl.simpleArithmeticFunctionsImpl;
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

}

package nl.smith.mathematics.functions.decimal;

import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.List;

import nl.smith.mathematics.functions.StatisticalFunctions;
import nl.smith.mathematics.number.DecimalNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class StatisticalFunctionsImpl extends StatisticalFunctions<DecimalNumber> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticalFunctionsImpl.class);

	private final SimpleArithmeticFunctionsImpl simpleArithmeticFunctionsImpl;

	/** Spring instantiated bean */
	public StatisticalFunctionsImpl(SimpleArithmeticFunctionsImpl simpleArithmeticFunctionsImpl) {
		this.simpleArithmeticFunctionsImpl = simpleArithmeticFunctionsImpl;
	}

	// Constructor for instantiating proxy
	public StatisticalFunctionsImpl(StatisticalFunctionsImpl statisticalFunctionsImpl) {
		simpleArithmeticFunctionsImpl = statisticalFunctionsImpl.simpleArithmeticFunctionsImpl;
	}

	@Override
	public DecimalNumber average(DecimalNumber... numbers) {
		return simpleArithmeticFunctionsImpl.sum(numbers).divide(BigInteger.valueOf(numbers.length));
	}

	@Override
	public DecimalNumber average(List<DecimalNumber> numbers) {
		return simpleArithmeticFunctionsImpl.sum(numbers).divide(BigInteger.valueOf(numbers.size()));
	}

	@Override
	public DecimalNumber deviation(DecimalNumber... numbers) {
		if (numbers == null || numbers.length == 0) {
			throw new IllegalArgumentException("No numbers specified");
		}

		DecimalNumber average = average(numbers);

		DecimalNumber sum = new DecimalNumber(ZERO);
		for (DecimalNumber number : numbers) {
			DecimalNumber delta = number.subtract(average);
			sum = sum.add(delta.multiply(delta));
		}

		return sum.divide(BigInteger.valueOf(numbers.length));
	}

}

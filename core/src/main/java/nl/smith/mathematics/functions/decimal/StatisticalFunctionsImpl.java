package nl.smith.mathematics.functions.decimal;

import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.List;

import nl.smith.mathematics.functions.StatisticalFunctions;
import nl.smith.mathematics.number.DecimalNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class StatisticalFunctionsImpl extends StatisticalFunctions<DecimalNumber> {

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

	@Override
	public String toString() {
		return "StatisticalFunctionsImpl [simpleArithmeticFunctionsImpl=" + simpleArithmeticFunctionsImpl.toString() + "]";
	}

}

package nl.smith.mathematics.functions.decimal;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.List;

import nl.smith.mathematics.constraint.annotation.AssertIsInteger;
import nl.smith.mathematics.constraint.annotation.AssertIsPositive;
import nl.smith.mathematics.constraint.annotation.AssertIsSmaller;
import nl.smith.mathematics.functions.SimpleArithmeticFunctions;
import nl.smith.mathematics.number.DecimalNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleArithmeticFunctionsImpl extends SimpleArithmeticFunctions<DecimalNumber> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArithmeticFunctionsImpl.class);

	/** Spring instantiated bean */
	public SimpleArithmeticFunctionsImpl() {
		setFunctionProperties();
	}

	/** Constructor for instantiating proxy */
	public SimpleArithmeticFunctionsImpl(SimpleArithmeticFunctionsImpl baseObject) {
		LOGGER.info("Create instance proxy instance of class {} using {}", this.getClass().getCanonicalName(), baseObject.toString());
		setFunctionProperties(baseObject);
	}

	@Override
	public DecimalNumber sum(DecimalNumber... numbers) {
		if (numbers == null || numbers.length == 0) {
			throw new IllegalArgumentException("No numbers specified");
		}

		DecimalNumber sum = new DecimalNumber(ZERO);
		for (DecimalNumber number : numbers) {
			sum = sum.add(number);
		}

		return sum;
	}

	@Override
	public DecimalNumber sum(List<DecimalNumber> numbers) {
		if (numbers == null || numbers.size() == 0) {
			throw new IllegalArgumentException("No numbers specified");
		}

		DecimalNumber sum = new DecimalNumber(ZERO);
		for (DecimalNumber number : numbers) {
			sum = sum.add(number);
		}

		return sum;
	}

	@Override
	public DecimalNumber faculty(
			@AssertIsInteger @AssertIsPositive(errorFormat = "De aanname dat het getal {} positief is is niet correct") @AssertIsSmaller(value = "100") DecimalNumber number) {
		if (number == null || !number.isNaturalNumber()) {
			throw new IllegalArgumentException("Error in argument in faculty function. Argument must be a not negative natural number");
		}

		BigInteger naturalNumber = number.getIntegerPartAsBigInteger();
		BigInteger product = ONE;
		while (!naturalNumber.equals(ZERO)) {
			product = product.multiply(naturalNumber);
			naturalNumber = naturalNumber.subtract(ONE);
		}

		return new DecimalNumber(product);
	}

	@Override
	public String toString() {
		return "SimpleArithmeticFunctionsImpl []";
	}
}

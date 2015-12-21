package nl.smith.mathematics.functions.rational;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.List;

import nl.smith.mathematics.constraint.annotation.AssertIsInteger;
import nl.smith.mathematics.constraint.annotation.AssertIsPositive;
import nl.smith.mathematics.constraint.annotation.AssertIsSmaller;
import nl.smith.mathematics.constraint.annotation.MethodConstraint;
import nl.smith.mathematics.functions.SimpleArithmeticFunctions;
import nl.smith.mathematics.functions.annotation.MathematicalFunction;
import nl.smith.mathematics.number.RationalNumber;

import org.springframework.stereotype.Component;

@Component
@MethodConstraint(numberType = RationalNumber.class)
public class SimpleArithmeticFunctionsImpl extends SimpleArithmeticFunctions<RationalNumber> {

	/** Spring instantiated bean */
	public SimpleArithmeticFunctionsImpl() {

	}

	// Constructor for instantiating proxy
	public SimpleArithmeticFunctionsImpl(SimpleArithmeticFunctionsImpl simpleArithmeticFunctionsImpl) {
		// No values are being transferred to the proxy
	}

	@Override
	@MathematicalFunction
	public RationalNumber sum(RationalNumber... numbers) {
		if (numbers == null || numbers.length == 0) {
			throw new IllegalArgumentException("No numbers specified");
		}

		RationalNumber sum = new RationalNumber(ZERO);
		for (RationalNumber number : numbers) {
			sum = sum.add(number);
		}

		return sum;
	}

	@Override
	@MathematicalFunction(methodNameAlias = "suml")
	public RationalNumber sum(List<RationalNumber> numbers) {
		if (numbers == null || numbers.size() == 0) {
			throw new IllegalArgumentException("No numbers specified");
		}

		RationalNumber sum = new RationalNumber(ZERO);
		for (RationalNumber number : numbers) {
			sum = sum.add(number);
		}

		return sum;
	}

	@Override
	@MathematicalFunction(methodNameAlias = "Faculteit")
	public RationalNumber faculty(
			@AssertIsInteger @AssertIsPositive(errorFormat = "De aanname dat het getal {} positief is is niet correct") @AssertIsSmaller(value = "100") RationalNumber number) {
		if (number == null || !number.isNaturalNumber()) {
			throw new IllegalArgumentException("Error in argument in faculty function. Argument must be a not negative natural number");
		}

		BigInteger naturalNumber = number.getNormalizedRationalNumber().getNumerator();
		BigInteger product = ONE;
		while (!naturalNumber.equals(ZERO)) {
			product = product.multiply(naturalNumber);
			naturalNumber = naturalNumber.subtract(ONE);
		}

		return new RationalNumber(product);
	}

}

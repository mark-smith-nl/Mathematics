package nl.smith.mathematics;

import nl.smith.mathematics.number.RationalNumber;

/**
 * This class is being used by the test class {@link NumericMethodSignatureTest}
 * 
 * @author nb0008
 * 
 */
public class NumericFunction {

	public RationalNumber functionOne() {
		return null;
	}

	public RationalNumber functionTwo(@SuppressWarnings("unused")
	RationalNumber... number) {
		return null;
	}

	public RationalNumber functionThree(@SuppressWarnings("unused")
	RationalNumber numberOne, @SuppressWarnings("unused")
	RationalNumber numberTwo, @SuppressWarnings("unused")
	RationalNumber numberThree) {
		return null;
	}

	public RationalNumber[] functionOneArray() {
		return null;
	}

	public RationalNumber[] functionTwoArray(@SuppressWarnings("unused")
	RationalNumber... number) {
		return null;
	}

	public RationalNumber[] functionThreeArray(@SuppressWarnings("unused")
	RationalNumber numberOne, @SuppressWarnings("unused")
	RationalNumber numberTwo, @SuppressWarnings("unused")
	RationalNumber numberThree) {
		return null;
	}

}

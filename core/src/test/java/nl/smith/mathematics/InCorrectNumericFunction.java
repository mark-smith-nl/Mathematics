package nl.smith.mathematics;

import nl.smith.mathematics.number.RationalNumber;

/**
 * This class is being used by the test class {@link NumericMethodSignatureTest}
 * 
 * @author nb0008
 * 
 */
public class InCorrectNumericFunction {

	public void functionOne() {
		return;
	}

	public RationalNumber functionTwo(@SuppressWarnings("unused")
	String... number) {
		return null;
	}

	public RationalNumber functionThree(@SuppressWarnings("unused")
	RationalNumber numberOne, @SuppressWarnings("unused")
	Object numberTwo, @SuppressWarnings("unused")
	RationalNumber numberThree) {
		return null;
	}

	public static RationalNumber[] functionOneArray() {
		return null;
	}

	/** Method is made private and is used in a test using reflection */
	@SuppressWarnings({ "unused", "static-method" })
	private RationalNumber[] functionTwoArray(
			RationalNumber... number) {
		return null;
	}

	/** Method is made protected and is used in a test using reflection */
	protected RationalNumber[] functionThreeArray(@SuppressWarnings("unused")
	RationalNumber numberOne, @SuppressWarnings("unused")
	RationalNumber numberTwo, @SuppressWarnings("unused")
	RationalNumber numberThree) {
		return null;
	}

	public String[] functionOneInt() {
		return null;
	}

}

package nl.smith.mathematics.factory.constant;

/**
 * Names of groups defined in {@link ArithmeticComponentRegularExpression} If the component is associated with a corresponding method declared in the NumberOperations interface
 * this methodName is set
 * 
 * @author Mark Smith
 *
 */
public enum ArithmeticComponentName {
	// Unary operator
	NEGATION("negate"),
	UNSIGNED_INTEGER,
	NON_FUNCTIONAL,
	INTEGER_NUMBER, DECIMAL_NUMBER, DECIMAL_NUMBER_WITH_INFINITE_DECIMALS, SCIENTIFIC_NUMBER, SCIENTIFIC_NUMBER_WITH_INFINITE_DECIMALS,
	FRACTION_NON_REPEATING_BLOCK, FRACTION_REPEATING_BLOCK,
	DIMENSION,
	EXPONENT,
	ANY_NUMBER,
	ANY_NUMBER_EQUIVALENT,
	SUBEXPRESSION, FUNCTION, VARIABLE, NAME,
	// Binary operators
	BINARY_OPERATOR,
	ADDITION("add"), SUBTRACTION("subtract"), MULTIPLICATION("multiply"), DIVISION("divide"), POWER,
	UNARY_OPERATION;

	private final String methodName;

	private ArithmeticComponentName() {
		this.methodName = null;
	}

	private ArithmeticComponentName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodName() {
		return methodName;
	}

}

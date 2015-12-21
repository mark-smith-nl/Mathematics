package nl.smith.mathematics.utility;

public enum ErrorMessages {
	IMPLEMENT_ERROR_MESSAGE("Implement error message %s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: all reserved characters</li>
	 * <li>%s: positions used reserved characters</li>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	ILLEGAL_USAGE_OF_RESERVED_CHARACTERS(
			"Expression string contains a character from the reserved character set\n"
					+ "Please remove these characters\n"
					+ "Reserved character(s): %s\n"
					+ "Positions (zero based): %s\n" +
					"%s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%c: unexpected dimension token</li>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	UNEXPECTED_DIMENSION_TOKEN_FOUND("Unexpected dimension token found: '%c'\n"
			+ "Could not find corresponding enclosing expression.\n"
			+ "%s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%c: unexpected end token</li>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	UNEXPECTED_END_TOKEN_FOUND("Unexpected end token: '%c'\n"
			+ "Could not find corresponding open token.\n"
			+ "%s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%c: expected end token</li>
	 * <li>%c: actual end token</li>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	EXPRESSION_NOT_PROPERLY_CLOSED("Wrong end token. Expected: %c Actual: %c\n%s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: expected end token(s)</li>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	UNCLOSED_EXPRESSIONS("Missing end token(s). Expected: %s at end of the expression\n%s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: input string which could not be transformed into a number</li>
	 * </ol>
	 */
	NOT_A_NUMBER(
			"The string '%s' can not be interpreted as a number.\n"
					+ "Please do not use redundant '+' signs\n"
					+ "Please do not use multiple '-' signs\n"
					+ "Please do not use redundant leading/trailing zero's in integer and fractional parts\n"
					+ "Please enclose repeating fractional parts within curly braces ans append with character 'R'\n"
					+ "If using the scientific number notation please remind the following:\n"
					+ "The absolute value of the mantissa should be smaller than 10 and not be smaller than one"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: method alias which can not be found</li>
	 * <li>%d: number of arguments</li>
	 * <li>%d: type of arguments</li>
	 * </ol>
	 */
	UNKNOWN_METHOD(
			"The method with alias %s and %d argument(s) of type %s can not be found"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: annotation</li>
	 * <li>%s: class</li>
	 * <li>%d: method name</li>
	 * </ol>
	 */
	ANNOTATED_METHOD_NOT_PUBLIC_INSTANCE_METHOD(
			"The %s annotated %s.%s method is not a public instance method"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: actual return type</li>
	 * <li>%s: expected return type</li>
	 * </ol>
	 */
	WRONG_METHOD_RETURN_TYPE(
			"The method's return type class %s is not of type of %s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	EXPRESSION_CONTAINS_UNFINALIZED_ELEMENTS(
			"Expression contains unfinalized elements.\n"
					+ "%s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	EXPRESSION_EXPECTED(
			"Expression expected but none encountered.\n"
					+ "The content you provide must result in a number when parsed.\n"
					+ "%s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%d: expected position at which the expression should be added</li>
	 * <li>%d: actual position at which the expression is added</li>
	 * <li>
	 * </ol>
	 */
	WRONG_START_POSITION_ADDED_EXPRESSION(
			"Expression added at wrong position.\n"
					+ "Expected position: %d.\n"
					+ "Actual position: %d.\n"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	EXPRESSION_DOES_NOT_START_WITH_UNARY_OPERATION(
			"Expression does not start with a unary operation.\n"
					+ "%s.\n"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: Unexpected content</li>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 * */
	UNEXPECTED_CONTENT_OPERATION(
			"Unexpected content '%s'.\n"
					+ "%s.\n"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: Method not founf</li>
	 * </ol>
	 */
	METHOD_NOT_FOUND(
			"No method found to match '%s'");

	private final String format;

	private ErrorMessages(String format) {
		this.format = format;
	}

	public String getFormattedErrorMessage(Object... args) {
		return "\n" + String.format(format, args);
	}

}

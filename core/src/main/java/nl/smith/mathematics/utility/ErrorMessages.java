package nl.smith.mathematics.utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ErrorMessages {

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: error messages</li>
	 * </ol>
	 */
	IMPLEMENT_ERROR_MESSAGE("Implement error message %s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: method argument name</li>
	 * </ol>
	 */
	METHOD_ARGUMENT_CAN_NOT_BE_NULL("The value of method argument '%s' can not be null"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: canonical class name</li>
	 * </ol>
	 */
	CAN_NOT_LOAD_CLASS("can not load class %s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: resource name</li>
	 * </ol>
	 */
	CAN_NOT_OPEN_RESOURCE("Can not open resource '%s'"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: modifiers</li>
	 * <li>%s: method return type</li>
	 * <li>%s: canonical name of class</li>
	 * <li>%s: method name</li>
	 * <li>%s: method argument(s)</li>
	 * </ol>
	 */
	REQUIRED_METHOD_NOT_RETRIEVED("Method %s %s %s.(%s)' not found"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: canonical name of class</li>
	 * <li>%s: method argument(s)</li>
	 * </ol>
	 */
	REQUIRED_CONSTRUCTOR_NOT_RETRIEVED("Constructor %s.(%s)' not found"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: string value to be parsed</li>
	 * <li>%s: class of instance to be constructed during parsing of the string</li>
	 * </ol>
	 */
	STRING_NUMBER_PARSE_ERROR("Can not parse the string value %s into an instance of type %s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: class of instance to be constructed during parsing of a string</li>
	 * </ol>
	 */
	CONVERSION_OF_STRING_TO_SPECIFIED_OBJECT_NOT_SUPPORTED("The conversion of a string to an instance ot type %s is not supported."),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: all reserved characters</li>
	 * <li>%s: positions used reserved characters</li>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	ILLEGAL_USAGE_OF_RESERVED_CHARACTERS("Expression string contains a character from the reserved character set\n" + "Please remove these characters\n" + "Reserved character(s): %s\n"
			+ "Positions (zero based): %s\n" + "%s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%c: unexpected dimension token</li>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	UNEXPECTED_DIMENSION_TOKEN_FOUND("Unexpected dimension token found: '%c'\n" + "Could not find corresponding enclosing expression.\n" + "%s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%c: unexpected end token</li>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	UNEXPECTED_END_TOKEN_FOUND("Unexpected end token: '%c'\n" + "Could not find corresponding open token.\n" + "%s"),

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
	NOT_A_NUMBER("The string '%s' can not be interpreted as a number.\n" + "Please do not use redundant '+' signs\n" + "Please do not use multiple '-' signs\n"
			+ "Please do not use redundant leading/trailing zero's in integer and fractional parts\n"
			+ "Please enclose repeating fractional parts within curly braces ans append with character 'R'\n" + "If using the scientific number notation please remind the following:\n"
			+ "The absolute value of the mantissa should be smaller than 10 and not be smaller than one"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: method alias which can not be found</li>
	 * <li>%d: number of arguments</li>
	 * <li>%d: type of arguments</li>
	 * </ol>
	 */
	UNKNOWN_METHOD("The method with alias %s and %d argument(s) of type %s can not be found"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: annotation</li>
	 * <li>%s: class</li>
	 * <li>%d: method name</li>
	 * </ol>
	 */
	ANNOTATED_METHOD_NOT_PUBLIC_INSTANCE_METHOD("The %s annotated %s.%s method is not a public instance method"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: actual return type</li>
	 * <li>%s: expected return type</li>
	 * </ol>
	 */
	WRONG_METHOD_RETURN_TYPE("The method's return type class %s is not of type of %s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	EXPRESSION_CONTAINS_UNFINALIZED_ELEMENTS("Expression contains unfinalized elements.\n" + "%s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	EXPRESSION_EXPECTED("Expression expected but none encountered.\n" + "The content you provide must result in a number when parsed.\n" + "%s"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%d: expected position at which the expression should be added</li>
	 * <li>%d: actual position at which the expression is added</li>
	 * <li>
	 * </ol>
	 */
	WRONG_START_POSITION_ADDED_EXPRESSION("Expression added at wrong position.\n" + "Expected position: %d.\n" + "Actual position: %d.\n"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 */
	EXPRESSION_DOES_NOT_START_WITH_UNARY_OPERATION("Expression does not start with a unary operation.\n" + "%s.\n"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: Unexpected content</li>
	 * <li>%s: (concatenated, initialExpression and trimmedExpression) input string(s) supplied with carets to point to positions of interest</li>
	 * </ol>
	 * */
	UNEXPECTED_CONTENT_OPERATION("Unexpected content '%s'.\n" + "%s.\n"),

	/**
	 * Formatter string contains the following place holder(s) specifying:<br>
	 * <ol>
	 * <li>%s: Method not founf</li>
	 * </ol>
	 */
	METHOD_NOT_FOUND("No method found to match '%s'");

	private final String format;

	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMessages.class);

	private ErrorMessages(String format) {
		this.format = format;
	}

	public String getFormattedErrorMessage(Object... args) {
		return "\n" + String.format(format, args);
	}

	/**
	 * Throws the specified exception wrapping the specified cause and uses the auxiliary arguments to construct an error message.
	 * 
	 * @param exceptionClazz
	 *            the class the thrown exception is an instance of
	 * @param cause
	 *            the cause of the exception
	 * @param args
	 *            the arguments used to create an error message
	 */
	public <T extends RuntimeException> void throwUncheckedException(Class<T> exceptionClazz, Throwable cause, Object... args) {
		String errorMessage = this.getFormattedErrorMessage(args);
		LOGGER.warn(errorMessage);

		throw getUncheckedException(exceptionClazz, cause, errorMessage);
	}

	/**
	 * Throws the specified exception and uses the auxiliary arguments to construct an error message.
	 * 
	 * @param exceptionClazz
	 *            the class the thrown exception is an instance of
	 * @param args
	 *            the arguments used to create an error message
	 */
	public <T extends RuntimeException> void throwUncheckedException(Class<T> exceptionClazz, Object... args) {
		throwUncheckedException(exceptionClazz, null, args);
	}

	private <T extends RuntimeException> T getUncheckedException(Class<T> exceptionClazz, Throwable cause, String errorMessage) {
		T exception = null;
		Constructor<T> constructor = null;
		try {
			if (cause == null) {
				constructor = exceptionClazz.getConstructor(new Class<?>[] { String.class });
				exception = constructor.newInstance(errorMessage);
			} else {
				constructor = exceptionClazz.getConstructor(new Class<?>[] { String.class, Throwable.class });
				exception = constructor.newInstance(errorMessage, cause);
			}
		} catch (IllegalArgumentException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}

		return exception;
	}

}

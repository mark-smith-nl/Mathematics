package nl.smith.mathematics.factory.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.smith.mathematics.factory.Expression;

import org.apache.commons.lang.StringUtils;

/**
 * Enums describing arithmetic expression or its components<br>
 * The enum constructor uses the following arguments:
 * <ul>
 * <li>Description</li>
 * <li>The regular expression describing the arithmetic expression or its components. The regular expression can make use of regular expressions defined in predefined enums. The
 * placeholder used for these expressions is '%s'. The replacement values are provided as the last arguments
 * <li>The names (see: {@link ArithmeticComponentName}) of the groups in the regular expression</li>
 * <li>Replacement values for the '%s' placeholders in the regular expression</li>
 * </ul>
 * 
 * @author Mark Smith
 *
 */
public enum ArithmeticComponentRegularExpression {

	CIPHRE(
			"Ciphre (0, 1, 2 ... 9)",
			"\\d",
			null),

	NON_ZERO_CIPHRE(
			"Non zero ciphre (1, 2 ... 9)",
			"[%s&&[^0]]",
			null,
			CIPHRE),

	UNARY_NEGATION_OPERATOR(
			"Minus sign",
			"\\-",
			null),

	POSITIVE_INTEGER(
			"Positive integer number (1, 2 ...)",
			"%s%s*",
			null,
			NON_ZERO_CIPHRE, CIPHRE),

	UNSIGNED_INTEGER(
			"Unsigned integer number (0, 1, 2 ...)",
			"(%s|0)",
			Arrays.asList(ArithmeticComponentName.UNSIGNED_INTEGER),
			POSITIVE_INTEGER),

	FRACTION_NON_REPEATING(
			"Finite number of digits after a decimal point. Sequence (1..N) of decimals",
			"%s*%s",
			null,
			CIPHRE, NON_ZERO_CIPHRE),

	FRACTION_REPEATING(
			"Repeating block after decimal point. A repeating block has to contain at least one not '0' cipher",
			"%s*%s+%s*",
			null,
			CIPHRE,
			NON_ZERO_CIPHRE,
			CIPHRE),

	FRACTION(
			"Fraction after decimal point. Contains a sequence (0..N) of decimals (non repeating fraction) and a sequence (1..N) of decimals with at least one not zero ciphre (repeating fraction) enclosed in {} en terminated by 'R'",
			"(%s*)\\{(%s)\\}R",
			Arrays.asList(
					ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK,
					ArithmeticComponentName.FRACTION_REPEATING_BLOCK),
			CIPHRE,
			FRACTION_REPEATING),

	INTEGER(
			"Integer number (... -2, -1, 0, 1, 2, ...)",
			"%s?%s|0",
			null,
			UNARY_NEGATION_OPERATOR,
			POSITIVE_INTEGER),

	DECIMAL_NUMBER(
			"Normal decimal number with finite fractional part",
			"(%s?%s)\\.(%s)",
			Arrays.asList(
					ArithmeticComponentName.INTEGER_NUMBER,
					ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK),
			UNARY_NEGATION_OPERATOR,
			UNSIGNED_INTEGER,
			FRACTION_NON_REPEATING),

	DECIMAL_NUMBER_WITH_FRACTION_REPEATING(
			"decimal number with a fraction with infinite ciphres",
			"(%s?%s)\\.%s",
			Arrays.asList(ArithmeticComponentName.INTEGER_NUMBER),
			UNARY_NEGATION_OPERATOR,
			UNSIGNED_INTEGER,
			FRACTION),

	SCIENTIFIC_NUMBER(
			"Scientific number",
			"(%s?%s)(\\.(%s))?E(%s)",
			Arrays.asList(
					ArithmeticComponentName.INTEGER_NUMBER,
					ArithmeticComponentName.NON_FUNCTIONAL,
					ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK,
					ArithmeticComponentName.EXPONENT),
			UNARY_NEGATION_OPERATOR, NON_ZERO_CIPHRE, FRACTION_NON_REPEATING, INTEGER),

	SCIENTIFIC_NUMBER_WITH_FRACTION_REPEATING(
			"Scientific number with infinite numbers in fractional part of the mantissa",
			"(%s?%s)\\.%sE(%s)",
			Arrays.asList(
					ArithmeticComponentName.INTEGER_NUMBER,
					ArithmeticComponentName.EXPONENT),
			UNARY_NEGATION_OPERATOR,
			NON_ZERO_CIPHRE,
			FRACTION,
			INTEGER),

	// TODO Test
	ANY_NUMBER(
			"Any number (integer, decimal, decimal with infinite decimals, scientific, scientific with infinite decimals)",
			"(%s)|(%s)|(%s)|(%s)|(%s)",
			Arrays.asList(
					ArithmeticComponentName.SCIENTIFIC_NUMBER_WITH_INFINITE_DECIMALS,
					ArithmeticComponentName.SCIENTIFIC_NUMBER,
					ArithmeticComponentName.DECIMAL_NUMBER_WITH_INFINITE_DECIMALS,
					ArithmeticComponentName.DECIMAL_NUMBER,
					ArithmeticComponentName.INTEGER_NUMBER),
			SCIENTIFIC_NUMBER_WITH_FRACTION_REPEATING,
			SCIENTIFIC_NUMBER,
			DECIMAL_NUMBER_WITH_FRACTION_REPEATING,
			DECIMAL_NUMBER,
			INTEGER),

	// TODO Test
	NAME(
			"Name of a variable or function",
			"[a-zA-Z_][a-zA-Z_0-9]*",
			null),

	// TODO Test
	EXPRESSION_IDENTIFIER(
			"Token identifying an expression to be inserted",
			"\\" + Expression.EXPRESSION_PLACE_HOLDER,
			null),

	// TODO Test
	EXPRESSION_IDENTIFIER_WITH_DIMENSION(
			"Token identifying an expression to be inserted with number of elements",
			"\\" + Expression.EXPRESSION_PLACE_HOLDER + "(%s)",
			Arrays.asList(ArithmeticComponentName.DIMENSION),
			POSITIVE_INTEGER),

	// TODO Test
	UNARY_OPERATION(
			"Unary expression",
			"(%s)?((%s)|((%s)(%s))|((%s))|(%s))",
			Arrays.asList(
					ArithmeticComponentName.NEGATION,
					ArithmeticComponentName.ANY_NUMBER_EQUIVALENT,
					ArithmeticComponentName.SUBEXPRESSION,
					ArithmeticComponentName.FUNCTION,
					ArithmeticComponentName.NAME,
					ArithmeticComponentName.SUBEXPRESSION,
					ArithmeticComponentName.VARIABLE,
					ArithmeticComponentName.NAME,
					ArithmeticComponentName.ANY_NUMBER
					),
			UNARY_NEGATION_OPERATOR,
			EXPRESSION_IDENTIFIER,
			NAME,
			EXPRESSION_IDENTIFIER,
			NAME,
			ANY_NUMBER),

	// TODO Test
	BINARY_ADDITION_OPERATOR(
			"Add operator +",
			"\\+",
			null),

	// TODO Test
	BINARY_SUBTRACT_OPERATOR(
			"Subtract operator -",
			"\\-",
			null),

	// TODO Test
	BINARY_MULTIPLY_OPERATOR(
			"Multiply operator *",
			"\\*",
			null),

	// TODO Test
	BINARY_DIVIDE_OPERATOR(
			"Divide operator /",
			"/",
			null),

	// TODO Test
	BINARY_POWER_OPERATOR(
			"Power operator ^",
			"\\^",
			null),

	// TODO Test
	BINARY_OPERATOR(
			"Arithmetic binary operators",
			"(%s)|(%s)|(%s)|(%s)|(%s)",
			Arrays.asList(
					ArithmeticComponentName.ADDITION,
					ArithmeticComponentName.SUBTRACTION,
					ArithmeticComponentName.MULTIPLICATION,
					ArithmeticComponentName.DIVISION,
					ArithmeticComponentName.POWER),
			BINARY_ADDITION_OPERATOR,
			BINARY_SUBTRACT_OPERATOR,
			BINARY_MULTIPLY_OPERATOR,
			BINARY_DIVIDE_OPERATOR,
			BINARY_POWER_OPERATOR),

	// TODO Test
	BINARY_OPERATION(
			"Unary expression prepended with a binary operator",
			"(%s)(%s)",
			Arrays.asList(ArithmeticComponentName.BINARY_OPERATOR, ArithmeticComponentName.UNARY_OPERATION),
			BINARY_OPERATOR, UNARY_OPERATION
	);

	private final String description;

	private final String regex;

	private final List<ArithmeticComponentStructure> arithmeticComponentStructures;

	private final Pattern pattern;

	private ArithmeticComponentRegularExpression(
			String description,
			String regex,
			List<ArithmeticComponentName> namedNumberParts,
			ArithmeticComponentRegularExpression... predefinedArithmeticComponentRegularExpressions) {

		checkValidity(regex, namedNumberParts, predefinedArithmeticComponentRegularExpressions);

		this.description = description;
		this.regex = regex;
		pattern = Pattern.compile(getCompoundRegex(regex, predefinedArithmeticComponentRegularExpressions));
		arithmeticComponentStructures = getRegularExpressionNumberStructures(regex, namedNumberParts, predefinedArithmeticComponentRegularExpressions);
	}

	private void checkValidity(String regex, List<ArithmeticComponentName> namedNumberParts, ArithmeticComponentRegularExpression... predefinedRegularExpressionNumbers) {
		if (StringUtils.isBlank(regex)) {
			throw new IllegalStateException(String.format("\nNo regular expression specified"));
		}

		// Check if the number of specified named groups equal the number of braces
		int numberOfGroups = StringUtils.countMatches(regex, "(");
		int numberOfNamedNumberParts = namedNumberParts == null ? 0 : namedNumberParts.size();
		if (numberOfGroups != numberOfNamedNumberParts) {
			throw new IllegalStateException(String.format("\nThe number of number parts in %s is not correct\nExpected %d\nActual: %d\n%s", this.name(), numberOfGroups,
					numberOfNamedNumberParts, regex));
		}

		// Check if the number of place holders equal the number of provided values
		int numberOfPlaceHolders = StringUtils.countMatches(regex, "%s");
		int numberOfSubElements = predefinedRegularExpressionNumbers == null ? 0 : predefinedRegularExpressionNumbers.length;
		if (numberOfPlaceHolders != numberOfSubElements) {
			throw new IllegalStateException(String.format("\nThe number number placeholders in %s is not correct\nExpected %d\nActual: %d\n%s", this.name(), numberOfSubElements,
					numberOfPlaceHolders, regex));

		}
	}

	private static List<ArithmeticComponentStructure> getRegularExpressionNumberStructures(String regex, List<ArithmeticComponentName> namedNumberParts,
			ArithmeticComponentRegularExpression... predefinedRegularExpressionNumbers) {
		List<ArithmeticComponentStructure> regularExpressionNumberStructures = new ArrayList<>();
		int groupIndex = 0;
		int numberOfgroupsAdded = 0;
		int placeHolderIndex = 0;
		ArithmeticComponentStructure current = null;

		List<ArithmeticComponentStructure> stack = new ArrayList<>();

		Pattern pattern = Pattern.compile("\\(|\\)|%s");
		Matcher matcher = pattern.matcher(regex);
		while (matcher.find()) {
			if ("(".equals(matcher.group())) {
				groupIndex++;
				numberOfgroupsAdded++;
				ArithmeticComponentStructure head = new ArithmeticComponentStructure(namedNumberParts.get(groupIndex - 1), numberOfgroupsAdded);
				if (current == null) {
					regularExpressionNumberStructures.add(head);
				} else {
					current.add(head);
				}
				current = head;
				stack.add(current);
			} else if (")".equals(matcher.group())) {
				stack.remove(current);
				current = stack.isEmpty() ? null : stack.get(stack.size() - 1);
			} else if ("%s".equals(matcher.group())) {
				List<ArithmeticComponentStructure> predefinedStructures = predefinedRegularExpressionNumbers[placeHolderIndex]
						.getArithmeticComponentStructures();
				List<ArithmeticComponentStructure> copyOffpredefinedStructures = new ArrayList<ArithmeticComponentStructure>();

				for (ArithmeticComponentStructure predefinedStructure : predefinedStructures) {
					copyOffpredefinedStructures.add(new ArithmeticComponentStructure(predefinedStructure, numberOfgroupsAdded));
				}

				numberOfgroupsAdded += ArithmeticComponentStructure.getNumberOfElements(predefinedStructures);

				if (stack.isEmpty()) {
					regularExpressionNumberStructures.addAll(copyOffpredefinedStructures);
				} else {
					current.add(copyOffpredefinedStructures);
				}
				placeHolderIndex++;
			}
		}

		return regularExpressionNumberStructures;
	}

	private static String getCompoundRegex(String regex, ArithmeticComponentRegularExpression[] constant) {
		String[] subRegex = new String[constant.length];

		for (int i = 0; i < constant.length; i++) {
			subRegex[i] = constant[i].regex();
		}

		String compoundRegex = regex;
		if (subRegex.length > 0) {
			compoundRegex = String.format(regex, (Object[]) subRegex);
		}

		return compoundRegex;
	}

	public String getInitialRegex() {
		return regex;
	}

	public String regex() {
		return pattern.pattern();
	}

	public String getDescription() {
		return description;
	}

	public Pattern pattern() {
		return pattern;
	}

	public List<ArithmeticComponentStructure> getArithmeticComponentStructures() {
		return arithmeticComponentStructures;
	}

	public int getGroupCount() {
		return (StringUtils.countMatches(pattern.pattern(), "("));
	}

	// TODO Implement regularExpressionNumberStructures.toString()
	@Override
	public String toString() {
		StringBuffer toString = new StringBuffer();
		toString.append("Name: " + name() + "\n")
				.append("Regex: " + pattern.pattern() + "\n")
				.append("Description: " + description + "\n")
				.append(ArithmeticComponentStructure.toString(arithmeticComponentStructures));

		return toString.toString();
	}
}

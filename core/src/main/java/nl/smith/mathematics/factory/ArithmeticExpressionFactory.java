package nl.smith.mathematics.factory;

import static nl.smith.mathematics.factory.ExpressionStringAggregate.ShowCaretString.IN_BOTH_EXPRESSIONS;
import static nl.smith.mathematics.utility.ErrorMessages.EXPRESSION_EXPECTED;
import static nl.smith.mathematics.utility.ErrorMessages.ILLEGAL_USAGE_OF_RESERVED_CHARACTERS;
import static nl.smith.mathematics.utility.ErrorMessages.UNCLOSED_EXPRESSIONS;
import static nl.smith.mathematics.utility.ErrorMessages.UNEXPECTED_DIMENSION_TOKEN_FOUND;
import static nl.smith.mathematics.utility.ErrorMessages.UNEXPECTED_END_TOKEN_FOUND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.smith.mathematics.factory.stack.ArithmeticExpressionStack;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for parsing raw expression strings into an
 * {@link ArithmeticExpressionStack}
 */
public final class ArithmeticExpressionFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArithmeticExpressionFactory.class);

	public final Map<Character, Character> aggregationTokenPairs;

	// TODO Javadoc
	private final Pattern patternSplitTokens;

	private final ArithmeticComponentResolver arithmeticComponentResolver;

	public ArithmeticExpressionFactory(ArithmeticComponentResolver arithmeticComponentResolver) {
		if (arithmeticComponentResolver == null) {
			throw new IllegalArgumentException("\nNo arithmeticComponentResolver specified");
		}

		this.arithmeticComponentResolver = arithmeticComponentResolver;

		aggregationTokenPairs = makeAggregationTokenPairMap();
		patternSplitTokens = getPatternSplitTokens(aggregationTokenPairs);
	}

	private static Map<Character, Character> makeAggregationTokenPairMap() {
		Map<Character, Character> identifierPairs = new HashMap<>();

		identifierPairs.put('{', '}');
		identifierPairs.put('(', ')');
		identifierPairs.put('[', ']');
		return Collections.unmodifiableMap(identifierPairs);
	}

	private static Pattern getPatternSplitTokens(Map<Character, Character> compoundExpressionAggregationTokenPairs) {
		String regex = "[";
		regex += MultiDimensionalExpression.DIMENSION_SEPERATOR;
		for (Entry<Character, Character> entry : compoundExpressionAggregationTokenPairs.entrySet()) {
			regex += "\\" + entry.getKey() + "\\" + entry.getValue();
		}

		regex += "]";

		return Pattern.compile(regex);
	}

	public ArithmeticExpressionStack buildArithmeticExpressionStack(String rawExpression) {
		ExpressionStringAggregate expressionStringAggregate = new ExpressionStringAggregate(rawExpression);

		Expression expression = makeExpression(expressionStringAggregate);

		return null;
		// return buildArithmeticExpressionStack(expression);
	}

	// private ArithmeticExpressionStack
	// buildArithmeticExpressionStack(Expression expression) {
	// ArithmeticExpressionStack arithmeticExpressionStack =
	// buildArithmeticExpressionStackStub(expressionStringAggregate,
	// expression.getContentWithoutAggregationTokens());
	//
	// List<SubExpressionStack> subExpressionStacks =
	// arithmeticExpressionStack.getSubExpressionStacks();
	// List<Expression> subExpressions = expression.getSubExpressions();
	// for (int i = 0; i < subExpressionStacks.size(); i++) {
	// subExpressionStacks.get(i).setStack(buildArithmeticExpressionStack(expressionStringAggregate,
	// subExpressions.get(i)));
	// }
	//
	// return arithmeticExpressionStack;
	// }

	// TODO Implement
	@SuppressWarnings("unused")
	private ArithmeticExpressionStack buildArithmeticExpressionStackStub(Expression expression) {

		return arithmeticComponentResolver.buildArithmeticExpressionStack(expression);

	}

	/** Protected for test purposes */
	protected Expression makeExpression(ExpressionStringAggregate expressionStringAggregate) {
		LOGGER.info("Digested expression aggregate:\n{}", expressionStringAggregate.toString());

		validateNoUsageOfReservedCharacters(expressionStringAggregate);

		String trimmedExpression = expressionStringAggregate.getTrimmedExpression();

		int startPosition = 0;
		int endPosition = 0;
		String content;

		Expression currentExpression = null;
		List<Expression> expressionStack = new ArrayList<Expression>();

		MultiDimensionalExpression currentMultiDimensionalExpression = null;
		List<MultiDimensionalExpression> multiDimensionalExpressionStack = new ArrayList<>();

		Matcher matcher = patternSplitTokens.matcher(trimmedExpression);
		while (matcher.find()) {
			char token = matcher.group().charAt(0);
			startPosition = matcher.start();
			content = trimmedExpression.substring(endPosition, startPosition);
			currentExpression = getCurrentExpressionWithAddedContent(currentExpression, expressionStack, expressionStringAggregate, endPosition, content);
			endPosition = startPosition + 1;

			if (aggregationTokenPairs.containsKey(token)) {
				// Encountered a start token signaling the a new
				// multiDimensionalExpression should be created
				currentMultiDimensionalExpression = new MultiDimensionalExpression(expressionStringAggregate, startPosition, token,
						aggregationTokenPairs.get(token));
				multiDimensionalExpressionStack.add(currentMultiDimensionalExpression);
				currentExpression = null;
			} else {
				// Encountered a token signaling an expression should be
				// terminated and added its parent i.e. the
				// currentMultiDimensionalExpression
				// There should have been expression
				validateCurrentExpressionNotNull(expressionStringAggregate, startPosition, currentExpression);
				currentExpression.finalize();
				expressionStack.remove(currentExpression);

				// There should have been an multidimensional expression which
				// the currentExpression should be added to
				validateCurrentMultiDimensionalExpressionNotNull(expressionStringAggregate, startPosition, currentMultiDimensionalExpression, token);
				currentMultiDimensionalExpression.addExpression(currentExpression);

				if (MultiDimensionalExpression.DIMENSION_SEPERATOR == token) {
					// Encountered a dimension token signaling the dimension of
					// the currentMultiDimensionalExpression should be increased
					// Reset the currentExpression
					currentExpression = null;
				} else {
					// Encountered an end token signaling the
					// currentMultiDimensionalExpression should be closed
					currentMultiDimensionalExpression.addEndTokenAndFinalize(token);
					multiDimensionalExpressionStack.remove(currentMultiDimensionalExpression);

					if (expressionStack.size() == 0) {
						currentExpression = new Expression(expressionStringAggregate, currentMultiDimensionalExpression);
						expressionStack.add(currentExpression);
					} else {
						currentExpression = expressionStack.get(expressionStack.size() - 1);
						currentExpression.addMultiDimensionalExpression(currentMultiDimensionalExpression);
					}

					if (multiDimensionalExpressionStack.size() == 0) {
						currentMultiDimensionalExpression = null;
					} else {
						currentMultiDimensionalExpression = multiDimensionalExpressionStack.get(multiDimensionalExpressionStack.size() - 1);
					}
				}
			}
		}

		content = trimmedExpression.substring(endPosition);
		currentExpression = getCurrentExpressionWithAddedContent(currentExpression, expressionStack, expressionStringAggregate, endPosition, content);

		validateMultiDimensionalExpressionStackIsEmpty(expressionStringAggregate, multiDimensionalExpressionStack);

		currentExpression.finalize();

		return currentExpression;
	}

	private static void validateCurrentExpressionNotNull(ExpressionStringAggregate expressionStringAggregate, int startPosition, Expression currentExpression) {
		if (currentExpression == null) {
			EXPRESSION_EXPECTED.throwUncheckedException(IllegalArgumentException.class, expressionStringAggregate.getCaretedStringForPositions(
					ExpressionStringAggregate.ShowCaretString.IN_BOTH_EXPRESSIONS, true, startPosition - 1));
		}
	}

	private static void validateCurrentMultiDimensionalExpressionNotNull(ExpressionStringAggregate expressionStringAggregate, int startPosition,
			MultiDimensionalExpression currentMultiDimensionalExpression, char token) {
		if (currentMultiDimensionalExpression == null) {
			if (MultiDimensionalExpression.DIMENSION_SEPERATOR == token) {
				UNEXPECTED_DIMENSION_TOKEN_FOUND.throwUncheckedException(IllegalArgumentException.class, token,
						expressionStringAggregate.getCaretedStringForPositions(IN_BOTH_EXPRESSIONS, true, startPosition));
			}
			UNEXPECTED_END_TOKEN_FOUND.throwUncheckedException(IllegalArgumentException.class, token,
					expressionStringAggregate.getCaretedStringForPositions(IN_BOTH_EXPRESSIONS, true, startPosition));
		}
	}

	/*
	 * Method creates an expression if the supplied currentExpression is null.
	 * If a new expression is created this expression is pushed on the supplied
	 * expressionStack. Adds the content to the supplied or created expression
	 * and returns the supplied or created expression.
	 */
	private static Expression getCurrentExpressionWithAddedContent(Expression currentExpression, List<Expression> expressionStack,
			ExpressionStringAggregate expressionStringAggregate, int startPosition, String content) {

		Expression currentExpressionWithAddedContent = currentExpression;

		if (content.length() != 0) {
			if (currentExpression == null) {
				currentExpressionWithAddedContent = new Expression(expressionStringAggregate, startPosition, content);
				expressionStack.add(currentExpressionWithAddedContent);
			} else {
				currentExpressionWithAddedContent.addContent(content);
			}
		}

		return currentExpressionWithAddedContent;
	}

	private static void validateMultiDimensionalExpressionStackIsEmpty(ExpressionStringAggregate expressionStringAggregate,
			List<MultiDimensionalExpression> multiDimensionalExpressionStack) {
		if (multiDimensionalExpressionStack.size() > 0) {
			Set<Integer> startPositions = new HashSet<>();
			List<Character> expectedEndTokens = new ArrayList<>();

			for (MultiDimensionalExpression multiDimensionalExpression : multiDimensionalExpressionStack) {
				startPositions.add(multiDimensionalExpression.getStartPosition());
				expectedEndTokens.add(multiDimensionalExpression.getExpectedEndToken());
			}

			UNCLOSED_EXPRESSIONS
					.throwUncheckedException(IllegalArgumentException.class, StringUtils.join(expectedEndTokens, ", "), expressionStringAggregate
							.getCaretedStringForPositions(ExpressionStringAggregate.ShowCaretString.IN_BOTH_EXPRESSIONS, true, startPositions));
		}
	}

	private static void validateNoUsageOfReservedCharacters(ExpressionStringAggregate expressionStringAggregate) {
		char[] reservedCharacters = new char[] { Expression.EXPRESSION_PLACE_HOLDER };
		Set<Integer> positionsReservedCharacters = expressionStringAggregate.getPositionsReservedCharacters(reservedCharacters);
		if (!positionsReservedCharacters.isEmpty()) {
			ILLEGAL_USAGE_OF_RESERVED_CHARACTERS.throwUncheckedException(IllegalArgumentException.class,
					StringUtils.join(ArrayUtils.toObject(reservedCharacters), ' '), StringUtils.join(positionsReservedCharacters.toArray(), ", "),
					expressionStringAggregate.getCaretedStringForPositions(IN_BOTH_EXPRESSIONS, true, positionsReservedCharacters));
		}
	}

	public String aggregationTokenPairsAsString() {
		StringBuffer aggregationTokenPairsAsString = new StringBuffer();
		for (Entry<Character, Character> aggregationTokenPair : aggregationTokenPairs.entrySet()) {
			aggregationTokenPairsAsString.append(String.valueOf(aggregationTokenPair.getKey()));
			aggregationTokenPairsAsString.append(String.valueOf(aggregationTokenPair.getValue()));
		}

		return aggregationTokenPairsAsString.toString();
	}

	@Override
	public String toString() {
		return String.format("ArithmeticExpressionFactory\naggregationTokenPairs: %s\nDimension seperator: %c", aggregationTokenPairsAsString(),
				MultiDimensionalExpression.DIMENSION_SEPERATOR);
	}
}

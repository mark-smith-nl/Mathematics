package nl.smith.mathematics.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.smith.mathematics.utility.ErrorMessages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for storing one or multiple {@link Expression}s.<br>
 * The classes MultiDimensionalExpression and {@link Expression} are intertwined<br>
 * A multidimensional expression has a start position and <b>not null</b> begin and expected end characters.<br>
 * Expressions can only be added to the multidimensional expression if they are finalized and thus valid (see {@link AbstractExpression.STATE}.<br>
 * Multidimensional expression can only be finalized if they are valid.<br>
 * They are valid if the expected end character is the actual end character and the multidimensional expression contains at least one expression.<br>
 * The state of a multidimensional expression can only be modified if it is not in the finalized state.<br>
 * Properties can only be retrieved if the multidimensional expression's properties are stable.<br>
 * 
 * @author Mark Smith
 *
 */
public class MultiDimensionalExpression extends AbstractExpression {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiDimensionalExpression.class);

	public static final char DIMENSION_SEPERATOR = ',';

	// Note: A wrapper class is used since this value can be null
	private final Character startToken;

	// Note: A wrapper class is used since this value can be null
	private final Character expectedEndToken;

	// Note: A wrapper class is used since this value can be null
	private Character endToken;

	private final List<Expression> expressions = new ArrayList<>();

	/**
	 * Constructor for the multidimensional expression starting with a aggregation open token (see {@link ArithmeticExpressionFactoryTest})
	 * 
	 * @param startPosition
	 * @param startToken
	 * @param expectedEndToken
	 * @param expression
	 */
	public MultiDimensionalExpression(ExpressionStringAggregate expressionStringAggregate, int startPosition, Character startToken, Character expectedEndToken) {
		super(expressionStringAggregate, startPosition);

		if (startToken == null || expectedEndToken == null) {
			throw new IllegalArgumentException(ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("No startToken or expectedEndToken supplied"));
		}

		this.startToken = startToken;
		shiftAppendPositionIfValid(1);

		this.expectedEndToken = expectedEndToken;

		LOGGER.debug("Created multidimensional expression position({}-...) using starttoken '{}'", startPosition, startToken);
	}

	public void addExpression(Expression expression) {
		validateInState(STATE.BUILD);

		if (expression == null) {
			throw new IllegalArgumentException();
		}

		expression.validateInState(AbstractExpression.STATE.FINALIZED);

		if (!expressions.isEmpty()) {
			shiftAppendPositionIfValid(1);
		}

		shiftAppendPositionIfValid(expression);

		expressions.add(expression);
	}

	/** Stop appending the subexpression */
	public void addEndTokenAndFinalize(Character endToken) {
		validateInState(STATE.BUILD);

		this.endToken = endToken;
		shiftAppendPositionIfValid(1);

		LOGGER.debug("Stopped appending multidimensional expression position({}-...) using tokens '{} 'and '{}'", new Object[] { getStartPosition(), startToken, endToken });

		if (endToken != expectedEndToken) {
			throw new IllegalArgumentException(ErrorMessages.EXPRESSION_NOT_PROPERLY_CLOSED.getFormattedErrorMessage(
					expectedEndToken,
					endToken,
					getExpressionStringAggregate().getCaretedStringForPositions(ExpressionStringAggregate.ShowCaretString.IN_BOTH_EXPRESSIONS, true, getStartPosition(),
							getEndPosition())));
		}

		if (expressions.isEmpty()) {
			throw new IllegalArgumentException(ErrorMessages.EXPRESSION_EXPECTED.getFormattedErrorMessage(
					getExpressionStringAggregate().getCaretedStringForPositions(ExpressionStringAggregate.ShowCaretString.IN_BOTH_EXPRESSIONS, true, getEndPosition() + 1)));
		}

		finalize();
	}

	public Character getStartToken() {
		return startToken;
	}

	public Character getExpectedEndToken() {
		return expectedEndToken;
	}

	public Character getEndToken() {
		return endToken;
	}

	public List<Expression> getExpressions() {
		return Collections.unmodifiableList(expressions);
	}

	public int getDimension() {
		return expressions.size();
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();

		result.append(startToken);
		boolean added = false;
		for (Expression expression : expressions) {
			if (added) {
				result.append(DIMENSION_SEPERATOR);
			}
			result.append(expression.toString());
			added = true;
		}

		if (endToken != null) {
			result.append(endToken);
		}

		return result.toString();
	}
}
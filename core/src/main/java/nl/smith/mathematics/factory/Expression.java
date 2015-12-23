package nl.smith.mathematics.factory;

import static nl.smith.mathematics.utility.ErrorMessages.IMPLEMENT_ERROR_MESSAGE;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for storing characters for generating an arithmetic expression.<br>
 * The classes Expression and {@link MultiDimensionalExpression} are intertwined<br>
 * Multidimensional expressions can only be added to the expression if they are
 * finalized and thus valid (see {@link AbstractExpression.STATE}.<br>
 * Expressions can only be instantiated with
 * <ul>
 * <li>real content (not expression place holders) or</li>
 * <li><strike>a multidimensional expressions with dimension 1</strike><br>
 * </ul>
 * <br>
 * The state of a multidimensional expression can only be modified if it is not
 * in the finalized state.<br>
 * 
 * @author Mark Smith
 *
 */
public class Expression extends AbstractExpression {

	private static final Logger LOGGER = LoggerFactory.getLogger(Expression.class);

	public static final char EXPRESSION_PLACE_HOLDER = '$';

	private final StringBuffer content = new StringBuffer();

	private final List<MultiDimensionalExpression> multiDimensionalExpressions = new ArrayList<>();

	/**
	 * 
	 * @param expressionStringAggregate
	 * @param startPosition
	 * @param content
	 */
	public Expression(ExpressionStringAggregate expressionStringAggregate, int startPosition, String content) {
		super(expressionStringAggregate, startPosition);

		addContent(content);

		LOGGER.debug("Created expression starting position({}-...)", getStartPosition());
	}

	public Expression(ExpressionStringAggregate expressionStringAggregate, MultiDimensionalExpression multiDimensionalExpression) {
		super(expressionStringAggregate, validateAndDetermineStartPositionMultiDimensionalExpression(multiDimensionalExpression));
		addMultiDimensionalExpression(multiDimensionalExpression);

		LOGGER.debug("Created expression starting position({}-...)", getStartPosition());
	}

	/**
	 * Validates that the expression is a not null one dimensional expression
	 * and if so returns its start position
	 * 
	 * @param multiDimensionalExpression
	 * @return the start position of the provided multiDimensionalExpression
	 */
	private static int validateAndDetermineStartPositionMultiDimensionalExpression(MultiDimensionalExpression multiDimensionalExpression) {
		if (multiDimensionalExpression == null) {
			throw new IllegalArgumentException("Fout 1");
		}

		return multiDimensionalExpression.getStartPosition();
	}

	public void addContent(String content) {
		validateInState(STATE.BUILD);

		if (StringUtils.isBlank(content)) {
			IMPLEMENT_ERROR_MESSAGE.throwUncheckedException(IllegalArgumentException.class, "No content specified");
		}

		shiftAppendPositionIfValid(content.length());

		this.content.append(content);
	}

	public void addMultiDimensionalExpression(MultiDimensionalExpression multiDimensionalExpression) {
		validateInState(STATE.BUILD);

		if (multiDimensionalExpression == null) {
			throw new IllegalArgumentException();
		}

		multiDimensionalExpression.validateInState(AbstractExpression.STATE.FINALIZED);

		shiftAppendPositionIfValid(multiDimensionalExpression);

		content.append(EXPRESSION_PLACE_HOLDER);

		multiDimensionalExpressions.add(multiDimensionalExpression);
	}

	/**
	 * Returns the content of an expression using <b>$</b> as place holders for
	 * compound expressions
	 */
	public String getContentAsString() {
		return content.toString();
	}

	/**
	 * Method to get a real position of a character.
	 * 
	 * @param relativePosition
	 *            position relative to the start position of the expression
	 * @return
	 */
	public int getRealPosition(int relativePosition) {
		if (relativePosition < 0 || relativePosition >= content.length()) {
			IMPLEMENT_ERROR_MESSAGE.throwUncheckedException(IllegalArgumentException.class, "");
		}

		int realPosition = getStartPosition() + relativePosition;

		int numberOfMultiDimensionalExpressions = StringUtils.countMatches(content.subSequence(0, relativePosition).toString(),
				String.valueOf(EXPRESSION_PLACE_HOLDER));
		for (int i = 0; i < numberOfMultiDimensionalExpressions; i++) {
			realPosition += multiDimensionalExpressions.get(i).getLength();
		}
		realPosition -= numberOfMultiDimensionalExpressions;

		return realPosition;
	}

	public List<MultiDimensionalExpression> getMultiDimensionalExpressions() {
		return multiDimensionalExpressions;
	}

	public String toStructuredString() {
		StringBuffer result = new StringBuffer();

		result.append(content);
		for (MultiDimensionalExpression multiDimensionalExpression : multiDimensionalExpressions) {
			result.append("\n");
			result.append(StringUtils.repeat(" ", multiDimensionalExpression.getStartPosition()));
			result.append(multiDimensionalExpression.toString());
		}
		return result.toString();
	}
}
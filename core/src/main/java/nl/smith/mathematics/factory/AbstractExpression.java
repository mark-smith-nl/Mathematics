package nl.smith.mathematics.factory;

import nl.smith.mathematics.utility.ErrorMessages;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractExpression {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExpression.class);

	public static enum STATE {
		BUILD, // The expression is in the build state and can be modified
		FINALIZED // The expression is finalized and can not be modified
	}

	private final ExpressionStringAggregate expressionStringAggregate;

	private final int startPosition;

	private int appendPosition;

	private final int lastPositionTrimmedExpression;

	private STATE state = STATE.BUILD;

	public AbstractExpression(ExpressionStringAggregate expressionStringAggregate, int startPosition) {

		if (expressionStringAggregate == null) {
			throw new IllegalArgumentException(ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("No ExpressionStringAggregate supplied"));
		}

		lastPositionTrimmedExpression = expressionStringAggregate.getTrimmedExpression().length() - 1;

		if (startPosition < 0 || startPosition > lastPositionTrimmedExpression) {
			throw new IllegalArgumentException(ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("Foute startpositie 1"));
		}

		this.expressionStringAggregate = expressionStringAggregate;

		this.startPosition = startPosition;
		appendPosition = startPosition;
	}

	@Override
	public void finalize() {
		validateNotInState(STATE.FINALIZED);

		setState(STATE.FINALIZED);

		LOGGER.debug("Finalized expression position({}-{})", getStartPosition(), getEndPosition());
	};

	protected void validateInState(STATE... states) {
		for (STATE s : states) {
			if (this.state == s)
				return;
		}

		throw new IllegalStateException(String.format("\nThe multidimensional expression is not in one the specified state\nExpected in one of the states: %s\nActual: %s",
				StringUtils.join(states, ", "), this.state));

	}

	public void validateNotInState(STATE... states) {
		for (STATE state : states) {
			if (this.state == state)
				throw new IllegalStateException(String.format("\nThe expression is in the specified state: %s", this.state));
		}
	}

	/**
	 * Method validates:<br>
	 * <ul>
	 * <li>if the expected appendPosition is the actual position of appending
	 * <li>if the new appendPosition does not exceed the maximum append position
	 * </ul>
	 * <br>
	 * <br>
	 * If validat
	 * 
	 * @param length
	 */
	public void shiftAppendPositionIfValid(AbstractExpression augend) {
		if (augend.getStartPosition() != appendPosition) {
			throw new IllegalArgumentException(ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("Content starts ar wrong position"));
		}

		shiftAppendPositionIfValid(augend.getLength());
	}

	public void shiftAppendPositionIfValid(int length) {
		appendPosition += length;
		if (appendPosition > lastPositionTrimmedExpression + 1) {
			throw new IllegalArgumentException(ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("Content size exceeds the size of the original raw expression"));
		}
	}

	public ExpressionStringAggregate getExpressionStringAggregate() {
		return expressionStringAggregate;
	}

	/** Returns the begin position of the expression relative to the trimmed root expression */
	public int getStartPosition() {
		return startPosition;
	}

	public int getAppendPosition() {
		return appendPosition;
	}

	public int getLength() {
		return appendPosition - startPosition;
	}

	public int getEndPosition() {
		return appendPosition - 1;
	}

	public STATE getState() {
		return state;
	}

	protected void setState(STATE state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return expressionStringAggregate.getTrimmedExpression().substring(startPosition, appendPosition);
	}
}

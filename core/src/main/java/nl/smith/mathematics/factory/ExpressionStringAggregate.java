package nl.smith.mathematics.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * An expression string aggregate contains the following elements:
 * <ul>
 * <li>single line expression. The initial expression as a single line, possibly containing whitespace characters</li>
 * <li>trimmed expression. The initial expression with all whitespace characters removed</li>
 * <li>original positions. The original positions of the characters in the trimmed expression relative to that in the initial expression</li>
 * </ul>
 * 
 * @author mark
 */
public class ExpressionStringAggregate {

	private final String singleLineExpression;

	private final String trimmedExpression;

	private final List<Integer> positions;

	public static enum ShowCaretString {
		IN_INITIAL_EXPRESSION,
		IN_TRIMMED_EXPRESSION,
		IN_BOTH_EXPRESSIONS;
	}

	/**
	 * Method removes white space characters from a <b> not null</b> string and registers the positions of the remaining characters in the original string.
	 * 
	 * @param singleLineExpression
	 *            Input string without new lines
	 */
	public ExpressionStringAggregate(String rawExpression) {
		if (rawExpression == null) {
			throw new IllegalArgumentException("\nNo string specified");
		}

		// Remove newline characters
		String singleLineExpression = rawExpression.replaceAll("['\\n\\f'\\r\\013']", "");

		if (StringUtils.isBlank(singleLineExpression)) {
			throw new IllegalArgumentException("\nBlank string specified");
		}

		StringBuffer trimmedExpression = new StringBuffer();

		List<Integer> positions = new ArrayList<>();
		for (int i = 0; i < singleLineExpression.length(); i++) {
			char charAt = singleLineExpression.charAt(i);
			if (charAt != ' ' && charAt != '\t') {
				positions.add(i);
				trimmedExpression.append(charAt);
			}
		}

		this.singleLineExpression = singleLineExpression;
		this.trimmedExpression = trimmedExpression.toString();
		this.positions = Collections.unmodifiableList(positions);
	}

	public String getSingleLineExpression() {
		return singleLineExpression;
	}

	public String getTrimmedExpression() {
		return trimmedExpression;
	}

	public List<Integer> getPositions() {
		return positions;
	}

	public String getCaretedStringForPositionRange(ShowCaretString showCaretString, int positionOne, int positionTwo) {
		int startPosition = positionOne < positionTwo ? positionOne : positionTwo;
		int endPosition = positionOne > positionTwo ? positionOne : positionTwo;

		Set<Integer> positions = new HashSet<>();
		for (int i = startPosition; i <= endPosition; i++) {
			positions.add(i);
		}

		return getCaretedStringForPositions(showCaretString, false, positions);
	}

	public String getCaretedStringForPositions(ShowCaretString showCaretString, boolean showPositionNumber, Set<Integer> positions) {
		validatePositions(positions);

		String caretString = "";

		ShowCaretString show = singleLineExpression.equals(trimmedExpression) ? ShowCaretString.IN_TRIMMED_EXPRESSION : showCaretString;

		if (ShowCaretString.IN_INITIAL_EXPRESSION.equals(show) || ShowCaretString.IN_BOTH_EXPRESSIONS.equals(show)) {
			Set<Integer> originalPositions = new HashSet<>();
			for (int position : positions) {
				originalPositions.add(this.positions.get(position));
			}
			caretString += getCaretedStringForPositions(singleLineExpression, showPositionNumber, originalPositions);
		}

		if (ShowCaretString.IN_TRIMMED_EXPRESSION.equals(show) || ShowCaretString.IN_BOTH_EXPRESSIONS.equals(show)) {
			caretString += getCaretedStringForPositions(trimmedExpression, showPositionNumber, positions);
		}

		return caretString;
	}

	public String getCaretedStringForPositions(ShowCaretString showCaretString, boolean showPositionNumber, Integer... positions) {
		return this.getCaretedStringForPositions(showCaretString, showPositionNumber, new HashSet<Integer>(Arrays.asList(positions)));
	}

	private void validatePositions(Set<Integer> positions) {
		if (CollectionUtils.isEmpty(positions)) {
			throw new IllegalArgumentException(String.format("No value for position specified"));
		}

		for (int position : positions) {
			if (position < 0 || position >= trimmedExpression.length()) {
				throw new IllegalArgumentException(String.format("Illegal value for position specified: %d. 0 < position < %d", position, trimmedExpression.length()));
			}
		}
	}

	private static String getCaretedStringForPositions(String input, boolean showPositionNumber, Set<Integer> positions) {
		boolean showPosition = showPositionNumber && positions.size() == 1;

		StringBuffer sb = new StringBuffer(input);
		// Replace character at specified position by a caret
		// Get the highest i.e. last position
		int lastPosition = -1;
		for (int position : positions) {
			sb.setCharAt(position, '^');
			lastPosition = position > lastPosition ? position : lastPosition;
		}

		Matcher m = Pattern.compile("[\\S&&[^\\^]]").matcher(sb);
		// Replace all non-whitespace characters by a space EXCLUDING the carets. Clip te result after the last caret.
		String result = "\n" + input + "\n" + m.replaceAll(" ").substring(0, lastPosition + 1)
				+ (showPosition ? "[" + lastPosition + "]" : "");
		return result;

	}

	/**
	 * Gets the positions of the specified characters in the <b>{@link ExpressionStringAggregate#trimmedExpression}</b>
	 * 
	 * @param reservedCharacters
	 *            character array of reserved characters
	 * @return
	 */
	public Set<Integer> getPositionsReservedCharacters(char... reservedCharacters) {
		Set<Integer> positionsSubExpressionIdentifiers = new HashSet<>();

		if (ArrayUtils.isEmpty(reservedCharacters)) {
			return positionsSubExpressionIdentifiers;
		}

		StringBuffer regex = new StringBuffer();
		regex.append('[');
		for (char reservedCharacter : reservedCharacters) {
			regex.append("\\Q").append(reservedCharacter).append("\\E");
		}
		regex.append(']');

		Matcher matcher = Pattern.compile(regex.toString()).matcher(this.trimmedExpression);
		while (matcher.find()) {
			positionsSubExpressionIdentifiers.add(matcher.start());
		}

		return positionsSubExpressionIdentifiers;
	}

	@Override
	public String toString() {
		StringBuffer placeIndices = new StringBuffer();
		int i = 0;
		while (i < trimmedExpression.length()) {
			placeIndices.append(i++ % 10);
		}

		return singleLineExpression + "\n" + trimmedExpression + "\n" + placeIndices.toString();
	}

}
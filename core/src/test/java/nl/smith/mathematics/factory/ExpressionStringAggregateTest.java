package nl.smith.mathematics.factory;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.smith.mathematics.factory.ExpressionStringAggregate.ShowCaretString;

import org.junit.Test;

public class ExpressionStringAggregateTest {

	// "mark\tsmith  Willem de Zwijgerweg    51\t\tGeldermalsen$0345#577490"
	// "0123 4567890123456789012345678901234567 8 9012345678901234567890123"
	// "           1         2         3           4         5         6"
	// "0123  56789  234567 90 2345678901    67    012345678901234567890123"

	String singleLineExpression = "mark\tsmith  Willem de Zwijgerweg    51\t\tGeldermalsen$0345#577490";
	ExpressionStringAggregate stringAggregate = new ExpressionStringAggregate(singleLineExpression);

	@Test(expected = IllegalArgumentException.class)
	public void constructorNullArgument() {
		new ExpressionStringAggregate(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorArgumentContainsNoContent() {
		new ExpressionStringAggregate(" \r\n\t");
	}

	@Test
	public void constructorArgument() {
		String trimmedExpression = stringAggregate.getTrimmedExpression();

		assertEquals("marksmithWillemdeZwijgerweg51Geldermalsen$0345#577490", trimmedExpression);
		assertEquals(stringAggregate.getTrimmedExpression().length(), stringAggregate.getPositions().size());

		List<Integer> expectedPositions = Arrays.asList(
				0, 1, 2, 3, 5, 6, 7, 8, 9, 12, 13, 14, 15, 16, 17, 19, 20, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 36, 37, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53,
				54, 55, 56, 57, 58, 59, 60, 61, 62, 63);

		assertEquals(expectedPositions, stringAggregate.getPositions());
	}

	@Test
	public void getPositionsReservedCharacters() {
		Set<Integer> positionsReservedCharacters = stringAggregate.getPositionsReservedCharacters('$', '#');
		Set<Integer> expectedPositions = new HashSet<>(Arrays.asList(41, 46));

		assertEquals(expectedPositions, positionsReservedCharacters);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getCaretedStringForPositionRangeUsingNegativePosition() {
		stringAggregate.getCaretedStringForPositionRange(ShowCaretString.IN_INITIAL_EXPRESSION, -5, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getCaretedStringForPositionRangePositionOutOfBound() {
		stringAggregate.getCaretedStringForPositionRange(ShowCaretString.IN_INITIAL_EXPRESSION, 0, stringAggregate.getSingleLineExpression().length());
	}

	@Test
	public void getCaretedStringForPositionRange() {
		String caretedString = stringAggregate.getCaretedStringForPositionRange(ShowCaretString.IN_INITIAL_EXPRESSION, 2, 5);
		String expected = "\n" + stringAggregate.getSingleLineExpression() + "\n" + "  ^^\t^^";

		caretedString = stringAggregate.getCaretedStringForPositionRange(ShowCaretString.IN_TRIMMED_EXPRESSION, 2, 5);
		expected = "\n" + stringAggregate.getTrimmedExpression() + "\n" + "  ^^^^";

		assertEquals(expected, caretedString);

		caretedString = stringAggregate.getCaretedStringForPositionRange(ShowCaretString.IN_INITIAL_EXPRESSION, 0, 0);
		expected = "\n" + stringAggregate.getSingleLineExpression() + "\n" + "^";

		assertEquals(expected, caretedString);

		caretedString = stringAggregate.getCaretedStringForPositionRange(ShowCaretString.IN_TRIMMED_EXPRESSION, 0, 0);
		expected = "\n" + stringAggregate.getTrimmedExpression() + "\n" + "^";

	}

	@Test
	public void getCaretedStringForPositions() {
		String caretedString = stringAggregate.getCaretedStringForPositions(ShowCaretString.IN_INITIAL_EXPRESSION, true, 6, 4, 2, 0, 0, 0);
		String expected = "\n" + stringAggregate.getSingleLineExpression() + "\n" + "^ ^ \t^ ^";

		assertEquals(expected, caretedString);

		caretedString = stringAggregate.getCaretedStringForPositions(ShowCaretString.IN_TRIMMED_EXPRESSION, true, 6, 4, 2, 0, 0, 0);
		expected = "\n" + stringAggregate.getTrimmedExpression() + "\n" + "^ ^ ^ ^";

		assertEquals(expected, caretedString);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getCaretedStringForPositionsNoPositionsSpecified() {
		stringAggregate.getCaretedStringForPositions(ShowCaretString.IN_INITIAL_EXPRESSION, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getCaretedStringForPositionsNoSetSpecified() {
		stringAggregate.getCaretedStringForPositions(ShowCaretString.IN_INITIAL_EXPRESSION, true, (Set<Integer>) null);
	}
}

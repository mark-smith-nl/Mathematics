package nl.smith.mathematics.factory;

import static nl.smith.mathematics.factory.ExpressionStringAggregate.ShowCaretString.IN_BOTH_EXPRESSIONS;
import static nl.smith.mathematics.utility.ErrorMessages.EXPRESSION_EXPECTED;
import static nl.smith.mathematics.utility.ErrorMessages.EXPRESSION_NOT_PROPERLY_CLOSED;
import static nl.smith.mathematics.utility.ErrorMessages.ILLEGAL_USAGE_OF_RESERVED_CHARACTERS;
import static nl.smith.mathematics.utility.ErrorMessages.UNCLOSED_EXPRESSIONS;
import static nl.smith.mathematics.utility.ErrorMessages.UNEXPECTED_DIMENSION_TOKEN_FOUND;
import static nl.smith.mathematics.utility.ErrorMessages.UNEXPECTED_END_TOKEN_FOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArithmeticExpressionFactoryTest {

	@Mock
	private ArithmeticComponentResolver arithmeticComponentResolver;

	/** System under test */
	private ArithmeticExpressionFactory arithmeticExpressionFactory;

	@Before
	public void init() {
		arithmeticExpressionFactory = new ArithmeticExpressionFactory(arithmeticComponentResolver);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor() {
		new ArithmeticExpressionFactory(null);
	}

	@Test
	public void makeExpressionUsingArgumentContainingReservedCharacters() {
		boolean errorThrown = false;

		String rawExpression = "\t\f\r\n(2  +  3*{4-5}) * $1\t  + $$2 maal\n vijf#";
		// "(2+3*{4-5})*$1+$$2maalvijf#"
		// "012345678901234567890123456"
		ExpressionStringAggregate expressionStringAggregate = new ExpressionStringAggregate(rawExpression);

		try {
			errorThrown = false;
			arithmeticExpressionFactory.makeExpression(new ExpressionStringAggregate(rawExpression));
		} catch (IllegalArgumentException e) {
			errorThrown = true;
			String actualErrormessage = e.getMessage();

			// char[] reservedCharacters = new char[] { Expression.EXPRESSION_PLACE_HOLDER, Expression.DIMENSION_PLACE_HOLDER };
			char[] reservedCharacters = new char[] { Expression.EXPRESSION_PLACE_HOLDER };
			Set<Integer> expectedPositionsReservedCharacters = new HashSet<>(Arrays.asList(12, 15, 16));
			String expectedErrorMessage = ILLEGAL_USAGE_OF_RESERVED_CHARACTERS.getFormattedErrorMessage(
					StringUtils.join(ArrayUtils.toObject(reservedCharacters), ' '),
					StringUtils.join(expectedPositionsReservedCharacters.toArray(), ", "),
					expressionStringAggregate.getCaretedStringForPositions(IN_BOTH_EXPRESSIONS, true, expectedPositionsReservedCharacters));

			assertEquals(expectedErrorMessage, actualErrormessage);
		}
		assertTrue(errorThrown);
	}

	@Test
	public void makeExpressionUsingMalFormedRawExpressions() {
		boolean errorThrown = false;

		String rawExpression;
		ExpressionStringAggregate expressionStringAggregate = null;

		try {
			// Unexpected end token
			errorThrown = false;
			rawExpression = "5-7+(2+3*{4-5})]*D1+D2*vijf[2+6]/ X";
			// "5-7+(2+3*{4-5})]*D1+D2*vijf*[2+6]/X"
			// "01234567890123456789012345678901234567890"
			// "          1         2         3         4"
			// "               ^ Error"
			expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
			arithmeticExpressionFactory.makeExpression(expressionStringAggregate);
		} catch (IllegalArgumentException e) {
			errorThrown = true;
			String actualErrormessage = e.getMessage();

			Integer[] positions = new Integer[] { 15 };
			char token = ']';
			String expectedErrorMessage = UNEXPECTED_END_TOKEN_FOUND.getFormattedErrorMessage(
					token,
					expressionStringAggregate.getCaretedStringForPositions(IN_BOTH_EXPRESSIONS, true, positions));

			assertEquals(expectedErrorMessage, actualErrormessage);
		}
		assertTrue(errorThrown);

		try {
			// Missing end tokens
			errorThrown = false;
			rawExpression = "5-7+(2+3*{4-5})*D1+D2*vijf*[2+6]/X+(2-3*[";
			// "5-7+(2+3*{4-5})*D1+D2*vijf*[2+6]/X+(2-3*["
			// "01234567890123456789012345678901234567890"
			// "          1         2         3         4"
			// "                                   ^    ^ Error"
			expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
			arithmeticExpressionFactory.makeExpression(expressionStringAggregate);
		} catch (IllegalArgumentException e) {
			errorThrown = true;
			String actualErrormessage = e.getMessage();

			List<Character> expectedEndTokens = Arrays.asList(')', ']');
			Integer[] positions = new Integer[] { 35, 40 };
			String expectedErrorMessage = UNCLOSED_EXPRESSIONS.getFormattedErrorMessage(
					StringUtils.join(expectedEndTokens, ", "),
					expressionStringAggregate.getCaretedStringForPositions(IN_BOTH_EXPRESSIONS, true, positions));

			assertEquals(expectedErrorMessage, actualErrormessage);
		}
		assertTrue(errorThrown);

		try {
			// Expression expected
			errorThrown = false;
			rawExpression = "2+sum({[()";
			// "2+sum({[()"
			// "0123456789"
			// "         ^ Error"
			expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
			arithmeticExpressionFactory.makeExpression(expressionStringAggregate);
		} catch (Exception e) {
			errorThrown = true;
			String actualErrormessage = e.getMessage();

			int position = 8;
			String expectedErrorMessage = EXPRESSION_EXPECTED.getFormattedErrorMessage(
					expressionStringAggregate.getCaretedStringForPositions(IN_BOTH_EXPRESSIONS, true, position));

			assertEquals(expectedErrorMessage, actualErrormessage);
		}
		assertTrue(errorThrown);

		try {
			// Expression expected
			errorThrown = false;
			rawExpression = "2+sum({[(}";
			// "2+sum({[(}"
			// "0123456789"
			// "         ^ Error"
			expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
			arithmeticExpressionFactory.makeExpression(expressionStringAggregate);
		} catch (Exception e) {
			errorThrown = true;
			String actualErrormessage = e.getMessage();

			int position = 8;
			String expectedErrorMessage = EXPRESSION_EXPECTED.getFormattedErrorMessage(
					expressionStringAggregate.getCaretedStringForPositions(IN_BOTH_EXPRESSIONS, true, position));

			assertEquals(expectedErrorMessage, actualErrormessage);
		}
		assertTrue(errorThrown);

		try {
			// Wrong end token
			rawExpression = "2+sum({[(8}";
			// "2+sum({[(8}"
			// "01234567890"
			// "          ^ Error"
			expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
			errorThrown = false;
			arithmeticExpressionFactory.makeExpression(expressionStringAggregate);
		} catch (Exception e) {
			errorThrown = true;
			String actualErrormessage = e.getMessage();

			Integer[] positions = new Integer[] { 8, 10 };
			char expectedEndToken = ')';
			char actualEndToken = '}';
			String expectedErrorMessage = EXPRESSION_NOT_PROPERLY_CLOSED.getFormattedErrorMessage(
					expectedEndToken,
					actualEndToken,
					expressionStringAggregate.getCaretedStringForPositions(IN_BOTH_EXPRESSIONS, true, positions));

			assertEquals(expectedErrorMessage, actualErrormessage);
		}
		assertTrue(errorThrown);

		try {
			rawExpression = "2+sum({[(8,}";
			// "2+sum({[(8,}"
			// "012345678901"
			expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
			errorThrown = false;
			arithmeticExpressionFactory.makeExpression(expressionStringAggregate);
		} catch (Exception e) {
			errorThrown = true;
			String actualErrormessage = e.getMessage();

			Integer[] positions = new Integer[] { 10 };
			String expectedErrorMessage = EXPRESSION_EXPECTED.getFormattedErrorMessage(
					expressionStringAggregate.getCaretedStringForPositions(IN_BOTH_EXPRESSIONS, true, positions));

			assertEquals(expectedErrorMessage, actualErrormessage);
		}
		assertTrue(errorThrown);

		try {
			rawExpression = "2,5";
			// "2,5"
			// "0123"
			expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
			errorThrown = false;
			arithmeticExpressionFactory.makeExpression(expressionStringAggregate);
		} catch (Exception e) {
			errorThrown = true;
			String actualErrormessage = e.getMessage();

			Integer[] positions = new Integer[] { 1 };
			String expectedErrorMessage = UNEXPECTED_DIMENSION_TOKEN_FOUND.getFormattedErrorMessage(
					MultiDimensionalExpression.DIMENSION_SEPERATOR,
					expressionStringAggregate.getCaretedStringForPositions(IN_BOTH_EXPRESSIONS, true, positions));

			assertEquals(expectedErrorMessage, actualErrormessage);
		}
		assertTrue(errorThrown);
	}

	@Test
	public void makeExpression() {
		String rawExpression;
		ExpressionStringAggregate expressionStringAggregate;
		Expression expression;

		rawExpression = "2";
		expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
		expression = arithmeticExpressionFactory.makeExpression(expressionStringAggregate);

		rawExpression = "2+3";
		expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
		expression = arithmeticExpressionFactory.makeExpression(expressionStringAggregate);

		rawExpression = "(2)";
		expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
		expression = arithmeticExpressionFactory.makeExpression(expressionStringAggregate);

		rawExpression = "(2+3)";
		expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
		expression = arithmeticExpressionFactory.makeExpression(expressionStringAggregate);

		rawExpression = "2+3*sum(1,2,sum(1,sum{1,1}),4,5,faculty{3})*sin(pi/4)";
		expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
		expression = arithmeticExpressionFactory.makeExpression(expressionStringAggregate);
		System.out.println(expression);
		System.out.println(expression.toStructuredString());
	}
}

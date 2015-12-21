package nl.smith.mathematics.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import nl.smith.mathematics.utility.ErrorMessages;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MultiDimensionalExpressionTest {

	private static final int NUMBER_OF_CHARS_IN_TRIMMED_EXPRESSION = 20;

	private static final String TRIMMED_EXPRESSION = StringUtils.repeat(".", NUMBER_OF_CHARS_IN_TRIMMED_EXPRESSION);

	private ExpressionStringAggregate expressionStringAggregate;

	private Expression expression;

	private Expression secondExpression;

	private Expression thirdExpression;

	@Before
	public void initMocks() {
		expressionStringAggregate = mock(ExpressionStringAggregate.class);
		when(expressionStringAggregate.getTrimmedExpression()).thenReturn(TRIMMED_EXPRESSION);

		expression = mock(Expression.class);
		secondExpression = mock(Expression.class);
		thirdExpression = mock(Expression.class);
	}

	@Test
	public void constructWithIllegalExpressionArguments() {
		boolean errorThrown;

		try {
			// No expressionStringAggregate
			initMocks();
			errorThrown = false;
			new MultiDimensionalExpression(null, 0, '{', '}');
		} catch (IllegalArgumentException e) {
			verify(expressionStringAggregate, never()).getTrimmedExpression();
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Negative start position
			initMocks();
			errorThrown = false;
			new MultiDimensionalExpression(expressionStringAggregate, -1, '{', '}');
		} catch (IllegalArgumentException e) {
			verify(expressionStringAggregate).getTrimmedExpression();
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Null start token
			initMocks();
			errorThrown = false;
			new MultiDimensionalExpression(expressionStringAggregate, 0, null, '}');
		} catch (IllegalArgumentException e) {
			verify(expressionStringAggregate).getTrimmedExpression();
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Null end token
			initMocks();
			errorThrown = false;
			new MultiDimensionalExpression(expressionStringAggregate, 0, '{', null);
		} catch (IllegalArgumentException e) {
			verify(expressionStringAggregate).getTrimmedExpression();
			errorThrown = true;
		}
		assertTrue(errorThrown);
	}

	@Test
	public void addIllegalExpressions() {
		boolean errorThrown;

		try {
			// Null expression
			initMocks();
			errorThrown = false;
			MultiDimensionalExpression multiDimensionalExpression = new MultiDimensionalExpression(expressionStringAggregate, 0, '{', '}');
			multiDimensionalExpression.addExpression(null);
		} catch (IllegalArgumentException e) {
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Expression not finalized
			initMocks();
			errorThrown = false;
			doThrow(new IllegalArgumentException()).when(expression).validateInState(Expression.STATE.FINALIZED);
			MultiDimensionalExpression multiDimensionalExpression = new MultiDimensionalExpression(expressionStringAggregate, 0, '{', '}');
			multiDimensionalExpression.addExpression(expression);
		} catch (IllegalArgumentException e) {
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Expression starts before expected append position of multiDimensionalExpression
			initMocks();
			errorThrown = false;
			when(expression.getStartPosition()).thenReturn(1);
			MultiDimensionalExpression multiDimensionalExpression = new MultiDimensionalExpression(expressionStringAggregate, 1, '{', '}');
			multiDimensionalExpression.addExpression(expression);
		} catch (IllegalArgumentException e) {
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Expression starts after expected append position of multiDimensionalExpression
			initMocks();
			errorThrown = false;
			when(expression.getStartPosition()).thenReturn(3);
			MultiDimensionalExpression multiDimensionalExpression = new MultiDimensionalExpression(expressionStringAggregate, 1, '{', '}');
			multiDimensionalExpression.addExpression(expression);
		} catch (IllegalArgumentException e) {
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Expression starts on the expected append position of multiDimensionalExpression but is to long
			initMocks();
			errorThrown = false;
			when(expression.getStartPosition()).thenReturn(2);
			when(expression.getLength()).thenReturn(NUMBER_OF_CHARS_IN_TRIMMED_EXPRESSION);
			MultiDimensionalExpression multiDimensionalExpression = new MultiDimensionalExpression(expressionStringAggregate, 1, '{', '}');
			multiDimensionalExpression.addExpression(expression);
		} catch (IllegalArgumentException e) {
			errorThrown = true;
		}
		assertTrue(errorThrown);
	}

	@Test
	public void addExpressions() {
		String trimmedExpression = "2+{2+7,4+7*8-9,88}";
		// "                        012345678901234567"
		when(expressionStringAggregate.getTrimmedExpression()).thenReturn(trimmedExpression);

		when(expression.getStartPosition()).thenReturn(3);
		when(expression.getLength()).thenReturn(3);
		when(expression.toString()).thenReturn("2+7");

		when(secondExpression.getStartPosition()).thenReturn(7);
		when(secondExpression.getLength()).thenReturn(7);
		when(secondExpression.toString()).thenReturn("4+7*8-9");

		when(thirdExpression.getStartPosition()).thenReturn(15);
		when(thirdExpression.getLength()).thenReturn(2);
		when(thirdExpression.toString()).thenReturn("88");

		MultiDimensionalExpression multiDimensionalExpression = new MultiDimensionalExpression(expressionStringAggregate, 2, '{', '}');
		multiDimensionalExpression.addExpression(expression);
		multiDimensionalExpression.addExpression(secondExpression);
		multiDimensionalExpression.addExpression(thirdExpression);
		multiDimensionalExpression.addEndTokenAndFinalize('}');

		assertEquals(2, multiDimensionalExpression.getStartPosition());
		assertEquals(Character.valueOf('{'), multiDimensionalExpression.getStartToken());
		assertEquals(Character.valueOf('}'), multiDimensionalExpression.getEndToken());
		assertEquals(Character.valueOf('}'), multiDimensionalExpression.getExpectedEndToken());
		assertEquals(16, multiDimensionalExpression.getLength());
		assertEquals(17, multiDimensionalExpression.getEndPosition());

		assertEquals("{2+7,4+7*8-9,88}", multiDimensionalExpression.toString());

		assertEquals(3, multiDimensionalExpression.getExpressions().size());
		assertEquals(expression, multiDimensionalExpression.getExpressions().get(0));
		assertEquals(secondExpression, multiDimensionalExpression.getExpressions().get(1));
		assertEquals(thirdExpression, multiDimensionalExpression.getExpressions().get(2));

		assertEquals(AbstractExpression.STATE.FINALIZED, multiDimensionalExpression.getState());
	}

	@Test
	public void finalizeWithIllegalContent() {
		boolean errorThrown;

		MultiDimensionalExpression multiDimensionalExpression = null;

		try {
			// No content
			initMocks();
			errorThrown = false;
			multiDimensionalExpression = new MultiDimensionalExpression(expressionStringAggregate, 2, '{', '}');
			multiDimensionalExpression.addEndTokenAndFinalize('}');
		} catch (IllegalArgumentException e) {
			errorThrown = true;

			String actualMessage = ErrorMessages.EXPRESSION_EXPECTED.getFormattedErrorMessage((Object[]) null);
			assertEquals(actualMessage, e.getMessage());
			assertNotEquals(AbstractExpression.STATE.FINALIZED, multiDimensionalExpression.getState());
		}
		assertTrue(errorThrown);

		try {
			// Wrong end token
			initMocks();
			errorThrown = false;

			when(expression.getStartPosition()).thenReturn(3);
			when(expression.getLength()).thenReturn(3);
			when(expression.toString()).thenReturn("2+7");

			multiDimensionalExpression = new MultiDimensionalExpression(expressionStringAggregate, 2, '{', '}');
			multiDimensionalExpression.addExpression(expression);
			multiDimensionalExpression.addEndTokenAndFinalize(')');
		} catch (IllegalArgumentException e) {
			errorThrown = true;

			assertEquals(2, multiDimensionalExpression.getStartPosition());
			assertEquals(Character.valueOf('{'), multiDimensionalExpression.getStartToken());
			assertEquals(Character.valueOf(')'), multiDimensionalExpression.getEndToken());
			assertEquals(Character.valueOf('}'), multiDimensionalExpression.getExpectedEndToken());

			assertEquals(5, multiDimensionalExpression.getLength());
			assertEquals(6, multiDimensionalExpression.getEndPosition());
			assertEquals("{2+7)", multiDimensionalExpression.toString());
			assertEquals(1, multiDimensionalExpression.getExpressions().size());
			assertEquals(expression, multiDimensionalExpression.getExpressions().get(0));

			String actualMessage = ErrorMessages.EXPRESSION_NOT_PROPERLY_CLOSED.getFormattedErrorMessage(
					multiDimensionalExpression.getExpectedEndToken(),
					multiDimensionalExpression.getEndToken(),
					null);

			assertEquals(actualMessage, e.getMessage());
			assertNotEquals(AbstractExpression.STATE.FINALIZED, multiDimensionalExpression.getState());
		}
		assertTrue(errorThrown);
	}
}

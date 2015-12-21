package nl.smith.mathematics.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionTest {

	private static final int NUMBER_OF_CHARS_IN_TRIMMED_EXPRESSION = 20;

	private static final String TRIMMED_EXPRESSION = StringUtils.repeat(".", NUMBER_OF_CHARS_IN_TRIMMED_EXPRESSION);

	private ExpressionStringAggregate expressionStringAggregate;

	private MultiDimensionalExpression multiDimensionalExpression;

	private MultiDimensionalExpression secondMultiDimensionalExpression;

	@Before
	public void initMocks() {
		expressionStringAggregate = mock(ExpressionStringAggregate.class);
		when(expressionStringAggregate.getTrimmedExpression()).thenReturn(TRIMMED_EXPRESSION);

		multiDimensionalExpression = mock(MultiDimensionalExpression.class);
		secondMultiDimensionalExpression = mock(MultiDimensionalExpression.class);
	}

	@Test
	public void constructWithIllegalStringArguments() {
		boolean errorThrown;

		try {
			// No expressionStringAggregate
			initMocks();
			errorThrown = false;
			new Expression(null, 0, "2+3");
		} catch (IllegalArgumentException e) {
			verify(expressionStringAggregate, never()).getTrimmedExpression();
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Negative start position
			initMocks();
			errorThrown = false;
			new Expression(expressionStringAggregate, -1, "2+3");
		} catch (IllegalArgumentException e) {
			verify(expressionStringAggregate).getTrimmedExpression();
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Start position larger or equal then the trimmedExpression length
			initMocks();
			errorThrown = false;
			new Expression(expressionStringAggregate, NUMBER_OF_CHARS_IN_TRIMMED_EXPRESSION, "2+3");
		} catch (IllegalArgumentException e) {
			verify(expressionStringAggregate).getTrimmedExpression();
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Blank content
			initMocks();
			errorThrown = false;
			new Expression(expressionStringAggregate, 0, " ");
		} catch (IllegalArgumentException e) {
			verify(expressionStringAggregate).getTrimmedExpression();
			errorThrown = true;
		}
		assertTrue(errorThrown);

	}

	@Test
	public void constructWithIllegalMultiDimensionalExpressionArguments() {
		boolean errorThrown = false;

		try {
			// Null MultiDimensionalExpression
			initMocks();
			errorThrown = false;
			new Expression(expressionStringAggregate, (MultiDimensionalExpression) null);
		} catch (IllegalArgumentException e) {
			verify(multiDimensionalExpression, never()).getStartPosition();
			verify(expressionStringAggregate, never()).getTrimmedExpression();

			verify(multiDimensionalExpression, never()).validateInState(AbstractExpression.STATE.FINALIZED);
			verify(multiDimensionalExpression, never()).getEndPosition();
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Negative start position
			initMocks();
			errorThrown = false;
			when(multiDimensionalExpression.getStartPosition()).thenReturn(-1);
			new Expression(expressionStringAggregate, multiDimensionalExpression);
		} catch (IllegalArgumentException e) {
			verify(multiDimensionalExpression).getStartPosition();
			verify(expressionStringAggregate).getTrimmedExpression();

			verify(multiDimensionalExpression, never()).validateInState(AbstractExpression.STATE.FINALIZED);
			verify(multiDimensionalExpression, never()).getEndPosition();
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// Start position larger or equal then the trimmedExpression length
			initMocks();
			errorThrown = false;
			when(multiDimensionalExpression.getStartPosition()).thenReturn(NUMBER_OF_CHARS_IN_TRIMMED_EXPRESSION);

			new Expression(expressionStringAggregate, multiDimensionalExpression);
		} catch (IllegalArgumentException e) {
			verify(multiDimensionalExpression).getStartPosition();
			verify(expressionStringAggregate).getTrimmedExpression();

			verify(multiDimensionalExpression, never()).validateInState(AbstractExpression.STATE.FINALIZED);
			verify(multiDimensionalExpression, never()).getEndPosition();
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// The MultiDimensionalExpression can be added but:
			// the MultiDimensionalExpression not in state FINALIZED
			initMocks();
			errorThrown = false;
			when(multiDimensionalExpression.getStartPosition()).thenReturn(0);
			doThrow(new IllegalArgumentException()).when(multiDimensionalExpression).validateInState(Expression.STATE.FINALIZED);

			new Expression(expressionStringAggregate, multiDimensionalExpression);
		} catch (IllegalArgumentException e) {
			verify(multiDimensionalExpression).getStartPosition();
			verify(multiDimensionalExpression).getStartPosition();
			verify(expressionStringAggregate).getTrimmedExpression();

			verify(multiDimensionalExpression).validateInState(AbstractExpression.STATE.FINALIZED);
			verify(multiDimensionalExpression, never()).getEndPosition();
			verify(multiDimensionalExpression, never()).getLength();
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			// The MultiDimensionalExpression can be added but:
			// the end position for MultiDimensionalExpression larger or equal then the trimmedExpression length
			initMocks();
			errorThrown = false;
			when(multiDimensionalExpression.getDimension()).thenReturn(1);
			when(multiDimensionalExpression.getStartPosition()).thenReturn(0);
			when(multiDimensionalExpression.getLength()).thenReturn(NUMBER_OF_CHARS_IN_TRIMMED_EXPRESSION + 1);

			new Expression(expressionStringAggregate, multiDimensionalExpression);
		} catch (IllegalArgumentException e) {
			verify(multiDimensionalExpression, times(2)).getStartPosition();
			verify(expressionStringAggregate).getTrimmedExpression();

			verify(multiDimensionalExpression).validateInState(AbstractExpression.STATE.FINALIZED);
			verify(multiDimensionalExpression).getLength();
			errorThrown = true;
		}
		assertTrue(errorThrown);

	}

	@Test()
	public void constructWithStringArguments() {
		Expression expression = new Expression(expressionStringAggregate, 4, "2+3");

		assertEquals(AbstractExpression.STATE.BUILD, expression.getState());
		assertEquals(4, expression.getStartPosition());
		assertEquals("2+3", expression.getContentAsString());
		assertEquals(3, expression.getLength());
		assertEquals(6, expression.getEndPosition());

		verify(expressionStringAggregate).getTrimmedExpression();
	}

	@Test()
	public void constructWithMultiDimensionalExpressionArguments() {
		when(multiDimensionalExpression.getDimension()).thenReturn(1);
		when(multiDimensionalExpression.getStartPosition()).thenReturn(3);
		when(multiDimensionalExpression.getLength()).thenReturn(5);

		Expression expression = new Expression(expressionStringAggregate, multiDimensionalExpression);

		assertEquals(3, expression.getStartPosition());
		assertEquals("$", expression.getContentAsString());
		assertEquals(5, expression.getLength());
		assertEquals(7, expression.getEndPosition());

		verify(multiDimensionalExpression, times(2)).getStartPosition();
		verify(expressionStringAggregate).getTrimmedExpression();
		verify(multiDimensionalExpression).getLength();

		initMocks();

		when(multiDimensionalExpression.getDimension()).thenReturn(1);
		when(multiDimensionalExpression.getStartPosition()).thenReturn(0);
		when(multiDimensionalExpression.getLength()).thenReturn(5);

		expression = new Expression(expressionStringAggregate, multiDimensionalExpression);

		assertEquals(0, expression.getStartPosition());
		assertEquals("$", expression.getContentAsString());
		assertEquals(5, expression.getLength());
		assertEquals(4, expression.getEndPosition());

		verify(multiDimensionalExpression, times(2)).getStartPosition();
		verify(expressionStringAggregate).getTrimmedExpression();
		verify(multiDimensionalExpression).getLength();
	}

	@Test
	public void addIllegalContent() {
		Expression expression;

		boolean errorThrown = false;

		try {
			errorThrown = false;
			expression = new Expression(expressionStringAggregate, 4, "2+3");
			expression.addContent(StringUtils.repeat(".", NUMBER_OF_CHARS_IN_TRIMMED_EXPRESSION));
		} catch (IllegalArgumentException e) {
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			errorThrown = false;
			expression = new Expression(expressionStringAggregate, 4, "2+3");

			when(multiDimensionalExpression.getDimension()).thenReturn(1);
			when(multiDimensionalExpression.getStartPosition()).thenReturn(2);
			when(multiDimensionalExpression.getLength()).thenReturn(6);

			expression = new Expression(expressionStringAggregate, multiDimensionalExpression);
			expression.addContent(StringUtils.repeat(".", NUMBER_OF_CHARS_IN_TRIMMED_EXPRESSION));
		} catch (IllegalArgumentException e) {
			errorThrown = true;
		}
		assertTrue(errorThrown);
	}

	@Test
	public void addContent() {
		Expression expression = new Expression(expressionStringAggregate, 4, "2+3");

		expression.addContent("-27");

		assertEquals(4, expression.getStartPosition());
		assertEquals("2+3-27", expression.getContentAsString());
		assertEquals(6, expression.getLength());
		assertEquals(9, expression.getEndPosition());

		when(multiDimensionalExpression.getDimension()).thenReturn(1);
		when(multiDimensionalExpression.getStartPosition()).thenReturn(2);
		when(multiDimensionalExpression.getEndPosition()).thenReturn(6);
		when(multiDimensionalExpression.getLength()).thenReturn(5);

		expression = new Expression(expressionStringAggregate, multiDimensionalExpression);

		assertEquals(AbstractExpression.STATE.BUILD, expression.getState());
		assertEquals(2, expression.getStartPosition());
		assertEquals("$", expression.getContentAsString());
		assertEquals(6, expression.getEndPosition());
		assertEquals(5, expression.getLength());

		expression.addContent("+2*7");
		assertEquals(2, expression.getStartPosition());
		assertEquals("$+2*7", expression.getContentAsString());
		assertEquals(10, expression.getEndPosition());
		assertEquals(9, expression.getLength());
	}

	@Test
	public void addIllegalMultiDimensionalExpressions() {
		Expression expression;

		boolean errorThrown = false;

		try {
			errorThrown = false;
			expression = new Expression(expressionStringAggregate, 20, "2+3");
			expression.addMultiDimensionalExpression(null);
		} catch (Exception e) {
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			errorThrown = false;
			doThrow(new IllegalArgumentException()).when(multiDimensionalExpression).validateInState(Expression.STATE.FINALIZED);
			expression = new Expression(expressionStringAggregate, 20, "2+3");
			expression.addMultiDimensionalExpression(multiDimensionalExpression);
		} catch (Exception e) {
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			errorThrown = false;
			when(multiDimensionalExpression.getDimension()).thenReturn(1);
			when(multiDimensionalExpression.getStartPosition()).thenReturn(22);
			expression = new Expression(expressionStringAggregate, 20, "2+3");
			expression.addMultiDimensionalExpression(multiDimensionalExpression);
		} catch (Exception e) {
			errorThrown = true;
		}
		assertTrue(errorThrown);

		try {
			errorThrown = false;
			when(multiDimensionalExpression.getDimension()).thenReturn(1);
			when(multiDimensionalExpression.getStartPosition()).thenReturn(23);
			expression = new Expression(expressionStringAggregate, 20, "2+3");
			expression.addMultiDimensionalExpression(multiDimensionalExpression);
		} catch (Exception e) {
			errorThrown = true;
		}
		assertTrue(errorThrown);

	}

	@Test
	public void addMultiDimensionalExpressions() {
		// "2+3*(6-8*2)-sum(4-9,45,88)/4"
		// "012345678901234567890123456789"
		String trimmedExpression = "2+3*(6-8*2)-sum(4-9,45,88)/4";
		when(expressionStringAggregate.getTrimmedExpression()).thenReturn(trimmedExpression);

		when(multiDimensionalExpression.getDimension()).thenReturn(1);
		when(multiDimensionalExpression.getStartPosition()).thenReturn(4);
		when(multiDimensionalExpression.getLength()).thenReturn(7);
		when(multiDimensionalExpression.toString()).thenReturn("(6-8*2)");

		when(secondMultiDimensionalExpression.getDimension()).thenReturn(3);
		when(secondMultiDimensionalExpression.getStartPosition()).thenReturn(15);
		when(secondMultiDimensionalExpression.getLength()).thenReturn(11);
		when(secondMultiDimensionalExpression.toString()).thenReturn("(4-9,45,88)");

		Expression expression = new Expression(expressionStringAggregate, 0, "2+3*");
		expression.addMultiDimensionalExpression(multiDimensionalExpression);
		expression.addContent("-sum");
		expression.addMultiDimensionalExpression(secondMultiDimensionalExpression);

		expression.addContent("/4");

		assertEquals(0, expression.getStartPosition());
		assertEquals("2+3*$-sum$/4", expression.getContentAsString());
		assertEquals(0, expression.getRealPosition(0));
		assertEquals(1, expression.getRealPosition(1));
		assertEquals(2, expression.getRealPosition(2));
		assertEquals(3, expression.getRealPosition(3));
		assertEquals(4, expression.getRealPosition(4));
		assertEquals(11, expression.getRealPosition(5));
		assertEquals(12, expression.getRealPosition(6));
		assertEquals(13, expression.getRealPosition(7));
		assertEquals(14, expression.getRealPosition(8));
		assertEquals(15, expression.getRealPosition(9));
		assertEquals(26, expression.getRealPosition(10));
		assertEquals(27, expression.getRealPosition(11));

		assertEquals(28, expression.getLength());
		assertEquals(trimmedExpression, expression.toString());
	}

}

package nl.smith.mathematics.factory;

import static nl.smith.mathematics.factory.constant.ArithmeticComponentName.ADDITION;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentName.DIVISION;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentName.MULTIPLICATION;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentName.NEGATION;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentName.SUBTRACTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.smith.mathematics.factory.constant.ArithmeticComponentName;
import nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression;
import nl.smith.mathematics.factory.constant.ArithmeticComponentStructure;
import nl.smith.mathematics.factory.stack.ArithmeticExpressionStack;
import nl.smith.mathematics.factory.stack.element.BinaryOperator;
import nl.smith.mathematics.factory.stack.element.UnaryOperator;
import nl.smith.mathematics.invoker.MathematicalFunctionInvoker;
import nl.smith.mathematics.number.NumberOperations;
import nl.smith.mathematics.number.RationalNumber;
import nl.smith.mathematics.utility.ErrorMessages;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class ArithmeticComponentResolverTest {

	@Mock
	private Expression expression;

	private static final Logger LOGGER = LoggerFactory.getLogger(ArithmeticComponentResolverTest.class);

	private static final Class<? extends NumberOperations> numberClass = RationalNumber.class;

	@Mock
	private MathematicalFunctionInvoker mathematicalFunctionInvoker;

	/** System under test */
	private ArithmeticComponentResolver arithmeticComponentResolver;

	@SuppressWarnings("unchecked")
	@Before
	public void init() {
		try {
			DummyMathematicalFunctionContainer container = new DummyMathematicalFunctionContainer();

			Method method = container.getClass().getDeclaredMethod("cosinus", new Class[] {});
			when(mathematicalFunctionInvoker.getProxyMethodPairForAlias("cos", 1)).thenReturn(new SimpleEntry<Object, Method>(this, method));

			method = container.getClass().getDeclaredMethod("sinus", new Class[] {});
			when(mathematicalFunctionInvoker.getProxyMethodPairForAlias("sin", 1)).thenReturn(new SimpleEntry<Object, Method>(this, method));

			method = container.getClass().getDeclaredMethod("faculty", new Class[] {});
			when(mathematicalFunctionInvoker.getProxyMethodPairForAlias("faculty", 1)).thenReturn(new SimpleEntry<Object, Method>(this, method));

			method = container.getClass().getDeclaredMethod("power", new Class[] {});
			when(mathematicalFunctionInvoker.getProxyMethodPairForAlias("pow", 2)).thenReturn(new SimpleEntry<Object, Method>(this, method));
			
			Answer<Class<? extends NumberOperations>> a = new Answer<Class<? extends NumberOperations>>() {

				@Override
				public Class<? extends NumberOperations> answer(InvocationOnMock invocation)
						throws Throwable {
					// TODO Auto-generated method stub
					return numberClass;
				}
			};
			when(mathematicalFunctionInvoker.getNumberClass()).then( a);
			arithmeticComponentResolver = new ArithmeticComponentResolver(mathematicalFunctionInvoker);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void instantiation() {
		new ArithmeticComponentResolver(null);
	}

	@Test
	public void instantiationCheckUnaryOperatorsMap() throws NoSuchMethodException, SecurityException {
		Map<ArithmeticComponentName, UnaryOperator> map = arithmeticComponentResolver.getUnaryOperatorsMap();

		assertEquals(1, map.size());
		for (ArithmeticComponentName arithmeticComponentName : Arrays.asList(NEGATION)) {
			UnaryOperator operator = map.get(arithmeticComponentName);
			assertNotNull(operator);
			Method actualMethod = operator.getMethod();
			Method expectedMethod = numberClass.getMethod(arithmeticComponentName.getMethodName());
			assertEquals(actualMethod, expectedMethod);
		}
	}

	@Test
	public void instantiationCheckBinaryOperatorsMap() throws NoSuchMethodException, SecurityException {
		Map<ArithmeticComponentName, BinaryOperator> map = arithmeticComponentResolver.getBinaryOperatorsMap();

		assertEquals(4, map.size());
		for (ArithmeticComponentName arithmeticComponentName : Arrays.asList(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION)) {
			BinaryOperator operator = map.get(arithmeticComponentName);
			assertNotNull(operator);
			Method actualMethod = operator.getMethod();
			Method expectedMethod = numberClass.getMethod(arithmeticComponentName.getMethodName(), numberClass);
			assertEquals(actualMethod, expectedMethod);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void getNumberElementsUsingBlankArgument() {
		ArithmeticComponentResolver.getNumberElements("\t\n\f ");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getNumberElementsUsingNonInterpretableInput() {
		String numberAsString = "12+ffff";

		try {
			ArithmeticComponentResolver.getNumberElements(numberAsString);
		} catch (IllegalArgumentException e) {
			String actualErrormessage = e.getMessage();

			String expectedErrorMessage = ErrorMessages.NOT_A_NUMBER.getFormattedErrorMessage(numberAsString);

			assertEquals(expectedErrorMessage, actualErrormessage);

			throw e;
		}
	}

	@Test
	public void getNumberElementsSpecifyingIntegerNumber() {
		String numberAsString = "-12";
		Map<ArithmeticComponentName, String> numberElements = ArithmeticComponentResolver.getNumberElements(numberAsString);
		assertEquals(1, numberElements.size());
		assertTrue(numberElements.containsKey(ArithmeticComponentName.INTEGER_NUMBER));
		assertEquals("-12", numberElements.get(ArithmeticComponentName.INTEGER_NUMBER));
	}

	@Test
	public void getNumberElementsSpecifyingDecimalNumber() {
		String numberAsString = "-13.567";
		Map<ArithmeticComponentName, String> numberElements = ArithmeticComponentResolver.getNumberElements(numberAsString);
		assertEquals(2, numberElements.size());
		assertTrue(numberElements.containsKey(ArithmeticComponentName.INTEGER_NUMBER));
		assertEquals("-13", numberElements.get(ArithmeticComponentName.INTEGER_NUMBER));
		assertTrue(numberElements.containsKey(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK));
		assertEquals("567", numberElements.get(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK));
	}

	@Test
	public void getNumberElementsSpecifyingDecimalNumberWithInfiniteDecimals() {
		String numberAsString = "-14.767{01234}R";
		Map<ArithmeticComponentName, String> numberElements = ArithmeticComponentResolver.getNumberElements(numberAsString);
		assertEquals(3, numberElements.size());
		assertTrue(numberElements.containsKey(ArithmeticComponentName.INTEGER_NUMBER));
		assertEquals("-14", numberElements.get(ArithmeticComponentName.INTEGER_NUMBER));
		assertTrue(numberElements.containsKey(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK));
		assertEquals("767", numberElements.get(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK));
		assertTrue(numberElements.containsKey(ArithmeticComponentName.FRACTION_REPEATING_BLOCK));
		assertEquals("01234", numberElements.get(ArithmeticComponentName.FRACTION_REPEATING_BLOCK));
	}

	@Test
	public void getNumberElementsSpecifyingScientificNumber() {
		String numberAsString = "-9.767{01234}RE23";
		Map<ArithmeticComponentName, String> numberElements = ArithmeticComponentResolver.getNumberElements(numberAsString);
		assertEquals(4, numberElements.size());
		assertTrue(numberElements.containsKey(ArithmeticComponentName.INTEGER_NUMBER));
		assertEquals("-9", numberElements.get(ArithmeticComponentName.INTEGER_NUMBER));
		assertTrue(numberElements.containsKey(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK));
		assertEquals("767", numberElements.get(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK));
		assertTrue(numberElements.containsKey(ArithmeticComponentName.FRACTION_REPEATING_BLOCK));
		assertEquals("01234", numberElements.get(ArithmeticComponentName.FRACTION_REPEATING_BLOCK));
		assertTrue(numberElements.containsKey(ArithmeticComponentName.EXPONENT));
		assertEquals("23", numberElements.get(ArithmeticComponentName.EXPONENT));
	}

	@Test
	public void getNumberElementsUsingMalformedNumberStrings() {
		Map<String, String> malFormatedNumberStrings = new HashMap<>();
		malFormatedNumberStrings.put(null, "Null string is not allowed");
		malFormatedNumberStrings.put("", "Empty string is not allowed");
		malFormatedNumberStrings.put("\t", "Blank string is not allowed");
		malFormatedNumberStrings.put("Osama", "Names are not allowed");
		malFormatedNumberStrings.put("00", "Only one zero is allowed");
		malFormatedNumberStrings.put("01", "Leading zeros are not allowed");
		malFormatedNumberStrings.put("1.0", "The last digit in the fractional part can not be zero");
		malFormatedNumberStrings.put("+1", "Redundant + sign is not allowed");
		malFormatedNumberStrings.put("--8", "Multiple negation signs are not allowed");
		malFormatedNumberStrings.put("2.12{00}R", "Only zeros in repeating fractional part are not allowed");
		malFormatedNumberStrings.put("10E44", "Integer part of the mantissa should be [1..9]");
		malFormatedNumberStrings.put("1E+5", "Redundant + sign in the exponent is not allowed");
		malFormatedNumberStrings.put("1E5.2", "Exponent should be an integer");
		malFormatedNumberStrings.put("1.0E5", "The last digit in the fractional part of the mantissa can not be zero");

		StringBuffer acceptedNumberStrings = new StringBuffer("The following number strings where (wrongly) accepted in the determination of number elements:\n");
		int numberOfFailures = 0;
		for (Entry<String, String> entry : malFormatedNumberStrings.entrySet()) {
			try {
				String numberAsString = entry.getKey();
				LOGGER.debug("Testing malformed number string {}", numberAsString);
				ArithmeticComponentResolver.getNumberElements(numberAsString);
				acceptedNumberStrings.append(numberAsString + "\t" + entry.getValue() + "\n");
			} catch (IllegalArgumentException e) {
				numberOfFailures++;
			}
		}

		assertEquals(acceptedNumberStrings.toString(), malFormatedNumberStrings.size(), numberOfFailures);
	}

	// TODO @Test
	public void getUnaryOperationElements() {
		ArithmeticComponentRegularExpression arithmeticComponentRegularExpression = ArithmeticComponentRegularExpression.UNARY_OPERATION;
		Pattern pattern = arithmeticComponentRegularExpression.pattern();

		String unaryExpressionString = "--2.45{34}RE4";
		Matcher matcher = pattern.matcher(unaryExpressionString);
		boolean isUnaryExpressionString = false;
		if (matcher.matches()) {
			isUnaryExpressionString = true;
			List<ArithmeticComponentStructure> structures = arithmeticComponentRegularExpression.getArithmeticComponentStructures();
			// ArithmeticExpressionStack arithmeticExpressionStack = arithmeticComponentResolver.buildUnaryOperationArithmeticExpressionStack(matcher, null);
			System.out.println();
		}

		assertTrue(isUnaryExpressionString);

		unaryExpressionString = "-Test";
		matcher = pattern.matcher(unaryExpressionString);
		isUnaryExpressionString = false;
		if (matcher.matches()) {
			isUnaryExpressionString = true;
			List<ArithmeticComponentStructure> structures = arithmeticComponentRegularExpression.getArithmeticComponentStructures();
			// ArithmeticExpressionStack arithmeticExpressionStack = arithmeticComponentResolver.buildUnaryOperationArithmeticExpressionStack(matcher, null);
			System.out.println();
		}

		assertTrue(isUnaryExpressionString);

		unaryExpressionString = "-Test$";
		matcher = pattern.matcher(unaryExpressionString);
		isUnaryExpressionString = false;
		if (matcher.matches()) {
			isUnaryExpressionString = true;
			List<ArithmeticComponentStructure> structures = arithmeticComponentRegularExpression.getArithmeticComponentStructures();
			// ArithmeticExpressionStack arithmeticExpressionStack = arithmeticComponentResolver.buildUnaryOperationArithmeticExpressionStack(matcher, null);
			System.out.println();
		}

		assertTrue(isUnaryExpressionString);
	}

	@Test
	public void buildArithmeticExpressionStack() {
		// "1+2*(-sin(34)*faculty(612)-85/514-pow(3,4)+7+8"
		// "0123456789012345678901234567890123456789012345"
		// "-sin(34)*faculty(612)-85/514-pow$"
		// "567890123456789001234567890123456"
		// "-sin($)*faculty($)-85/514a"
		String rawExpression = "1+2*(-sin(34)*faculty(612)-85/514-pow(3,4)+7+8";
		ExpressionStringAggregate expressionStringAggregate = new ExpressionStringAggregate(rawExpression);
		when(expression.getContentAsString()).thenReturn("-pow$*faculty$-85/514-sin$");
		when(expression.getExpressionStringAggregate()).thenReturn(expressionStringAggregate);

		List<MultiDimensionalExpression> multiDimensionalExpressions = new ArrayList<>();

		MultiDimensionalExpression multiDimensionalExpression = mock(MultiDimensionalExpression.class);
		when(multiDimensionalExpression.getDimension()).thenReturn(2);
		multiDimensionalExpressions.add(multiDimensionalExpression);
		multiDimensionalExpression = mock(MultiDimensionalExpression.class);
		when(multiDimensionalExpression.getDimension()).thenReturn(1);
		multiDimensionalExpressions.add(multiDimensionalExpression);
		multiDimensionalExpression = mock(MultiDimensionalExpression.class);
		when(multiDimensionalExpression.getDimension()).thenReturn(1);
		multiDimensionalExpressions.add(multiDimensionalExpression);

		when(expression.getMultiDimensionalExpressions()).thenReturn(multiDimensionalExpressions);

		arithmeticComponentResolver = new ArithmeticComponentResolver(mathematicalFunctionInvoker);

		ArithmeticExpressionStack expressionStack = arithmeticComponentResolver.buildArithmeticExpressionStack(expression);
		System.out.println(expressionStack);
	}

	private static class DummyMathematicalFunctionContainer {
		@SuppressWarnings({ "unused", })
		private void cosinus() {
			//
		}

		@SuppressWarnings({ "unused", })
		private void sinus() {
			//
		}

		@SuppressWarnings({ "unused", })
		private void faculty() {
			//
		}

		@SuppressWarnings({ "unused", })
		private void power() {
			//
		}
	}

}

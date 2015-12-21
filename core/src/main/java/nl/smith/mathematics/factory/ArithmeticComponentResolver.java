package nl.smith.mathematics.factory;

import static nl.smith.mathematics.factory.constant.ArithmeticComponentName.ADDITION;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentName.DIVISION;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentName.MULTIPLICATION;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentName.NEGATION;
import static nl.smith.mathematics.factory.constant.ArithmeticComponentName.SUBTRACTION;
import static nl.smith.mathematics.utility.ErrorMessages.NOT_A_NUMBER;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.smith.mathematics.factory.constant.ArithmeticComponentName;
import nl.smith.mathematics.factory.constant.ArithmeticComponentRegularExpression;
import nl.smith.mathematics.factory.constant.ArithmeticComponentStructure;
import nl.smith.mathematics.factory.stack.ArithmeticExpressionStack;
import nl.smith.mathematics.factory.stack.element.BinaryOperator;
import nl.smith.mathematics.factory.stack.element.Function;
import nl.smith.mathematics.factory.stack.element.Number;
import nl.smith.mathematics.factory.stack.element.SubExpressionStack;
import nl.smith.mathematics.factory.stack.element.UnaryOperator;
import nl.smith.mathematics.factory.stack.element.Variable;
import nl.smith.mathematics.invoker.MathematicalFunctionInvoker;
import nl.smith.mathematics.number.NumberOperations;
import nl.smith.mathematics.utility.ErrorMessages;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stateless utility class describing static protected and public constants
 * defined {@link RegularExpressionNumberConstant}
 * 
 * @author M. Smith
 */
public class ArithmeticComponentResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArithmeticComponentResolver.class);

	private final Constructor<? extends NumberOperations<?>> numberConstructor;

	private final Map<ArithmeticComponentName, UnaryOperator> unaryOperatorsMap;

	private final Map<ArithmeticComponentName, BinaryOperator> binaryOperatorsMap;

	private final MathematicalFunctionInvoker mathematicalFunctionInvoker;

	public ArithmeticComponentResolver(MathematicalFunctionInvoker mathematicalFunctionInvoker) {
		if (mathematicalFunctionInvoker == null) {
			throw new IllegalArgumentException("\nNo mathematicalFunctionInvoker specified");
		}

		numberConstructor = findNumberConstructor(mathematicalFunctionInvoker.getNumberClass());
		unaryOperatorsMap = createUnaryOperatorsMap(mathematicalFunctionInvoker.getNumberClass());
		binaryOperatorsMap = createBinaryOperatorsMap(mathematicalFunctionInvoker.getNumberClass());
		this.mathematicalFunctionInvoker = mathematicalFunctionInvoker;
	}

	private static Constructor<? extends NumberOperations<?>> findNumberConstructor(Class<? extends NumberOperations<?>> numberClass) {
		Constructor<? extends NumberOperations<?>> constructor;
		try {
			constructor = numberClass.getConstructor(Map.class);
			Type[] genericParameterTypes = constructor.getGenericParameterTypes();
			ParameterizedType genericParameterType = (ParameterizedType) genericParameterTypes[0];
			Type[] actualTypeArguments = genericParameterType.getActualTypeArguments();
			if (actualTypeArguments[0] != ArithmeticComponentName.class || actualTypeArguments[1] != String.class) {
				throw new IllegalArgumentException(String.format("\nThe map argument in the constructor of %s is of the wrong type.\n"
						+ "Expected: %1$s(Map<%s, %s>)\n" + "Actual: %1$s(Map<%s, %s)>\n", numberClass.getCanonicalName(),
						ArithmeticComponentName.class.getCanonicalName(), String.class.getCanonicalName(), actualTypeArguments[0], actualTypeArguments[1]));
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage());
		}

		return constructor;
	}

	private static Map<ArithmeticComponentName, UnaryOperator> createUnaryOperatorsMap(Class<? extends NumberOperations<?>> numberClass) {
		LOGGER.debug("Creating Unary Operators Map");

		Map<ArithmeticComponentName, UnaryOperator> unaryOperatorsMap = new HashMap<>();
		for (ArithmeticComponentName arithmeticComponentName : Arrays.asList(NEGATION)) {
			LOGGER.debug("Creating {} for method {}", UnaryOperator.class.getName(), arithmeticComponentName.getMethodName());
			unaryOperatorsMap.put(arithmeticComponentName, new UnaryOperator(getMethod(arithmeticComponentName.getMethodName(), numberClass)));
		}

		return Collections.unmodifiableMap(unaryOperatorsMap);
	}

	private static Map<ArithmeticComponentName, BinaryOperator> createBinaryOperatorsMap(Class<? extends NumberOperations<?>> numberClass) {
		LOGGER.debug("Creating Binary Operators Map");

		Map<ArithmeticComponentName, BinaryOperator> binaryOperatorsMap = new HashMap<>();
		for (ArithmeticComponentName arithmeticComponentName : Arrays.asList(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION)) {
			LOGGER.debug("Creating {} for method {}", BinaryOperator.class.getName(), arithmeticComponentName.getMethodName());
			binaryOperatorsMap.put(arithmeticComponentName, new BinaryOperator(getMethod(arithmeticComponentName.getMethodName(), numberClass, numberClass)));
		}

		return Collections.unmodifiableMap(binaryOperatorsMap);
	}

	@SafeVarargs
	private static Method getMethod(String methodName, Class<? extends NumberOperations<?>> numberClass, Class<? extends NumberOperations<?>>... arguments) {
		try {
			return numberClass.getMethod(methodName, arguments);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalStateException(String.format("\nPublic method %s.%s(%1$s) can not be found.", numberClass.getName(), methodName));
		}
	}

	public Map<ArithmeticComponentName, UnaryOperator> getUnaryOperatorsMap() {
		return unaryOperatorsMap;
	}

	public Map<ArithmeticComponentName, BinaryOperator> getBinaryOperatorsMap() {
		return binaryOperatorsMap;
	}

	public MathematicalFunctionInvoker getMathematicalFunctionInvoker() {
		return mathematicalFunctionInvoker;
	}

	/**
	 * Method to retrieve the different elements of a number string as a map The
	 * key is the elements type, the value is the value of the element as string
	 * The map contains:<br>
	 * <ul>
	 * <li>signedIntegerPart: ArithmeticComponentName.INTEGER_NUMBER</li>
	 * <li>nonRepeatingFractionalPart:
	 * ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK</li>
	 * <li>repeatingFractionalPart:
	 * ArithmeticComponentName.FRACTION_REPEATING_BLOCK</li>
	 * <li>exponentPart: ArithmeticComponentName.EXPONENT</li>
	 * <ul>
	 * <br>
	 * 
	 * @param numberAsString
	 * @return The map
	 */
	// TODO Test
	public static Map<ArithmeticComponentName, String> getNumberElements(String numberAsString) {
		if (StringUtils.isBlank(numberAsString)) {
			throw new IllegalArgumentException("\nNull or blank numberAsString is not accepted");
		}

		ArithmeticComponentRegularExpression arithmeticComponentRegularExpression = ArithmeticComponentRegularExpression.ANY_NUMBER;

		Map<ArithmeticComponentName, String> elements = null;
		Matcher matcher = arithmeticComponentRegularExpression.pattern().matcher(numberAsString);
		if (matcher.matches()) {
			List<ArithmeticComponentStructure> structures = arithmeticComponentRegularExpression.getArithmeticComponentStructures();
			for (ArithmeticComponentStructure structure : structures) {
				int groupIndex = structure.getGroupIndex();
				String value = matcher.group(groupIndex);
				if (StringUtils.isNotBlank(value)) {
					elements = getNumberElements(structure, matcher);
					break;
				}
			}
		} else {
			String errorMessage = NOT_A_NUMBER.getFormattedErrorMessage(numberAsString);
			throw new IllegalArgumentException(errorMessage);
		}

		return elements;
	}

	/**
	 * Precondition: arithmeticComponentStructure in {INTEGER_NUMBER,
	 * DECIMAL_NUMBER, DECIMAL_NUMBER_WITH_INFINITE_DECIMALS, SCIENTIFIC_NUMBER,
	 * SCIENTIFIC_NUMBER_WITH_INFINITE_DECIMALS}
	 * 
	 * @param arithmeticComponentStructure
	 * @param matcher
	 * @return
	 */
	private static Map<ArithmeticComponentName, String> getNumberElements(ArithmeticComponentStructure arithmeticComponentStructure, Matcher matcher) {
		ArithmeticComponentName arithmeticComponentName = arithmeticComponentStructure.getArithmeticComponentName();

		Map<ArithmeticComponentName, String> elements = new HashMap<>();

		if (arithmeticComponentName == ArithmeticComponentName.INTEGER_NUMBER) {
			ArithmeticComponentStructure structure = arithmeticComponentStructure;
			elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));

		} else if (arithmeticComponentName == ArithmeticComponentName.DECIMAL_NUMBER) {
			ArithmeticComponentStructure structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.INTEGER_NUMBER);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
			structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
		} else if (arithmeticComponentName == ArithmeticComponentName.DECIMAL_NUMBER_WITH_INFINITE_DECIMALS) {
			ArithmeticComponentStructure structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.INTEGER_NUMBER);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
			structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
			structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.FRACTION_REPEATING_BLOCK);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
		} else if (arithmeticComponentName == ArithmeticComponentName.SCIENTIFIC_NUMBER) {
			ArithmeticComponentStructure structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.INTEGER_NUMBER);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
			structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.NON_FUNCTIONAL,
					ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
			structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.EXPONENT);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
		} else if (arithmeticComponentName == ArithmeticComponentName.SCIENTIFIC_NUMBER_WITH_INFINITE_DECIMALS) {
			ArithmeticComponentStructure structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.INTEGER_NUMBER);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
			structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.FRACTION_NON_REPEATING_BLOCK);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
			structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.FRACTION_REPEATING_BLOCK);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
			structure = arithmeticComponentStructure.getStructureByNamedNumberParts(ArithmeticComponentName.EXPONENT);
			if (structure != null) {
				elements.put(structure.getArithmeticComponentName(), matcher.group(structure.getGroupIndex()));
			}
		} else {
			// TODO
			throw new IllegalArgumentException();
		}

		return elements;
	}

	public ArithmeticExpressionStack buildArithmeticExpressionStack(Expression expression) {
		if (expression == null) {
			throw new IllegalArgumentException("\nNull expression is not accepted");
		}

		String expressionString = expression.getContentAsString();
		List<MultiDimensionalExpression> multiDimensionalExpressions = new ArrayList<>(expression.getMultiDimensionalExpressions());
		MultiDimensionalExpression multiDimensionalExpression = null;

		if (StringUtils.isBlank(expressionString)) {
			throw new IllegalArgumentException("\nNull or blank expressionString is not accepted");
		}

		ArithmeticExpressionStack arithmeticExpressionStack = new ArithmeticExpressionStack();

		LOGGER.debug("Parsing '{}'", expressionString);

		ArithmeticComponentRegularExpression arithmeticComponentRegularExpression = ArithmeticComponentRegularExpression.UNARY_OPERATION;
		Pattern pattern = arithmeticComponentRegularExpression.pattern();
		Matcher matcher = pattern.matcher(expressionString);
		int offSetUnaryOperation = 0;
		int expectedBeginPosition = 0;

		if (matcher.find() && matcher.start() == expectedBeginPosition) {
			LOGGER.debug("Expression '{}' begins with the unary operation {}", expressionString, matcher.group());

			multiDimensionalExpression = null;
			if (matcher.group().contains(String.valueOf(Expression.EXPRESSION_PLACE_HOLDER))) {
				multiDimensionalExpression = multiDimensionalExpressions.get(0);
				multiDimensionalExpressions.remove(0);
			}

			arithmeticExpressionStack.add(buildOperationArithmeticExpressionStack(matcher, multiDimensionalExpression,
					ArithmeticComponentRegularExpression.UNARY_OPERATION));

			offSetUnaryOperation = matcher.end();
		} else {
			throw new IllegalArgumentException(ErrorMessages.EXPRESSION_DOES_NOT_START_WITH_UNARY_OPERATION.getFormattedErrorMessage(expression
					.getExpressionStringAggregate().getCaretedStringForPositions(ExpressionStringAggregate.ShowCaretString.IN_TRIMMED_EXPRESSION, true,
							expression.getStartPosition())));
		}

		// Remove the unary operation part
		expressionString = expressionString.substring(offSetUnaryOperation);
		LOGGER.debug("Parsing '{}'", expressionString);

		arithmeticComponentRegularExpression = ArithmeticComponentRegularExpression.BINARY_OPERATION;
		pattern = arithmeticComponentRegularExpression.pattern();
		matcher = pattern.matcher(expressionString);
		expectedBeginPosition = 0;

		while (matcher.find() && matcher.start() == expectedBeginPosition) {
			LOGGER.debug("Expression '{}' contains a binary operation {}", expressionString, matcher.group());

			multiDimensionalExpression = null;
			if (matcher.group().contains(String.valueOf(Expression.EXPRESSION_PLACE_HOLDER))) {
				multiDimensionalExpression = multiDimensionalExpressions.get(0);
				multiDimensionalExpressions.remove(0);
			}

			arithmeticExpressionStack.add(buildOperationArithmeticExpressionStack(matcher, multiDimensionalExpression,
					ArithmeticComponentRegularExpression.BINARY_OPERATION));

			expectedBeginPosition = matcher.end();
		}

		expressionString = expressionString.substring(expectedBeginPosition);
		if (StringUtils.isNotEmpty(expressionString)) {
			throw new IllegalArgumentException(ErrorMessages.UNEXPECTED_CONTENT_OPERATION.getFormattedErrorMessage(
					expressionString,
					expression.getExpressionStringAggregate().getCaretedStringForPositions(ExpressionStringAggregate.ShowCaretString.IN_TRIMMED_EXPRESSION,
							true, expression.getRealPosition(offSetUnaryOperation + expectedBeginPosition))));
		}

		return arithmeticExpressionStack;
	}

	/**
	 * Method to extract the different parts from the matcher for pattern:
	 * <ul>
	 * <li> {@link ArithmeticComponentRegularExpression#UNARY_OPERATION}
	 * <b>or</b></li>
	 * <li> {@link ArithmeticComponentRegularExpression#BINARY_OPERATION}</li>
	 * </ul>
	 * <br>
	 * <br>
	 * Protected for test purposes
	 */
	protected ArithmeticExpressionStack buildOperationArithmeticExpressionStack(Matcher matcher, MultiDimensionalExpression multiDimensionalExpression,
			ArithmeticComponentRegularExpression arithmeticComponentRegularExpression) {
		ArithmeticExpressionStack operationArithmeticExpressionStack = new ArithmeticExpressionStack();

		List<ArithmeticComponentStructure> unaryStructures;
		if (ArithmeticComponentRegularExpression.BINARY_OPERATION == arithmeticComponentRegularExpression) {
			List<ArithmeticComponentStructure> binaryStructures = arithmeticComponentRegularExpression.getArithmeticComponentStructures();
			// Determine binary operator
			ArithmeticComponentName arithmeticComponentName = ArithmeticComponentName.BINARY_OPERATOR;
			ArithmeticComponentStructure structure = ArithmeticComponentStructure.getStructureByNamedNumberParts(binaryStructures, arithmeticComponentName);
			String value = matcher.group(structure.getGroupIndex());
			if (StringUtils.isNotBlank(value)) {
				LOGGER.debug("Adding to stack : {}", binaryOperatorsMap.get(arithmeticComponentName));
				operationArithmeticExpressionStack.add(unaryOperatorsMap.get(arithmeticComponentName));
			} else {
				throw new IllegalArgumentException(ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("Illegal argument"));
			}

			arithmeticComponentName = ArithmeticComponentName.UNARY_OPERATION;
			structure = ArithmeticComponentStructure.getStructureByNamedNumberParts(binaryStructures, arithmeticComponentName);
			unaryStructures = structure.getArithmeticComponentStructures();
		} else if (ArithmeticComponentRegularExpression.UNARY_OPERATION == arithmeticComponentRegularExpression) {
			unaryStructures = arithmeticComponentRegularExpression.getArithmeticComponentStructures();
		} else {
			throw new IllegalArgumentException(ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("Illegal argument"));
		}

		ArithmeticExpressionStack unaryOperationArithmeticExpressionStack = buildUnaryOperationArithmeticExpressionStack(matcher, multiDimensionalExpression,
				unaryStructures);
		operationArithmeticExpressionStack.add(unaryOperationArithmeticExpressionStack);

		return unaryOperationArithmeticExpressionStack;

	}

	/**
	 * Method to extract the different parts from the matcher for pattern:
	 * <ul>
	 * <li> {@link ArithmeticComponentRegularExpression#UNARY_OPERATION}</li>
	 * </ul>
	 * <br>
	 * <br>
	 * Protected for test purposes
	 */
	private ArithmeticExpressionStack buildUnaryOperationArithmeticExpressionStack(Matcher matcher, MultiDimensionalExpression multiDimensionalExpression,
			List<ArithmeticComponentStructure> unaryStructures) {

		ArithmeticExpressionStack arithmeticExpressionStack = new ArithmeticExpressionStack();

		// Determine negation
		ArithmeticComponentName arithmeticComponentName = ArithmeticComponentName.NEGATION;
		ArithmeticComponentStructure structure = ArithmeticComponentStructure.getStructureByNamedNumberParts(unaryStructures, arithmeticComponentName);
		String value = matcher.group(structure.getGroupIndex());
		if (StringUtils.isNotBlank(value)) {
			LOGGER.debug("Adding to stack : {}", unaryOperatorsMap.get(arithmeticComponentName));
			arithmeticExpressionStack.add(unaryOperatorsMap.get(arithmeticComponentName));
		}

		// Determine any number equivalent
		boolean anyNumberEquivalentDetermined = false;
		structure = ArithmeticComponentStructure.getStructureByNamedNumberParts(unaryStructures, ArithmeticComponentName.ANY_NUMBER_EQUIVALENT);

		arithmeticComponentName = ArithmeticComponentName.SUBEXPRESSION;
		ArithmeticComponentStructure subStructure = structure.getStructureByNamedNumberParts(arithmeticComponentName);
		value = matcher.group(subStructure.getGroupIndex());
		if (StringUtils.isNotBlank(value)) {
			if (multiDimensionalExpression.getDimension() != 1) {
				throw new IllegalArgumentException(
						ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("Implement message: first element must always have dimension 1"));
			}
			anyNumberEquivalentDetermined = true;
			arithmeticExpressionStack.add(new SubExpressionStack());
		}

		if (!anyNumberEquivalentDetermined) {
			subStructure = structure.getStructureByNamedNumberParts(ArithmeticComponentName.FUNCTION, ArithmeticComponentName.NAME);
			value = matcher.group(subStructure.getGroupIndex());
			if (StringUtils.isNotBlank(value)) {
				int dimension = multiDimensionalExpression.getDimension();
				anyNumberEquivalentDetermined = true;
				SimpleEntry<Object, Method> proxyClassMethodPair = mathematicalFunctionInvoker.getProxyMethodPairForAlias(value, dimension);
				if (proxyClassMethodPair == null) {
					String argumentType = mathematicalFunctionInvoker.getNumberClass().getSimpleName();
					throw new IllegalArgumentException(ErrorMessages.METHOD_NOT_FOUND.getFormattedErrorMessage(value + "(" + argumentType
							+ StringUtils.repeat(", " + argumentType, dimension - 1) + ")"));
				}
				arithmeticExpressionStack.add(new Function(proxyClassMethodPair.getKey(), proxyClassMethodPair.getValue(), dimension));
			}
		}

		if (!anyNumberEquivalentDetermined) {
			subStructure = structure.getStructureByNamedNumberParts(ArithmeticComponentName.VARIABLE, ArithmeticComponentName.NAME);
			value = matcher.group(subStructure.getGroupIndex());
			if (StringUtils.isNotBlank(value)) {
				anyNumberEquivalentDetermined = true;
				arithmeticExpressionStack.add(new Variable(value));
			}
		}

		// Determine any number
		if (!anyNumberEquivalentDetermined) {
			structure = structure.getStructureByNamedNumberParts(ArithmeticComponentName.ANY_NUMBER);
			Map<ArithmeticComponentName, String> numberElements = null;

			for (ArithmeticComponentName componentName : new ArithmeticComponentName[] { ArithmeticComponentName.SCIENTIFIC_NUMBER_WITH_INFINITE_DECIMALS,
					ArithmeticComponentName.SCIENTIFIC_NUMBER, ArithmeticComponentName.DECIMAL_NUMBER_WITH_INFINITE_DECIMALS,
					ArithmeticComponentName.DECIMAL_NUMBER, ArithmeticComponentName.INTEGER_NUMBER }) {
				subStructure = structure.getStructureByNamedNumberParts(componentName);
				if (StringUtils.isNotBlank(matcher.group(subStructure.getGroupIndex()))) {
					anyNumberEquivalentDetermined = true;
					numberElements = getNumberElements(subStructure, matcher);
					break;
				}
			}

			if (numberElements != null) {
				try {
					arithmeticExpressionStack.add(new Number(numberConstructor.newInstance(numberElements)));
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
			}
		}

		if (!anyNumberEquivalentDetermined) {
			throw new IllegalArgumentException(
					ErrorMessages.IMPLEMENT_ERROR_MESSAGE.getFormattedErrorMessage("Implement message: anyNumberEquivalentDetermined false"));
		}

		return arithmeticExpressionStack;
	}

}

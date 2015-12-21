package nl.smith.mathematics.functions;

import java.util.List;

import nl.smith.mathematics.functions.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.number.NumberOperations;

@MathematicalFunctionContainer(name = "Statistical functions", description = "Set of statistical functions")
public abstract class StatisticalFunctions<T extends NumberOperations<?>> extends AbstractFunction {

	@SuppressWarnings("unchecked")
	public abstract T average(T... numbers);

	public abstract T average(List<T> numbers);

	@SuppressWarnings("unchecked")
	public abstract T deviation(T... numbers);
}

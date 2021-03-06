package nl.smith.mathematics.functions;

import java.util.List;

import nl.smith.mathematics.functions.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.number.NumberOperations;

@MathematicalFunctionContainer(name = "Simple arithmetic functions", description = "Set of simple arithmetic functions")
public abstract class SimpleArithmeticFunctions<T extends NumberOperations<?>> extends AbstractFunction<SimpleArithmeticFunctions<T>> {

	@SuppressWarnings("unchecked")
	public abstract T sum(T... numbers);

	public abstract T sum(List<T> numbers);

	public abstract T faculty(T number);
}

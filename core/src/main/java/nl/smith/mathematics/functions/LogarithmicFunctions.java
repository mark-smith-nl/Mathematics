package nl.smith.mathematics.functions;

import nl.smith.mathematics.functions.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.number.NumberOperations;

@MathematicalFunctionContainer(name = "Logarithmic functions", description = "Set of logarithmic functions")
public abstract class LogarithmicFunctions<T extends NumberOperations<?>> extends TaylorFunction<LogarithmicFunctions<T>> {

	public abstract T exp(T number);

	public abstract T ln(T number);

}
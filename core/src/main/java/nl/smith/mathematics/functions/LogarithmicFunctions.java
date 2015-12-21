package nl.smith.mathematics.functions;

import nl.smith.mathematics.functions.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.number.NumberOperations;

@MathematicalFunctionContainer(name = "Logarithmic functions", description = "Set of logarithmic functions")
public abstract class LogarithmicFunctions<T extends NumberOperations<?>> extends AbstractFunction {

	public abstract T getEulersNumber();

	public abstract T exp(T number);

	public abstract T ln(T number);

}
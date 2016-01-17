package nl.smith.mathematics.functions;

import nl.smith.mathematics.functions.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.number.NumberOperations;

@MathematicalFunctionContainer(name = "Goniometric functions", description = "Set of goniometric functions")
public abstract class GoniometricFunctions<T extends NumberOperations<?>> extends TaylorFunction<GoniometricFunctions<T>> {

	public abstract AngleType getANGELTYPE();

	public abstract T convertAngle(AngleType from, AngleType to);

	public abstract T getPI();

	public abstract T sin(T angle);

	public abstract T cos(T angle);

}
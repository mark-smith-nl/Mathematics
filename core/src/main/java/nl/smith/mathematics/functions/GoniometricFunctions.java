package nl.smith.mathematics.functions;

import nl.smith.mathematics.functions.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.number.NumberOperations;

@MathematicalFunctionContainer(name = "Goniometric functions", description = "Set of goniometric functions")
public abstract class GoniometricFunctions<T extends NumberOperations<?>> extends AbstractFunction<GoniometricFunctions<T>> {

	public GoniometricFunctions() {
		super();
	}

	public <F extends GoniometricFunctions<T>> GoniometricFunctions(F baseObject) {
		super(baseObject);
	}

	public abstract AngleType getAngleType();

	public abstract T convertAngle(AngleType from, AngleType to);

	public abstract T getPi();

	public abstract T sin(T angle);

	public abstract T cos(T angle);

}
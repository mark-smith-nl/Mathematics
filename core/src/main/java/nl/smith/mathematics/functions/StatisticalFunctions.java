package nl.smith.mathematics.functions;

import java.util.List;

import nl.smith.mathematics.functions.annotation.MathematicalFunctionContainer;
import nl.smith.mathematics.number.NumberOperations;

@MathematicalFunctionContainer(name = "Statistical functions", description = "Set of statistical functions")
public abstract class StatisticalFunctions<T extends NumberOperations<?>> extends AbstractFunction {

	public StatisticalFunctions() {
		super();
	}

	public <F extends StatisticalFunctions<T>> StatisticalFunctions(F baseObject) {
		super(baseObject);
	}

	@SuppressWarnings("unchecked")
	public abstract T average(T... numbers);

	public abstract T average(List<T> numbers);

	@SuppressWarnings("unchecked")
	public abstract T deviation(T... numbers);
}

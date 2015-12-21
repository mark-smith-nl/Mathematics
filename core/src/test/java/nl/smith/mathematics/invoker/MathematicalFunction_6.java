package nl.smith.mathematics.invoker;

import nl.smith.mathematics.functions.annotation.MathematicalFunction;
import nl.smith.mathematics.number.NumberOperations;

public class MathematicalFunction_6 {

	@SuppressWarnings("unused")
	@MathematicalFunction
	public String doItWithWrongReturnType(NumberOperations<?> arg1) {
		return null;
	}

}

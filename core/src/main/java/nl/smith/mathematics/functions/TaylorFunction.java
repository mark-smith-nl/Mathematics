package nl.smith.mathematics.functions;

import java.math.BigInteger;

import nl.smith.mathematics.functions.annotation.FunctionProperty;

public abstract class TaylorFunction<T extends AbstractFunction<?>> extends AbstractFunction<T> {
	@FunctionProperty(simplePropertyName = "taylorNumber", prefixWithCanonicalClassName = false)
	private BigInteger taylorNumber;

	public TaylorFunction() {

	}

	public TaylorFunction(T baseObject) {
		super(baseObject);
	}

}

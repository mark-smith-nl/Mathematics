package nl.smith.mathematics.functions;

import java.math.BigInteger;

import nl.smith.mathematics.functions.annotation.FunctionProperty;

public abstract class TaylorFunction<T extends AbstractFunction<?>> extends AbstractFunction<T> {
	@FunctionProperty
	private final BigInteger TAYLORNUMBER = null;

	public BigInteger getTAYLORNUMBER() {
		return TAYLORNUMBER;
	}

}

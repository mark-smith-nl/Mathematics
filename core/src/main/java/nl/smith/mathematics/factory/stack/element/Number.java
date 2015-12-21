package nl.smith.mathematics.factory.stack.element;

import nl.smith.mathematics.number.NumberOperations;

public class Number extends AbstractArithmeticExpressionStackElement {

	private final NumberOperations<?> number;

	public Number(NumberOperations<?> number) {
		this.number = number;
	}

	public NumberOperations<?> getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return "NumberStackElement [number=" + number.toString() + "]";
	}

}

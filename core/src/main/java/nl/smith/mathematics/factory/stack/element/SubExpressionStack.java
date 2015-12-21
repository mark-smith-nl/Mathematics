package nl.smith.mathematics.factory.stack.element;

import nl.smith.mathematics.factory.stack.ArithmeticExpressionStack;

public class SubExpressionStack extends AbstractArithmeticExpressionStackElement {

	private ArithmeticExpressionStack stack;

	public SubExpressionStack() {

	}

	public ArithmeticExpressionStack getStack() {
		return stack;
	}

	public void setStack(ArithmeticExpressionStack stack) {
		this.stack = stack;
	}

	@Override
	public String toString() {
		return "NumberStackElement [number=" + stack.toString() + "]";
	}

}

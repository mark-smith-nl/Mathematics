package nl.smith.mathematics.factory.stack;

import java.util.ArrayList;
import java.util.List;

import nl.smith.mathematics.factory.stack.element.AbstractArithmeticExpressionStackElement;
import nl.smith.mathematics.factory.stack.element.SubExpressionStack;

public class ArithmeticExpressionStack {
	private final List<AbstractArithmeticExpressionStackElement> stack;

	public ArithmeticExpressionStack() {
		stack = new ArrayList<AbstractArithmeticExpressionStackElement>();
	}

	public void add(AbstractArithmeticExpressionStackElement expressionStackElement) {
		stack.add(expressionStackElement);
	}

	public void add(ArithmeticExpressionStack arithmeticExpressionStack) {
		stack.addAll(arithmeticExpressionStack.stack);
	}

	public List<SubExpressionStack> getSubExpressionStacks() {
		List<SubExpressionStack> subExpressionStacks = new ArrayList<>();
		for (AbstractArithmeticExpressionStackElement abstractArithmeticExpressionStackElement : stack) {
			if (abstractArithmeticExpressionStackElement instanceof SubExpressionStack) {
				subExpressionStacks.add((SubExpressionStack) abstractArithmeticExpressionStackElement);

			}
		}

		return subExpressionStacks;
	}

}

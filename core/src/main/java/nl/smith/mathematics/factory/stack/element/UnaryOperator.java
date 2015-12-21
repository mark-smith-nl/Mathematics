package nl.smith.mathematics.factory.stack.element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import nl.smith.mathematics.number.NumberOperations;

public class UnaryOperator extends AbstractArithmeticExpressionStackElement {

	private final Method method;

	public UnaryOperator(Method method) {
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	@SuppressWarnings("unchecked")
	public <T extends NumberOperations<?>> T getResult(T numberOne) {
		T result = null;
		try {
			result = (T) method.invoke(numberOne);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO
			throw new IllegalArgumentException(e);
		}
		return result;
	}

	@Override
	public String toString() {
		return "UnaryOperatorStackElement [method=" + method.getName() + "]";
	}

}

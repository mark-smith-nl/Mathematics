package nl.smith.mathematics.factory.stack.element;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO Implementation
public class Function extends AbstractArithmeticExpressionStackElement {

	private static final Logger LOGGER = LoggerFactory.getLogger(Function.class);

	private final Object proxy;

	private final Method method;

	private final int numberOfArguments;

	public Function(Object proxy, Method method, int numberOfArguments) {
		LOGGER.debug("Creating {} for {}.{} using {} number(s)",
				new Object[] { Function.class.getName(), proxy.getClass().getCanonicalName(), method.getName(), numberOfArguments });

		this.proxy = proxy;
		this.method = method;
		this.numberOfArguments = numberOfArguments;
	}

	public Object getProxy() {
		return proxy;
	}

	public Method getMethod() {
		return method;
	}

	public int getNumberOfArguments() {
		return numberOfArguments;
	}

	@Override
	public String toString() {
		return "Function [method=" + method.getName() + "]";
	}

}

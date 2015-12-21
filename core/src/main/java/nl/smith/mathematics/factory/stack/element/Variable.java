package nl.smith.mathematics.factory.stack.element;

public class Variable extends AbstractArithmeticExpressionStackElement {
	private final String name;

	public Variable(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Variable [name=" + name + "]";
	}

}

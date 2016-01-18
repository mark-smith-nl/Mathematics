package nl.smith.development;

import nl.smith.mathematics.functions.AbstractFunction;
import nl.smith.mathematics.functions.annotation.FunctionProperty;
import nl.smith.mathematics.number.RationalNumber;

public class Main {

	public static class A extends AbstractFunction<AbstractFunction<A>> {
		@FunctionProperty(simplePropertyName = "Olama", prefixWithCanonicalClassName = true)
		// private final RationalNumber oneSeventh = new RationalNumber(1, 7);
		private final RationalNumber oneSeventh = null;

		@FunctionProperty(simplePropertyName = "Olama", prefixWithCanonicalClassName = false)
		// private final RationalNumber oneSeventh = new RationalNumber(1, 7);
		private final RationalNumber duplicate = null;
	}

	public static class B extends A {
		@FunctionProperty(simplePropertyName = "Olama", prefixWithCanonicalClassName = true)
		private final RationalNumber oneSeventh = null;
	}

	public static class C extends B {
		@FunctionProperty(simplePropertyName = "Olama", prefixWithCanonicalClassName = true)
		private final RationalNumber oneSeventh = null;

		public C() {
			this.setFunctionProperties();
		}
	}

	public static void main(String[] args) {
		System.setProperty(A.class.getCanonicalName() + ".Olama.value", "0.{142857}R");
		System.setProperty(A.class.getCanonicalName() + ".Olama.type", "nl.smith.mathematics.number.RationalNumber");
		System.setProperty(B.class.getCanonicalName() + ".Olama.value", "0.{285714}R");
		System.setProperty(B.class.getCanonicalName() + ".Olama.type", "nl.smith.mathematics.number.RationalNumber");
		System.setProperty(C.class.getCanonicalName() + ".Olama.value", "0.{428571}R");
		System.setProperty(C.class.getCanonicalName() + ".Olama.type", "nl.smith.mathematics.number.RationalNumber");
		C c = new C();
		System.out.println(c);

	}

}

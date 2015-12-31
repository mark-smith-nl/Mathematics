package nl.smith.mathematics.main;

import nl.smith.mathematics.functions.rational.LogarithmicFunctionsImpl;

public abstract class MainTest {

	public static void main(String[] args) {
		LogarithmicFunctionsImpl functionsImpl = new LogarithmicFunctionsImpl();
		System.out.println(functionsImpl.getEulersNumber());

	}

}

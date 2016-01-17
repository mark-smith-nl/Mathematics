package nl.smith.mathematics.main;

import nl.smith.mathematics.functions.rational.GoniometricFunctionsImpl;

public abstract class MainTest {

	public static void main(String[] args) {
		GoniometricFunctionsImpl functionsImpl = new GoniometricFunctionsImpl();
		System.out.println(functionsImpl.getTAYLORNUMBER());

	}

}

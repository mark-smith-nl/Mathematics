package nl.smith.mathematics.number;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RationalNumberMatrixTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(RationalNumberMatrixTest.class);

	@Test(expected = IllegalArgumentException.class)
	public void constructorNullElements() {
		RationalNumber[][] elements = null;

		try {
			new RationalNumberMatrix(elements);
		} catch (Exception e) {
			LOGGER.info("\nExpected exception thrown:\n" + e.getMessage());
			throw e;
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorNoRows() {
		RationalNumber[][] elements = new RationalNumber[0][];
		try {
			new RationalNumberMatrix(elements);
		} catch (Exception e) {
			LOGGER.info("\nExpected exception thrown:\n" + e.getMessage());
			throw e;
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorNoColumns() {
		RationalNumber[][] elements = new RationalNumber[2][];
		try {
			new RationalNumberMatrix(elements);
		} catch (Exception e) {
			LOGGER.info("\nExpected exception thrown:\n" + e.getMessage());
			throw e;
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorMissingColumn() {
		RationalNumber[][] elements = new RationalNumber[3][];
		elements[0] = new RationalNumber[3];
		elements[1] = new RationalNumber[3];

		try {
			new RationalNumberMatrix(elements);
		} catch (Exception e) {
			LOGGER.info("\nExpected exception thrown:\n" + e.getMessage());
			throw e;
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorWrongColumnSize() {
		RationalNumber[][] elements = new RationalNumber[3][];
		elements[0] = new RationalNumber[3];
		elements[1] = new RationalNumber[3];
		elements[2] = new RationalNumber[4];

		try {
			new RationalNumberMatrix(elements);
		} catch (Exception e) {
			LOGGER.info("\nExpected exception thrown:\n" + e.getMessage());
			throw e;
		}
	}

	@Test
	public void constructor() {
		RationalNumber[][] elements = new RationalNumber[3][2];
		elements[0][0] = new RationalNumber(1);
		elements[0][1] = new RationalNumber(2);
		elements[1][0] = new RationalNumber(3);
		elements[1][1] = new RationalNumber(4);
		elements[2][0] = new RationalNumber(5, 3);
		elements[2][1] = new RationalNumber(6);

		new RationalNumberMatrix(elements);

	}

	@Test(expected = ArithmeticException.class)
	public void getDeterminantNonSquareMatrix() {
		RationalNumber[][] elements = new RationalNumber[][] {
				{ new RationalNumber(2), new RationalNumber(0), new RationalNumber(0), new RationalNumber(1) },
				{ new RationalNumber(0), new RationalNumber(1), new RationalNumber(3), new RationalNumber(-3) },
				{ new RationalNumber(-2), new RationalNumber(-3), new RationalNumber(-5), new RationalNumber(2) }
		};
		try {
			RationalNumberMatrix rationNumberMatrix = new RationalNumberMatrix(elements);
			rationNumberMatrix.getDeterminant();
		} catch (Exception e) {
			LOGGER.info("\nExpected exception thrown:\n" + e.getMessage());
			throw e;
		}
	}

	@Test
	public void getDeterminantSquareMatrix() {
		RationalNumber[][] elements;
		RationalNumberMatrix rationNumberMatrix;

		elements = new RationalNumber[][] {
				{ new RationalNumber(2) }
		};

		rationNumberMatrix = new RationalNumberMatrix(elements);
		assertEquals(rationNumberMatrix.getDeterminant(), new RationalNumber(2));

		elements = new RationalNumber[][] {
				{ new RationalNumber(1), new RationalNumber(2) },
				{ new RationalNumber(4), new RationalNumber(3) },
		};

		rationNumberMatrix = new RationalNumberMatrix(elements);
		assertEquals(rationNumberMatrix.getDeterminant(), new RationalNumber(-5));

		elements = new RationalNumber[][] {
				{ new RationalNumber(13), new RationalNumber(2), new RationalNumber(18) },
				{ new RationalNumber(-4), new RationalNumber(-2), new RationalNumber(-1) },
				{ new RationalNumber(7), new RationalNumber(-5), new RationalNumber(-11) },
		};

		rationNumberMatrix = new RationalNumberMatrix(elements);
		assertEquals(rationNumberMatrix.getDeterminant(), new RationalNumber(731));

		elements = new RationalNumber[][] {
				{ new RationalNumber(2), new RationalNumber(0), new RationalNumber(0), new RationalNumber(1) },
				{ new RationalNumber(0), new RationalNumber(1), new RationalNumber(3), new RationalNumber(-3) },
				{ new RationalNumber(-2), new RationalNumber(-3), new RationalNumber(-5), new RationalNumber(2) },
				{ new RationalNumber(4), new RationalNumber(-4), new RationalNumber(4), new RationalNumber(-6) }
		};

		rationNumberMatrix = new RationalNumberMatrix(elements);
		assertEquals(rationNumberMatrix.getDeterminant(), new RationalNumber(32));

		elements = new RationalNumber[][] {
				{ new RationalNumber(5), new RationalNumber(2), new RationalNumber(0), new RationalNumber(0), new RationalNumber(-2) },
				{ new RationalNumber(0), new RationalNumber(1), new RationalNumber(4), new RationalNumber(3), new RationalNumber(2) },
				{ new RationalNumber(0), new RationalNumber(0), new RationalNumber(2), new RationalNumber(6), new RationalNumber(3) },
				{ new RationalNumber(0), new RationalNumber(0), new RationalNumber(3), new RationalNumber(4), new RationalNumber(1) },
				{ new RationalNumber(0), new RationalNumber(0), new RationalNumber(0), new RationalNumber(0), new RationalNumber(2) }
		};

		rationNumberMatrix = new RationalNumberMatrix(elements);
		assertEquals(rationNumberMatrix.getDeterminant(), new RationalNumber(-100));

	}

}

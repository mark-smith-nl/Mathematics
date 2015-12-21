package nl.smith.mathematics.functions.rational;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Properties;

import nl.smith.mathematics.functions.AngleType;
import nl.smith.mathematics.functions.GoniometricFunctions;
import nl.smith.mathematics.number.RationalNumber;
import nl.smith.mathematics.utility.TestUtility;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(classes = { FunctionFinder.class })
public class GoniometricFunctionsImplTest {

	// Test resource dependencies
	private static final String COSINUS_PROPERTIES = "cosinus.properties";
	private static final String SINUS_PROPERTIES = "sinus.properties";

	private static final AngleType ANGLE_TYPE = AngleType.RAD;

	private static final RationalNumber PI = RationalNumber.valueOf("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");

	@Test
	public void sin() {
		Properties properties = TestUtility.getProperties(SINUS_PROPERTIES);

		RationalNumber angle = PI.divide(4);

		for (int i = 1; i <= 30; i++) {
			System.setProperty("taylorNumber", BigInteger.valueOf(i).toString());
			System.setProperty("angleType", ANGLE_TYPE.toString());
			System.setProperty("PI", PI.toStringExact());
			GoniometricFunctions<RationalNumber> goniometricFunctions = new GoniometricFunctionsImpl();
			String propertyName = "sinus_" + StringUtils.right(StringUtils.repeat("0", 3) + String.valueOf(i), 3);
			RationalNumber expected = RationalNumber.valueOf(properties.getProperty(propertyName));
			RationalNumber actual = goniometricFunctions.sin(angle);
			RationalNumber allowedDelta = new RationalNumber(1, 10000000000000000L);
			RationalNumber delta = expected.subtract(actual).abs();
			assertEquals(allowedDelta.compareTo(delta), 1);
		}

	}

	@Test
	public void cos() {
		Properties properties = TestUtility.getProperties(COSINUS_PROPERTIES);

		RationalNumber angle = PI.divide(4);

		for (int i = 1; i <= 30; i++) {
			System.setProperty("taylorNumber", BigInteger.valueOf(i).toString());
			System.setProperty("angleType", ANGLE_TYPE.toString());
			System.setProperty("PI", PI.toStringExact());
			GoniometricFunctions<RationalNumber> goniometricFunctions = new GoniometricFunctionsImpl();
			String propertyName = "cosinus_" + StringUtils.right(StringUtils.repeat("0", 3) + String.valueOf(i), 3);
			RationalNumber expected = RationalNumber.valueOf(properties.getProperty(propertyName));
			RationalNumber actual = goniometricFunctions.cos(angle);
			RationalNumber allowedDelta = new RationalNumber(1, 10000000000000000L);
			RationalNumber delta = expected.subtract(actual).abs();
			assertEquals(allowedDelta.compareTo(delta), 1);
		}

	}
}

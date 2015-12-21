package nl.smith.mathematics.utility;

import static nl.smith.mathematics.utility.ApplicationProperties.getApplicationProperties;
import static nl.smith.mathematics.utility.SystemPropertyUtil.copyProperties;
import static nl.smith.mathematics.utility.SystemPropertyUtil.setSystemProperties;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Properties;

import nl.smith.mathematics.functions.AngleType;
import nl.smith.mathematics.number.RationalNumber;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ApplicationPropertiesTest {

	private Properties storedProperties = new Properties();

	@Before
	public void init() {
		copyProperties(System.getProperties(), storedProperties);
		getApplicationProperties().reset();
	}

	@After()
	public void after() {
		System.getProperties().clear();
		copyProperties(storedProperties, System.getProperties());
	}

	@Test(expected = IllegalStateException.class)
	public void defaultPropertyUnspecified() {
		getApplicationProperties().getObjectProperty(Boolean.class, "Application", "persist");
	}

	@Test
	public void getSpecifiedProperty() {
		String fullyQualifiedPropertyName = "nl.smith.mathematics.functions.QGoniometricFunctionsImpl.angleType";
		Class<AngleType> propertyType = AngleType.class;
		AngleType expected = AngleType.RAD;
		AngleType actual = getApplicationProperties().getObjectProperty(propertyType, fullyQualifiedPropertyName);
		assertEquals(String.format("Test property: %s", fullyQualifiedPropertyName), expected, actual);
	}

	@Test
	public void getSpecifiedPropertySetAsSystemProperty() {
		Properties properties = new Properties();

		String fullyQualifiedPropertyNameOne = "nl.smith.mathematics.functions.QGoniometricFunctionsImpl.angleType";
		properties.setProperty(fullyQualifiedPropertyNameOne, "GRAD");

		String fullyQualifiedPropertyNameTwo = "nl.smith.mathematics.functions.TaylorSeriesImpl.taylorNumber";
		properties.setProperty(fullyQualifiedPropertyNameTwo, "50");

		String fullyQualifiedPropertyNameThree = "nl.smith.mathematics.functions.QGoniometricFunctionsImpl.PI";
		properties.setProperty(fullyQualifiedPropertyNameThree, "3.14");

		ApplicationProperties applicationContext = getApplicationProperties();

		setSystemProperties(properties, getApplicationProperties());

		assertEquals(String.format("Test property: %s", fullyQualifiedPropertyNameOne), AngleType.GRAD,
				applicationContext.getObjectProperty(AngleType.class, fullyQualifiedPropertyNameOne));

		assertEquals(String.format("Test property: %s", fullyQualifiedPropertyNameTwo), BigInteger.valueOf(50),
				applicationContext.getObjectProperty(BigInteger.class, fullyQualifiedPropertyNameTwo));

		assertEquals(String.format("Test property: %s", fullyQualifiedPropertyNameThree), RationalNumber.valueOf("3.14"),
				applicationContext.getObjectProperty(RationalNumber.class, fullyQualifiedPropertyNameThree));

	}

}

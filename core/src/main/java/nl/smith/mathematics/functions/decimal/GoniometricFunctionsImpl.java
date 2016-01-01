package nl.smith.mathematics.functions.decimal;

import java.math.BigInteger;

import nl.smith.mathematics.functions.AngleType;
import nl.smith.mathematics.functions.GoniometricFunctions;
import nl.smith.mathematics.functions.annotation.FunctionProperty;
import nl.smith.mathematics.number.DecimalNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GoniometricFunctionsImpl extends GoniometricFunctions<DecimalNumber> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(GoniometricFunctionsImpl.class);

	@FunctionProperty
	private BigInteger taylorNumber;

	@FunctionProperty
	private AngleType angleType;

	@FunctionProperty
	private DecimalNumber pi;

	/** Spring instantiated bean */
	public GoniometricFunctionsImpl() {
		super();
	}

	// Constructor for instantiating proxy
	public GoniometricFunctionsImpl(GoniometricFunctionsImpl baseObject) {
		super(baseObject);
	}

	public BigInteger getTaylorNumber() {
		return taylorNumber;
	}

	@Override
	public AngleType getAngleType() {
		// TODO Auto-generated method stub
		return angleType;
	}

	@Override
	public DecimalNumber convertAngle(AngleType from, AngleType to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecimalNumber getPi() {
		// TODO Auto-generated method stub
		return pi;
	}

	@Override
	public DecimalNumber sin(DecimalNumber angle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecimalNumber cos(DecimalNumber angle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "GoniometricFunctionsImpl [taylorNumber=" + taylorNumber + ", angleType=" + angleType + ", pi=" + pi + "]";
	}

}

package nl.smith.mathematics.functions.decimal;

import java.math.BigInteger;

import nl.smith.mathematics.functions.AngleType;
import nl.smith.mathematics.functions.GoniometricFunctions;
import nl.smith.mathematics.number.DecimalNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class GoniometricFunctionsImpl extends GoniometricFunctions<DecimalNumber> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(GoniometricFunctionsImpl.class);

	private final BigInteger taylorNumber;

	private final AngleType angleType;

	private final DecimalNumber pi;

	/** Spring instantiated bean */
	public GoniometricFunctionsImpl() {
		taylorNumber = (BigInteger) getFunctionContext().get("taylorNumber");
		angleType = (AngleType) getFunctionContext().get("angleType");
		pi = (DecimalNumber) getFunctionContext().get("PI");
	}

	// Constructor for instantiating proxy
	public GoniometricFunctionsImpl(GoniometricFunctionsImpl goniometricFunctionsImpl) {
		taylorNumber = goniometricFunctionsImpl.taylorNumber;
		angleType = goniometricFunctionsImpl.angleType;
		pi = goniometricFunctionsImpl.pi;
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

}
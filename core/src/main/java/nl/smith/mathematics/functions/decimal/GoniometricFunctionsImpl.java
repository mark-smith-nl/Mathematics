package nl.smith.mathematics.functions.decimal;

import nl.smith.mathematics.functions.AngleType;
import nl.smith.mathematics.functions.GoniometricFunctions;
import nl.smith.mathematics.functions.annotation.FunctionProperty;
import nl.smith.mathematics.number.DecimalNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GoniometricFunctionsImpl extends GoniometricFunctions<DecimalNumber> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GoniometricFunctionsImpl.class);

	@FunctionProperty
	private final AngleType ANGELTYPE = null;

	@FunctionProperty
	private final DecimalNumber PI = null;

	/** Spring instantiated bean */
	public GoniometricFunctionsImpl() {
		super();
	}

	/** Constructor for instantiating proxy */
	public GoniometricFunctionsImpl(GoniometricFunctionsImpl baseObject) {
		LOGGER.info("Create instance proxy instance of class {} using {}", this.getClass().getCanonicalName(), baseObject.toString());
		setFunctionProperties(baseObject);
	}

	@Override
	public AngleType getANGELTYPE() {
		// TODO Auto-generated method stub
		return ANGELTYPE;
	}

	@Override
	public DecimalNumber convertAngle(AngleType from, AngleType to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecimalNumber getPI() {
		// TODO Auto-generated method stub
		return PI;
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
		return "GoniometricFunctionsImpl [TAYLORNUMBER=" + getTAYLORNUMBER() + ", ANGELTYPE=" + getANGELTYPE() + ", PI=" + getPI() + "]";
	}

}

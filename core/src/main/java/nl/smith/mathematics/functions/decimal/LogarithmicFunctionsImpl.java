package nl.smith.mathematics.functions.decimal;

import nl.smith.mathematics.functions.LogarithmicFunctions;
import nl.smith.mathematics.functions.annotation.FunctionProperty;
import nl.smith.mathematics.number.DecimalNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogarithmicFunctionsImpl extends LogarithmicFunctions<DecimalNumber> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogarithmicFunctionsImpl.class);

	@FunctionProperty
	private DecimalNumber eulersNumber;

	/** Spring instantiated bean */
	public LogarithmicFunctionsImpl() {
		setFunctionProperties();
	}

	/** Constructor for instantiating proxy */
	public LogarithmicFunctionsImpl(LogarithmicFunctionsImpl baseObject) {
		LOGGER.info("Create instance proxy instance of class {} using {}", this.getClass().getCanonicalName(), baseObject.toString());
		setFunctionProperties(baseObject);
	}

	public DecimalNumber getEulersNumber() {
		return eulersNumber;
	}

	@Override
	public DecimalNumber exp(DecimalNumber number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecimalNumber ln(DecimalNumber number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "LogarithmicFunctionsImpl [TAYLORNUMBER=" + getTAYLORNUMBER() + ", eulersNumber=" + eulersNumber + "]";
	}
}

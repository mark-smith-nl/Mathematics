package nl.smith.mathematics.functions.decimal;

import java.math.BigInteger;

import nl.smith.mathematics.functions.LogarithmicFunctions;
import nl.smith.mathematics.functions.annotation.FunctionProperty;
import nl.smith.mathematics.number.DecimalNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogarithmicFunctionsImpl extends LogarithmicFunctions<DecimalNumber> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(LogarithmicFunctionsImpl.class);

	@FunctionProperty
	private BigInteger taylorNumber;

	@FunctionProperty
	private DecimalNumber eulersNumber;

	/** Spring instantiated bean */
	public LogarithmicFunctionsImpl() {
		super();
	}

	// Constructor for instantiating proxy
	public LogarithmicFunctionsImpl(LogarithmicFunctionsImpl baseObject) {
		super(baseObject);
	}

	public BigInteger getTaylorNumber() {
		return taylorNumber;
	}

	@Override
	public DecimalNumber getEulersNumber() {
		// TODO Auto-generated method stub
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
		return "LogarithmicFunctionsImpl [taylorNumber=" + taylorNumber + ", eulersNumber=" + eulersNumber + "]";
	}
}

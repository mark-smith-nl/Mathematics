package nl.smith.mathematics.functions;

import java.util.Collections;
import java.util.Map;

import nl.smith.mathematics.utility.FunctionContextHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFunction {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFunction.class);

	private final Map<String, Object> functionContext;

	public AbstractFunction() {
		LOGGER.debug("Create context for class {}", this.getClass().getCanonicalName());
		functionContext = Collections.unmodifiableMap(FunctionContextHelper.makeFunctionContext(this.getClass()));
	}

	public Map<String, Object> getFunctionContext() {
		return functionContext;
	}

}

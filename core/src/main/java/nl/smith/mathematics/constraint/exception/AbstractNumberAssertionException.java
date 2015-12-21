package nl.smith.mathematics.constraint.exception;

import java.util.ArrayList;
import java.util.List;

import nl.smith.mathematics.number.NumberOperations;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class AbstractNumberAssertionException extends Exception {

	private static final long serialVersionUID = 1L;

	public AbstractNumberAssertionException(String errorFormat, NumberOperations<?>... numbers) {
		super(getErrorMessage(errorFormat, numbers));
	}

	/**
	 * Convenience method to create an error message using a log4J messageformat and an array of {@link NumberOperations} arguments
	 * 
	 * @param errorFormat
	 *            The log4J errorformat
	 * @param numbers
	 *            The arguments
	 * @return
	 *         An error message
	 */
	private static String getErrorMessage(String errorFormat, NumberOperations<?>... numbers) {
		String renderedErrorFormat = errorFormat;
		int numberOfArguments = ArrayUtils.isEmpty(numbers) ? 0 : numbers.length;

		if (StringUtils.isEmpty(renderedErrorFormat)) {
			if (numberOfArguments == 0) {
				return "";
			}
			renderedErrorFormat = StringUtils.repeat("%s", "-", numberOfArguments);
		} else {
			renderedErrorFormat = renderedErrorFormat.replaceAll("\\{\\}", "%s");
			if (StringUtils.countMatches(renderedErrorFormat, "%s") != numberOfArguments) {
				return getErrorMessage(null, numbers);
			}
		}

		List<String> arguments = new ArrayList<>();
		if (numbers != null) {
			for (NumberOperations<?> number : numbers) {
				arguments.add(number == null ? null : number.toStringExact());
			}
		}

		return String.format(renderedErrorFormat, arguments.toArray());
	}
}

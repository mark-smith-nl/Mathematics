package nl.smith.mathematics.utility;

import org.apache.commons.lang.StringUtils;

 /** Simple POJO describing whether a structured string is well formed 
  * @author mark
  * */

public class StructuredStringValidationInfo {
	private final String structuredString;

	private final boolean valid;

	private final int position;

	private final String errorMessage;

	public StructuredStringValidationInfo(String structuredString, boolean valid, int position, String errorMessage) {
		super();
		this.structuredString = structuredString;
		this.valid = valid;
		this.position = position;
		this.errorMessage = errorMessage;
	}

	public boolean isValid() {
		return valid;
	}

	public int getPosition() {
		return position;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getStructuredString() {
		return structuredString;
	}

	@Override
	public String toString() {
		String string;
		if (valid) {
			string = String.format("The string '%s' is properly structured", structuredString);
		} else {
			string = String.format("The string '%s' is not properly structured", structuredString);
			string += "\n" + errorMessage;
			string += "\n" + structuredString;
			string += "\n" + StringUtils.repeat(" ", position) + "^";
		}

		return string;
	}

}

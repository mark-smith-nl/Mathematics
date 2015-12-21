package nl.smith.mathematics.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility singleton class to check whether a structured string is well formed
 * 
 * @author mark
 */
public class StructuredStringValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(StructuredStringValidator.class);

	public static final Map<String, String> COMPOUND_EXPRESSSION_AGGREGATION_TOKEN_PAIRS;

	static {
		Map<String, String> identifierPairs = new HashMap<>();

		identifierPairs.put("{", "}");
		identifierPairs.put("(", ")");
		identifierPairs.put("[", "]");

		COMPOUND_EXPRESSSION_AGGREGATION_TOKEN_PAIRS = Collections.unmodifiableMap(identifierPairs);
	}

	/** This class is an utility class and should not be instantiated */
	private StructuredStringValidator() {

	}

	/**
	 * Method to check whether a string is well formed
	 * 
	 * @param structuredString
	 *            The string to be validated
	 * @param identifierPairs
	 *            Map of pairs of identifiers marking the beginning and end of a
	 *            structured part of a string
	 * @return
	 *         Returns true if the string is well formed
	 *         Returns false otherwise
	 */
	public static boolean isValid(String structuredString, Map<String, String> identifierPairs) {
		StructuredStringValidationInfo structuredStringValidationInfo = getStructuredStringValidationInfo(structuredString, identifierPairs);

		return structuredStringValidationInfo.isValid();
	}

	public static StructuredStringValidationInfo getStructuredStringValidationInfo(String structuredString, Map<String, String> identifierPairs) {
		if (StringUtils.isBlank(structuredString)) {
			throw new IllegalArgumentException("No string specified");
		}

		List<String> identifiers = getIdentifiers(identifierPairs);

		List<String> startIdentifiers = new ArrayList<String>(identifierPairs.keySet());

		List<String> startIdentifierStack = new ArrayList<String>();

		Entry<String, Integer> occurence = getNextOccurence(structuredString, 0, identifiers);
		while (occurence != null) {
			String containerIdentifier = occurence.getKey();
			int position = occurence.getValue();

			if (startIdentifiers.contains(containerIdentifier)) {
				startIdentifierStack.add(containerIdentifier);
			} else {
				if (startIdentifierStack.isEmpty()) {
					// Missing opening identifier
					return new StructuredStringValidationInfo(structuredString, false, position, String.format(
							"Missing corresponding container start identifier for end identifier '%s' at position %d", containerIdentifier, position));
				}
				int indexLastcontainerStartIdentifierStackElement = startIdentifierStack.size() - 1;
				String lastcontainerStartIdentifierStackElement = startIdentifierStack.get(indexLastcontainerStartIdentifierStackElement);
				String expectedIdentifier = identifierPairs.get(lastcontainerStartIdentifierStackElement);
				if (!expectedIdentifier.equals(containerIdentifier)) {
					// TODO set position and error message
					// Wrong type of end container identifier
					return new StructuredStringValidationInfo(structuredString, false, position, String.format(
							"Wrong type of container end identifier. Expected '%s' at position %d, encountered '%s'", expectedIdentifier, position, containerIdentifier));
				}
				startIdentifierStack.remove(indexLastcontainerStartIdentifierStackElement);

			}

			occurence = getNextOccurence(structuredString, position + containerIdentifier.length(), identifiers);
		}

		if (startIdentifierStack.isEmpty()) {
			return new StructuredStringValidationInfo(structuredString, true, 0, null);
		}

		// TODO set position and error message
		return new StructuredStringValidationInfo(structuredString, false, 0, null);

	}

	/**
	 * Method to get a merged list of trimmed identifiers marking the beginning and or end of a part of a string
	 * Method validates empty and duplicate identifiers
	 * The elements in both provided lists are trimmed
	 * Method is protected for test purposes
	 * 
	 * @param startIdentifiers
	 *            List of identifier strings marking the beginning of a part of a string
	 *            These strings may not be empty or begin or end with a white space character
	 * @param endIdentifiers
	 *            List of identifier strings marking the end of a part of a string
	 *            These strings may not begin or end with a white space character
	 * @return
	 *         Returns a list of identifiers
	 */
	protected static List<String> getIdentifiers(Map<String, String> identifierPairs) {
		if (MapUtils.isEmpty(identifierPairs)) {
			throw new IllegalArgumentException("No container identifier pairs specified");
		}

		List<String> startIdentifiers = new ArrayList<String>(identifierPairs.keySet());
		List<String> endIdentifiers = new ArrayList<String>(identifierPairs.values());

		checkIdentifiers(startIdentifiers);
		checkIdentifiers(endIdentifiers);

		List<String> identifiers = new ArrayList<String>(startIdentifiers);
		identifiers.addAll(endIdentifiers);

		Set<String> uniqueIdentifier = new HashSet<String>(identifiers);
		if (identifiers.size() != uniqueIdentifier.size()) {
			throw new IllegalArgumentException("Duplicate container identifier(s) encountered");
		}

		return identifiers;
	}

	/**
	 * Method checks if the entries in the string list are not blank and don't begin or start with a white space character
	 * Method is protected for test purposes
	 * 
	 * @param identifiers
	 *            List of identifier strings <b>not empty</b>
	 */
	protected static void checkIdentifiers(List<String> identifiers) {
		for (String identifier : identifiers) {
			if (StringUtils.isBlank(identifier)) {
				String errorMessage = "Illegal blank identifier string";
				LOGGER.error(errorMessage);
				throw new IllegalArgumentException(errorMessage);
			}

			if (!identifier.equals(identifier.trim())) {
				String errorMessage = "Illegal identifier string '{} 'containing leading or trailing whitespace characters";
				LOGGER.error(errorMessage, identifier);
				throw new IllegalArgumentException(String.format(errorMessage.replaceAll("\\{\\}", "%s"), identifier));
			}
		}

		if (identifiers.size() != new HashSet<>(identifiers).size()) {
			String errorMessage = "List of identifiers contains duplicates";
			LOGGER.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
	}

	private static Entry<String, Integer> getNextOccurence(String structuredString, int position, List<String> identifiers) {
		int firstPosition = structuredString.length() + 1;
		String firstIdentifier = null;

		for (String containerIdentifier : identifiers) {
			int indexOf = structuredString.indexOf(containerIdentifier, position);
			if (indexOf != -1 && indexOf < firstPosition) {
				firstPosition = indexOf;
				firstIdentifier = containerIdentifier;
			}
		}

		if (firstIdentifier == null) {
			return null;
		}

		final String containerIdentifier = firstIdentifier;
		final Integer indexOf = firstPosition;

		return new Entry<String, Integer>() {
			@Override
			public Integer setValue(Integer value) {
				return null;
			}

			@Override
			public Integer getValue() {
				return indexOf;
			}

			@Override
			public String getKey() {
				return containerIdentifier;
			}
		};
	}

}

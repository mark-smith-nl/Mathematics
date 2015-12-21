package nl.smith.mathematics.utility;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class StructuredStringValidatorTest {
	// Test resource dependencies
	private static final String HTML_TAGS = "HTMLTags.properties";

	private static final String WELL_FORMED_HTML = "wellFormedHTML.html";

	private static final String NOT_WELL_FORMED_HTML = "notWellFormedHTML.html";

	private static final String WELL_FORMED_COMPOUND_EXPRESSION = "wellFormedCompoudExpressions.txt";

	@Test
	public void checkIdentifiersWithleadingTailingBlanks() {
		String[] illegalIdentifiers = new String[] { null, "\n<b>", "<b>\n", "\t<b>", " <b>", "<b>\t", " <b> ", " <div title=\"This is a div tag>\" " };

		for (String illegalIdentifier : illegalIdentifiers) {
			try {
				StructuredStringValidator.checkIdentifiers(Arrays.asList(new String[] { illegalIdentifier }));
				fail(String.format("Exception expected: The string '%s' should not be a legal identifier string", illegalIdentifier));
			} catch (IllegalArgumentException e) {
				//

			}
		}
	}

	@Test
	public void checkIdentifiersWithDuplicates() {
		List<String> identifiers = Arrays.asList(new String[] { "</a>", "</b>", "</i>", "</div>", "</a>" });

		try {
			StructuredStringValidator.checkIdentifiers(identifiers);
			fail("Exception expected: checkIdentifiers should fail. The list of identifiers contains duplicates");
		} catch (IllegalArgumentException e) {
			//
		}
	}

	@Test
	public void getIdentifiersEmptyMap() {
		try {
			StructuredStringValidator.getIdentifiers(null);
			fail("Exception expected: getIdentifiers should fail. The map of identifier pairs is null");
		} catch (IllegalArgumentException e) {
			//
		}

		try {
			StructuredStringValidator.getIdentifiers(new HashMap<String, String>());
			fail("Exception expected: getIdentifiers should fail. The map of identifier pairs is empty");
		} catch (IllegalArgumentException e) {
			//
		}
	}

	@Test
	public void getIdentifiersWithDuplicates() {
		Map<String, String> identifierPairs = new HashMap<>();
		identifierPairs.put("<a>", "</a>");
		identifierPairs.put("<b>", "</b>");
		identifierPairs.put("<c>", "</c>");
		identifierPairs.put("<d>", "</c>");
		try {
			StructuredStringValidator.getIdentifiers(identifierPairs);
			fail("Exception expected: getIdentifiers should fail. The list of identifiers contains duplicates (/c)");
		} catch (IllegalArgumentException e) {
			//
		}
	}

	@Test
	public void isValid() {
		String structuredString = TestUtility.getStringFromResource(WELL_FORMED_HTML, StructuredStringValidatorTest.class);
		Map<String, String> identifierPairs = getIdentifierPairs();

		assertTrue("The input string should be well formed", StructuredStringValidator.isValid(structuredString, identifierPairs));
	}

	@Test
	public void isNotValid() {
		String structuredString = TestUtility.getStringFromResource(NOT_WELL_FORMED_HTML, StructuredStringValidatorTest.class);
		Map<String, String> identifierPairs = getIdentifierPairs();

		assertFalse("The input string should not be well formed", StructuredStringValidator.isValid(structuredString, identifierPairs));
	}

	@Test
	public void isValidCompoundExpression() {
		String stringFromResource = TestUtility.getStringFromResource(WELL_FORMED_COMPOUND_EXPRESSION, StructuredStringValidatorTest.class);
		String[] compoundExpressions = StringUtils.split(stringFromResource, "\n");
		for (String compoundExpression : compoundExpressions) {
			assertTrue(String.format("The compoundExpression %s should be valid", compoundExpression),
					StructuredStringValidator.isValid(compoundExpression, StructuredStringValidator.COMPOUND_EXPRESSSION_AGGREGATION_TOKEN_PAIRS));
		}
	}

	private static Map<String, String> getIdentifierPairs() {
		Map<String, String> identifierPairs = new HashMap<>();

		Properties properties = TestUtility.getProperties(HTML_TAGS);
		String HTMLTags = (String) properties.get("HTMLTags");
		String[] tags = StringUtils.split(HTMLTags, ",");
		for (String tag : tags) {
			identifierPairs.put(tag, "</" + tag.substring(1));
		}

		return identifierPairs;
	}

}

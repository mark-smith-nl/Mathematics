package nl.smith.mathematics.factory.constant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Class describing the nested structure of the enums defined in {@link ArithmeticComponentRegularExpression}
 * 
 * @author mark
 *
 */
public class ArithmeticComponentStructure {
	private final ArithmeticComponentName arithmeticComponentName;

	/** Index of the group in the corresponding regular expression */
	private final int groupIndex;

	private final List<ArithmeticComponentStructure> arithmeticComponentStructures;

	public ArithmeticComponentStructure(ArithmeticComponentName arithmeticComponentName, int groupIndex) {

		if (arithmeticComponentName == null || groupIndex < 1) {
			// TODO
			throw new IllegalArgumentException("Implement message");
		}
		this.arithmeticComponentName = arithmeticComponentName;
		this.groupIndex = groupIndex;
		arithmeticComponentStructures = new ArrayList<ArithmeticComponentStructure>();
	}

	/** Constructor to create a ArithmeticComponentStructure wrapping the provides list of ArithmeticComponentStructures */
	private ArithmeticComponentStructure(List<ArithmeticComponentStructure> arithmeticComponentStructures) {
		arithmeticComponentName = null;
		groupIndex = -1;
		this.arithmeticComponentStructures = arithmeticComponentStructures;
	}

	public ArithmeticComponentStructure(ArithmeticComponentStructure arithmeticComponentStructure, int offset) {
		this.arithmeticComponentName = arithmeticComponentStructure.getArithmeticComponentName();
		this.groupIndex = arithmeticComponentStructure.getGroupIndex() + offset;
		this.arithmeticComponentStructures = new ArrayList<ArithmeticComponentStructure>();
		List<ArithmeticComponentStructure> arithmeticComponentStructures = arithmeticComponentStructure.getArithmeticComponentStructures();
		for (ArithmeticComponentStructure s : arithmeticComponentStructures) {
			this.arithmeticComponentStructures.add(new ArithmeticComponentStructure(s, offset));
		}
	}

	public void add(ArithmeticComponentStructure arithmeticComponentStructure) {
		if (arithmeticComponentStructure != null) {
			this.arithmeticComponentStructures.add(arithmeticComponentStructure);
		}
	}

	public void add(List<ArithmeticComponentStructure> arithmeticComponentStructures) {
		if (CollectionUtils.isNotEmpty(arithmeticComponentStructures)) {
			this.arithmeticComponentStructures.addAll(arithmeticComponentStructures);
		}
	}

	public ArithmeticComponentName getArithmeticComponentName() {
		return arithmeticComponentName;
	}

	public int getGroupIndex() {
		return groupIndex;
	}

	public List<ArithmeticComponentStructure> getArithmeticComponentStructures() {
		return arithmeticComponentStructures;
	}

	public int getNumberOfElements() {
		int numberOfElements = 1;
		for (ArithmeticComponentStructure arithmeticComponentStructure : arithmeticComponentStructures) {
			numberOfElements += arithmeticComponentStructure.getNumberOfElements();
		}

		return numberOfElements;
	}

	public static int getNumberOfElements(List<ArithmeticComponentStructure> arithmeticComponentStructures) {
		checkNotNull(arithmeticComponentStructures);
		return new ArithmeticComponentStructure(arithmeticComponentStructures).getNumberOfElements() - 1;
	}

	public ArithmeticComponentStructure getStructureByNamedNumberParts(ArithmeticComponentName...
			arithmeticComponentNames) {

		if (arithmeticComponentNames != null && arithmeticComponentNames != null && arithmeticComponentNames.length > 0) {
			for (ArithmeticComponentStructure arithmeticComponentStructure : arithmeticComponentStructures) {
				if (arithmeticComponentStructure.arithmeticComponentName == arithmeticComponentNames[0]) {
					if (arithmeticComponentNames.length == 1) {
						return arithmeticComponentStructure;
					}

					ArithmeticComponentStructure structureByNamedNumberParts = arithmeticComponentStructure
							.getStructureByNamedNumberParts((ArithmeticComponentName[]) ArrayUtils
									.remove(arithmeticComponentNames, 0));
					if (structureByNamedNumberParts != null) {
						return structureByNamedNumberParts;
					}
				}
			}
		}

		return null;
	}

	public static ArithmeticComponentStructure getStructureByNamedNumberParts(List<ArithmeticComponentStructure> arithmeticComponentStructures, ArithmeticComponentName...
			arithmeticComponentNames) {
		checkNotNull(arithmeticComponentStructures);
		return new ArithmeticComponentStructure(arithmeticComponentStructures).getStructureByNamedNumberParts(arithmeticComponentNames);
	}

	public List<ArithmeticComponentStructure> getStructuresByNamedNumberParts(ArithmeticComponentName...
			arithmeticComponentNames) {

		@SuppressWarnings("serial")
		List<ArithmeticComponentStructure> structuresByNamedNumberParts = new ArrayList<ArithmeticComponentStructure>() {

			@Override
			public boolean add(ArithmeticComponentStructure e) {
				if (e != null) {
					return super.add(e);
				}
				return false;
			}

			@Override
			public boolean addAll(Collection<? extends ArithmeticComponentStructure> c) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean addAll(int index, Collection<? extends ArithmeticComponentStructure> c) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void add(int index, ArithmeticComponentStructure element) {
				if (element != null) {
					super.add(index, element);
				}
			}

		};

		if (arithmeticComponentNames != null && arithmeticComponentNames != null && arithmeticComponentNames.length > 0) {
			for (ArithmeticComponentStructure arithmeticComponentStructure : arithmeticComponentStructures) {
				if (arithmeticComponentStructure.arithmeticComponentName == arithmeticComponentNames[0]) {
					if (arithmeticComponentNames.length == 1) {
						structuresByNamedNumberParts.add(arithmeticComponentStructure);
					} else {
						structuresByNamedNumberParts.addAll(arithmeticComponentStructure.getStructuresByNamedNumberParts((ArithmeticComponentName[]) ArrayUtils.remove(
								arithmeticComponentNames, 0)));
					}

				}
			}
		}

		return structuresByNamedNumberParts;
	}

	public static List<ArithmeticComponentStructure> getStructuresByNamedNumberParts(List<ArithmeticComponentStructure> arithmeticComponentStructures, ArithmeticComponentName...
			arithmeticComponentNames) {
		checkNotNull(arithmeticComponentStructures);
		return new ArithmeticComponentStructure(arithmeticComponentStructures).getStructuresByNamedNumberParts(arithmeticComponentNames);
	}

	private static void checkNotNull(List<ArithmeticComponentStructure> arithmeticComponentStructures) {
		if (arithmeticComponentStructures == null) {
			throw new IllegalArgumentException("\nNull or arithmeticComponentStructures is not accepted");
		}
	}

	public static String toString(List<ArithmeticComponentStructure> arithmeticComponentStructures) {
		if (arithmeticComponentStructures == null) {
			// TODO
			throw new IllegalArgumentException("Implement message");
		}
		StringBuffer toString = new StringBuffer();
		for (ArithmeticComponentStructure arithmeticComponentStructure : arithmeticComponentStructures) {
			arithmeticComponentStructure.toString(0, toString);
			toString.append("\n");
		}

		return toString.toString();
	}

	@Override
	public String toString() {
		StringBuffer toString = new StringBuffer();
		toString(0, toString);
		return toString.toString();
	}

	private void toString(int depth, StringBuffer toString) {
		String prefix = StringUtils.repeat(".....", depth);
		toString.append(prefix + groupIndex + ":" + arithmeticComponentName);
		for (ArithmeticComponentStructure arithmeticComponentStructure : arithmeticComponentStructures) {
			toString.append("\n");
			arithmeticComponentStructure.toString(depth + 1, toString);
		}
	}
}

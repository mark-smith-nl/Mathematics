package nl.smith.mathematics.number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RationalNumberMatrix implements Matrix<RationalNumberMatrix, RationalNumber> {

	public enum ORIENTATION_TYPE {
		ROW("row"),
		COLUMN("Column");

		private final String name;

		ORIENTATION_TYPE(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	/**
	 * Row representation of a matrix: a matrix is a list of rows in which each row is a list of values
	 * Immutable
	 */
	private final List<List<RationalNumber>> rows;

	/**
	 * Column representation of a matrix: a matrix is a list of columns in which each column is a list of values
	 * Immutable
	 */
	private final List<List<RationalNumber>> columns;

	private final Dimension dimension;

	public class DimensionImpl implements Dimension {
		private final int numberOfRows;

		private final int numberOfColumns;

		private DimensionImpl(int numberOfRows, int numberOfColumns) {
			this.numberOfRows = numberOfRows;
			this.numberOfColumns = numberOfColumns;
		}

		@Override
		public int getNumberOfRows() {
			return numberOfRows;
		}

		@Override
		public int getNumberOfColumns() {
			return numberOfColumns;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}

			if (this == obj) {
				return true;
			}

			if (obj instanceof Dimension) {
				Dimension other = (Dimension) obj;
				return numberOfRows == other.getNumberOfRows() && numberOfColumns == other.getNumberOfRows();
			}

			return false;
		}

		@Override
		public String toString() {
			return String.format("Dimension (rxc): %dx%d", numberOfRows, numberOfColumns);
		}

	}

	public RationalNumberMatrix(RationalNumber[][] arrayValues) {
		this(getValuesAsList(arrayValues));
	}

	public RationalNumberMatrix(List<List<RationalNumber>> listValues) {
		this(listValues, ORIENTATION_TYPE.ROW);
	}

	public RationalNumberMatrix(List<List<RationalNumber>> listValues, ORIENTATION_TYPE oType) {
		if (listValues == null || listValues.size() == 0) {
			throw new IllegalArgumentException("\nNo elements specified");
		}

		int size = 0;
		for (int i = 0; i < listValues.size(); i++) {
			List<RationalNumber> list = listValues.get(i);
			if (list == null || list.size() == 0) {
				throw new IllegalArgumentException(String.format("\nNo values specified for %s: ", i, oType.getName()));
			}

			if (size == 0) {
				size = list.size();
			} else {
				if (list.size() != size) {
					throw new IllegalArgumentException(String.format("\nWrong length of %s[%d].\nActual: %d.\nExpected: %d", i, oType.getName(), list.size(), size));
				}
			}
		}

		if (oType == ORIENTATION_TYPE.ROW) {
			rows = Collections.unmodifiableList(listValues);
			dimension = new DimensionImpl(rows.size(), rows.get(0).size());
			columns = Collections.unmodifiableList(transpose(rows));
		} else {
			columns = Collections.unmodifiableList(listValues);
			dimension = new DimensionImpl(columns.get(0).size(), columns.size());
			rows = transpose(columns);
		}
	}

	private static List<List<RationalNumber>> getValuesAsList(RationalNumber[][] arrayValues) {
		if (arrayValues == null || arrayValues.length == 0) {
			throw new IllegalArgumentException("\nNo values specified");
		}

		List<List<RationalNumber>> valuesAsList = new ArrayList<List<RationalNumber>>();
		for (RationalNumber[] list : arrayValues) {
			if (list == null) {
				valuesAsList.add(new ArrayList<RationalNumber>());
			} else {
				valuesAsList.add(Arrays.asList(list));
			}
		}

		return valuesAsList;
	}

	private static List<List<RationalNumber>> transpose(List<List<RationalNumber>> elements) {
		List<List<RationalNumber>> list = new ArrayList<List<RationalNumber>>();
		for (int i = 0; i < elements.get(0).size(); i++) {
			List<RationalNumber> l = new ArrayList<RationalNumber>();
			for (int j = 0; j < elements.size(); j++) {
				l.add(elements.get(j).get(i));
			}
			list.add(l);
		}

		return list;
	}

	@Override
	public Dimension getDimension() {
		return dimension;
	}

	@Override
	public List<List<RationalNumber>> getRows() {
		return rows;
	}

	@Override
	public List<RationalNumber> getRow(int index) {
		validateLegalRowIndex(index);

		return rows.get(index);
	}

	@Override
	public RationalNumberMatrix getWithoutRow(int index) {
		validateLegalRowIndex(index);

		List<List<RationalNumber>> rows = new ArrayList<>();
		for (int i = 0; i < this.rows.size(); i++) {
			if (i != index) {
				rows.add(this.rows.get(i));
			}
		}

		return new RationalNumberMatrix(rows, ORIENTATION_TYPE.ROW);
	}

	private void validateLegalRowIndex(int index) {
		if (index < 0) {
			throw new ArithmeticException();
		}

		if (index >= rows.size()) {
			throw new ArithmeticException();
		}
	}

	@Override
	public List<List<RationalNumber>> getColumns() {
		return columns;
	}

	@Override
	public List<RationalNumber> getColumn(int index) {
		validateLegalColumnIndex(index);

		return columns.get(index);
	}

	@Override
	public RationalNumberMatrix getWithoutColumn(int index) {
		validateLegalColumnIndex(index);

		List<List<RationalNumber>> columns = new ArrayList<>();
		for (int i = 0; i < this.columns.size(); i++) {
			if (i != index) {
				columns.add(this.columns.get(i));
			}
		}

		return new RationalNumberMatrix(columns, ORIENTATION_TYPE.COLUMN);
	}

	private void validateLegalColumnIndex(int index) {
		if (index < 0) {
			throw new ArithmeticException();
		}

		if (index >= columns.size()) {
			throw new ArithmeticException();
		}
	}

	@Override
	public RationalNumberMatrix add(RationalNumberMatrix augend) {
		if (augend == null) {
			throw new ArithmeticException("\nNo augend specified");
		}

		if (!dimension.equals(augend.getDimension())) {
			throw new ArithmeticException(String.format("\nAugend has wrong dimension\nExpected: %s\nActual: %s", dimension, augend.getDimension()));
		}

		int numberOfRows = getDimension().getNumberOfRows();
		int numberOfColumns = getDimension().getNumberOfColumns();
		RationalNumber[][] arrayValues = new RationalNumber[numberOfRows][numberOfColumns];

		for (int i = 0; i < numberOfRows; i++) {
			List<RationalNumber> row = rows.get(i);
			List<RationalNumber> otherRow = augend.getRows().get(i);
			for (int j = 0; j < numberOfColumns; j++) {
				arrayValues[i][j] = row.get(j).add(otherRow.get(j));
			}
		}
		return new RationalNumberMatrix(arrayValues);
	}

	@Override
	public RationalNumberMatrix subtract(RationalNumberMatrix subtrahend) {
		if (subtrahend == null) {
			throw new ArithmeticException("\nNo subtrahend specified");
		}

		return add(subtrahend.negate());
	}

	@Override
	public RationalNumberMatrix multiply(RationalNumber multiplicand) {
		if (multiplicand == null) {
			throw new ArithmeticException("\nNo multiplicand specified");
		}

		List<List<RationalNumber>> rows = new ArrayList<>();
		for (List<RationalNumber> row : rows) {
			List<RationalNumber> column = new ArrayList<RationalNumber>();
			for (RationalNumber element : row) {
				column.add(element.multiply(multiplicand));
			}
			rows.add(column);
		}

		return new RationalNumberMatrix(rows);
	}

	@Override
	public RationalNumberMatrix multiply(RationalNumberMatrix multiplicand) {
		if (multiplicand == null) {
			throw new ArithmeticException("\nNo multiplicand specified");
		}

		if (dimension.getNumberOfColumns() != multiplicand.dimension.getNumberOfRows()) {
			throw new ArithmeticException(String.format("\nMultiplicand has wrong number of rows.\nActual: %d.\nExpected: %d", multiplicand.dimension.getNumberOfRows(),
					dimension.getNumberOfColumns()));
		}

		List<List<RationalNumber>> newRows = new ArrayList<>();
		for (List<RationalNumber> row : rows) {
			List<RationalNumber> newRow = new ArrayList<>();
			for (List<RationalNumber> column : multiplicand.columns) {
				RationalNumber sum = new RationalNumber(0);
				for (int index = 0; index < dimension.getNumberOfColumns(); index++) {
					sum = sum.add(row.get(index).multiply(column.get(index)));
				}
				newRow.add(sum);
			}
			newRows.add(newRow);
		}

		return new RationalNumberMatrix(newRows);
	}

	@Override
	public RationalNumberMatrix negate() {
		return multiply(new RationalNumber(-1));
	}

	@Override
	public RationalNumberMatrix transpose() {
		return new RationalNumberMatrix(transpose(rows));
	}

	@Override
	public boolean isSquare() {
		return dimension.getNumberOfRows() == dimension.getNumberOfColumns();
	}

	@Override
	public RationalNumber getDeterminant() {
		if (!isSquare()) {
			throw new ArithmeticException(String.format("\nThe determinant is not defined for a matrix of this dimension.\n%s", dimension));
		}

		return getDet();
	}

	private RationalNumber getDet() {
		if (this.dimension.getNumberOfColumns() == 1) {
			return rows.get(0).get(0);
		}

		RationalNumberMatrix matrixWithoutRow = this.getWithoutRow(0);
		RationalNumber determinant = new RationalNumber(0);

		for (int i = 0; i < rows.get(0).size(); i++) {
			RationalNumberMatrix matrixWithoutRowAndColumn = matrixWithoutRow.getWithoutColumn(i);
			RationalNumber multiplier = rows.get(0).get(i);
			RationalNumber subDeterminant = matrixWithoutRowAndColumn.getDeterminant();
			if (i % 2 == 0) {
				determinant = determinant.add(multiplier.multiply(subDeterminant));
			} else {
				determinant = determinant.subtract(multiplier.multiply(subDeterminant));
			}
		}

		return determinant;
	}

	@Override
	public String toString() {
		return "Matrix: " + dimension;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != RationalNumberMatrix.class) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		RationalNumberMatrix otherRationalNumberMatrix = (RationalNumberMatrix) obj;
		if (!dimension.equals(otherRationalNumberMatrix.dimension)) {
			return false;
		}

		for (int rowIndex = 0; rowIndex < dimension.getNumberOfRows(); rowIndex++) {
			List<RationalNumber> row = getRow(rowIndex);
			List<RationalNumber> otherRow = otherRationalNumberMatrix.getRow(rowIndex);
			for (int columnIndex = 0; columnIndex < dimension.getNumberOfColumns(); columnIndex++) {
				if (!row.get(columnIndex).equals(otherRow.get(columnIndex))) {
					return false;
				}
			}
		}

		return true;

	}

}

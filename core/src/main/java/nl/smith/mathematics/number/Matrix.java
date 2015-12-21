package nl.smith.mathematics.number;

import java.util.List;

public interface Matrix<M extends Matrix<?, ?>, N extends NumberOperations<?>> {
	Dimension getDimension();

	List<List<N>> getRows();

	/**
	 * Method to get the Nth row from a matrix
	 * Note: zero based indexing
	 * 
	 * @param index
	 * @return
	 */
	List<N> getRow(int index);

	/**
	 * Method to get a new matrix without the Nth row
	 * Note: zero based indexing
	 * 
	 * @param index
	 * @return
	 */
	M getWithoutRow(int index);

	List<List<N>> getColumns();

	/**
	 * Method to get the Nth column from a matrix
	 * Note: zero based indexing
	 * 
	 * @param index
	 * @return
	 */
	List<N> getColumn(int index);

	/**
	 * Method to get a new matrix without the Nth column
	 * Note: zero based indexing
	 * 
	 * @param index
	 * @return
	 */
	M getWithoutColumn(int index);

	M add(M augend);

	M subtract(M subtrahend);

	M multiply(N multiplicand);

	M multiply(M multiplicand);

	M negate();

	M transpose();

	boolean isSquare();

	N getDeterminant();

}

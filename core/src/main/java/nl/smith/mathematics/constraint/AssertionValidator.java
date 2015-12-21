package nl.smith.mathematics.constraint;


public interface AssertionValidator<A extends Object> {
	IllegalArgumentException getWrappedAssertionException(String errorFormat, @SuppressWarnings("unchecked") A... argument);

}

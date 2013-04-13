
package alg;

/*
 * Represents an algebraic expression.
 */
public abstract class AlgebraicExpression {

	@Override
	public abstract String toString();

	public abstract String representation();

	/**
	 * Creates and returns a simplified version of this expression.
	 *
	 * @return a simplified version of this expression
	 */
	public abstract AlgebraicExpression simplify();

	/**
	 * Finds the negative of this expression. Subclasses may override this
	 * method. The return type must be in simplest form -- that is, if
	 * <code>simplify()</code> is invoked on it, it should return a copy of
	 * itself.
	 *
	 * @return the negative of this expression
	 */
	public AlgebraicExpression negate() {

		AlgebraicSum res = new AlgebraicSum();
		res.appendTerm(Sign.NEGATIVE, this);

		return res;
	}
}

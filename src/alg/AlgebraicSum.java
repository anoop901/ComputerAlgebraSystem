
package alg;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the sum or difference of an arbitrary number of algebraic terms.
 */
public class AlgebraicSum extends AlgebraicExpression {

	private ArrayList<AlgebraicTerm> terms;

	public AlgebraicSum() {
		terms = new ArrayList<AlgebraicTerm>();
	}

	public void appendTerm(Sign s, AlgebraicExpression e) {
		if (e != this)
			terms.add(new AlgebraicTerm(s, e));
		else
			throw new IllegalArgumentException(
					"A sum cannot be an addend in itself");
	}

	@Override
	public String toString() {

		if (terms.isEmpty())
			return "0";

		String str = "";

		for (int i = 0; i < terms.size(); i++) {
			AlgebraicTerm term = terms.get(i);
			if (i > 0 || term.sign == Sign.NEGATIVE)
				str += term.sign.toString();

			// should the term be surrounded by parentheses?
			boolean needsParentheses = false;

			if (term.expr instanceof RealConstant) {
				if (term.sign == Sign.POSITIVE) {
					needsParentheses = i > 0 && ((RealConstant) term.expr)
							.isNegative();
				} else {
					needsParentheses = ((RealConstant) term.expr).isNegative();
				}
			} else if (term.expr instanceof AlgebraicSum) {
				needsParentheses = true;
			}

			if (needsParentheses)
				str += "(";
			str += term.expr.toString();
			if (needsParentheses)
				str += ")";
		}

		return str;
	}

	@Override
	public String representation() {

		String str = "sum(";

		for (AlgebraicTerm t : terms) {
			str += t.representation();
		}

		str += ")";

		return str;
	}

	@Override
	public AlgebraicExpression simplify() {

		if (terms.isEmpty()) {

			// the sum is zero
			return new IntegerConstant(0);

		} else if (terms.size() == 1) { // there is one term

			// return an AlgebraicExpression that represents the only term's 
			//		sign and expression
			AlgebraicTerm term = terms.get(0);
			if (term.sign == Sign.POSITIVE) {
				return term.expr.simplify();
			} else {
				return term.expr.simplify().negate();
			}

		} else { // there are multiple terms

			// TODO: update as functionality is added
			// so far, supports Variables, RealConstants, AlgebraicSums
			// does not support combining like terms


			AlgebraicSum innerTermsSimplified = new AlgebraicSum();
			for (AlgebraicTerm termInOuterSum : terms) {
				AlgebraicExpression simplified = termInOuterSum.expr.simplify();
				if (simplified instanceof AlgebraicSum) {
					for (AlgebraicTerm termInInnerSum : ((AlgebraicSum) simplified).terms) {
						Sign combined = Sign.multiply(termInOuterSum.sign, termInInnerSum.sign);
						innerTermsSimplified.appendTerm(combined, termInInnerSum.expr);
					}
				} else {
					innerTermsSimplified.appendTerm(termInOuterSum.sign,
							simplified);
				}
			}
			System.out.println("InnerTermsSimplified:" + innerTermsSimplified);

			int intConstSum = 0;		// accumulates the sum of all IntegerConstants
			double realConstSum = 0.0;	// accumulates the sum of all other RealConstants

			AlgebraicSum expr2 = new AlgebraicSum();
			for (AlgebraicTerm t : innerTermsSimplified.terms) {
				// iterate through each of the terms, and process them depending
				//		on what type they are


				if (t.expr instanceof RealConstant) {

					// if this term contains a RealConstant, do not add it yet
					//		because it will be combined with other RealConstants
					//		and added at the end of the sum. For now, just
					//		update the appropriate info about what to add at the
					//		end
					if (t.expr instanceof IntegerConstant) {
						if (t.sign == Sign.POSITIVE)
							intConstSum += ((IntegerConstant) t.expr)
									.getIntValue();
						else
							intConstSum -= ((IntegerConstant) t.expr)
									.getIntValue();
					} else {
						if (t.sign == Sign.POSITIVE)
							realConstSum += ((RealConstant) t.expr).getValue();
						else
							realConstSum -= ((RealConstant) t.expr).getValue();
					}
				} else if (t.expr instanceof Variable) {

					// add the variable to the result immediately
					expr2.appendTerm(t.sign, t.expr);

					// TODO: add support for combining like terms and cancelling

				} else if (t.expr instanceof AlgebraicSum) {
					// TODO: this code only works if t.expr simplifies to an
					//		instance of AlgebraicSum. fix it.
					/*ArrayList<AlgebraicTerm> newTerms;
					 if (t.sign == Sign.POSITIVE) {
					 newTerms = ((AlgebraicSum) t.expr.simplify()).terms;
					 } else {
					 newTerms = ((AlgebraicSum) t.expr.simplify()).negate().terms;
					 }

					 for (AlgebraicTerm t2 : newTerms) {
					 // TODO: fix repeated code (maybe)
					 if (t2.expr instanceof RealConstant) {
					 if (t2.expr instanceof IntegerConstant) {
					 if (t2.sign == Sign.POSITIVE)
					 intConstSum += ((IntegerConstant) t2.expr).getIntValue();
					 else
					 intConstSum -= ((IntegerConstant) t2.expr).getIntValue();
					 } else {
					 if (t2.sign == Sign.POSITIVE)
					 realConstSum += ((RealConstant) t2.expr).getValue();
					 else
					 realConstSum -= ((RealConstant) t2.expr).getValue();
					 }
					 } else
					 expr2.appendTerm(t2.sign, t2.expr);
					 }*/
				} else {
					throw new UnsupportedOperationException("A sum containing "
							+ "this type of expression cannot be simplified.");

				}
			}

			if (realConstSum == 0.0) {
				if (intConstSum > 0)
					expr2.appendTerm(Sign.POSITIVE, new IntegerConstant(
							intConstSum));
				else if (intConstSum < 0)
					expr2.appendTerm(Sign.NEGATIVE, new IntegerConstant(
							-intConstSum));
			} else if (realConstSum + intConstSum > 0) {
				expr2.appendTerm(Sign.POSITIVE, new RealConstant(
						realConstSum + intConstSum).simplify());
			} else if (realConstSum + intConstSum < 0) {
				expr2.appendTerm(Sign.NEGATIVE, new RealConstant(
						-realConstSum - intConstSum).simplify());
			}

			if (expr2.terms.size() < 2) {
				return expr2.simplify();
			} else {
				return expr2;
			}
		}
	}

	@Override
	public AlgebraicSum negate() {

		AlgebraicSum res = new AlgebraicSum();

		for (AlgebraicTerm t : terms) {
			res.appendTerm(t.sign.negate(), t.expr);
		}

		return res;
	}

	private static class AlgebraicTerm {

		private Sign sign;
		private AlgebraicExpression expr;

		public AlgebraicTerm(Sign s, AlgebraicExpression e) {
			sign = s;
			expr = e;
		}

		@Override
		public String toString() {
			if (sign == Sign.POSITIVE) {
				return expr.toString();
			} else {
				return expr.negate().toString();
			}
		}

		public String representation() {
			return "[" + sign.toString() + ", " + expr.representation() + "]";
		}
	}
}


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

		// for now, an AlgebraicSum is "simplified" iff
		//  -- if it has many terms, it has only one RealConstant term, at the end
		//  -- if it has one term, its sign is negative and its expression is a Variable
		//  -- it has at least one term

		if (terms.isEmpty()) {

			// the sum is zero
			return new IntegerConstant(0);

		} else if (terms.size() == 1) {

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
			
			// represents an AlgebraicSum which is a copy of this, except that the
			//		expression of each term is simplified and any AlgebraicSums
			//		within this AlgebraicSum have been simplified and expanded
			AlgebraicSum innerTermsSimplified = new AlgebraicSum();
			
			for (AlgebraicTerm termInOuterSum : terms) {
				AlgebraicExpression simplified = termInOuterSum.expr.simplify();
				if (simplified instanceof AlgebraicSum) { // sum within a sum
					// distribute the sign of this term to each term in the sum
					for (AlgebraicTerm termInInnerSum : ((AlgebraicSum) simplified).terms) {
						Sign combined = Sign.multiply(termInOuterSum.sign,
								termInInnerSum.sign);
						innerTermsSimplified.appendTerm(combined,
								termInInnerSum.expr);
					}
				} else {
					innerTermsSimplified.appendTerm(termInOuterSum.sign,
							simplified);
				}
			}

			int intConstSum = 0;		// accumulates the sum of all IntegerConstants
			double realConstSum = 0.0;	// accumulates the sum of all other RealConstants

			AlgebraicSum expr2 = new AlgebraicSum();
			for (AlgebraicTerm term : innerTermsSimplified.terms) {
				// iterate through each of the terms, and process them depending
				//		on what type they are


				if (term.expr instanceof RealConstant) {

					// if this term contains a RealConstant, do not add it yet
					//		because it will be combined with other RealConstants
					//		and added at the end of the sum. For now, just
					//		update the appropriate info about what to add at the
					//		end
					if (term.expr instanceof IntegerConstant) {
						if (term.sign == Sign.POSITIVE)
							intConstSum += ((IntegerConstant) term.expr)
									.getIntValue();
						else
							intConstSum -= ((IntegerConstant) term.expr)
									.getIntValue();
					} else {
						if (term.sign == Sign.POSITIVE)
							realConstSum += ((RealConstant) term.expr).getValue();
						else
							realConstSum -= ((RealConstant) term.expr).getValue();
					}
				} else if (term.expr instanceof Variable) {

					// add the variable to the result immediately
					expr2.appendTerm(term.sign, term.expr);

					// TODO: add support for combining like terms and cancelling

				} else if (term.expr instanceof AlgebraicSum) {
					throw new IllegalStateException("There was some kind of logic error in the code.");
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
	public AlgebraicExpression negate() {

		AlgebraicSum res = new AlgebraicSum();

		for (AlgebraicTerm t : terms) {
			res.appendTerm(t.sign.negate(), t.expr);
		}
		
		if (res.terms.size() == 1)
			return res.simplify();
		else 
			return res;
	}

	private static class AlgebraicTerm {

		private Sign sign;
		private AlgebraicExpression expr;

		public AlgebraicTerm(Sign s, AlgebraicExpression e) {
			sign = s;
			expr = e;
		}
/*
		@Override
		public String toString() {
			if (sign == Sign.POSITIVE) {
				return expr.toString();
			} else {
				return expr.negate().toString();
			}
		}
*/
		public String representation() {
			return "[" + sign.toString() + ", " + expr.representation() + "]";
		}
	}
}

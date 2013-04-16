
package alg;

import java.util.ArrayList;


public class AlgebraicProduct extends AlgebraicExpression {
	
	ArrayList<AlgebraicExpression> factors;
	
	public AlgebraicProduct() {
		factors = new ArrayList<AlgebraicExpression>();
	}
	
	public void appendFactor(AlgebraicExpression e) {
		factors.add(e);
	}

	@Override
	public String toString() {
		
		// This works, but the notation it returns has too many parentheses.
		// TODO: Make it better e.g. (3)(x) should show as 3x
		
		if (factors.isEmpty()) {
			return "1";
		}
		
		String str = "";
		
		for (int i = 0; i < factors.size(); i++) {
			AlgebraicExpression fac = factors.get(i);
			str += "(" + fac.toString() + ")";
		}
		
		return str;
	}

	@Override
	public String representation() {
		
		ArrayList<String> facReps = new ArrayList<String>();
		
		for (AlgebraicExpression ae : factors) {
			facReps.add(ae.representation());
		}
		
		return "product" + facReps.toString();
	}

	@Override
	public AlgebraicExpression simplify() {
		throw new UnsupportedOperationException("Not supported yet."); // TODO: implement this
	}
}

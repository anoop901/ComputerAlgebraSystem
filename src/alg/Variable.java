
package alg;


public class Variable extends AlgebraicExpression {
	
	private String varname;
	private String subscript;
	
	public Variable(String vn) {
		varname = vn;
		subscript = null;
	}
	
	public Variable(String vn, String sub) {
		varname = vn;
		subscript = sub;
	}
	
	@Override
	public String toString() {
		if (subscript == null)
			return varname;
		else
			return varname + "_" + subscript;
	}
	
	public String representation() {
		return "var(" + toString() + ")";
	}

	@Override
	public AlgebraicExpression simplify() {
		return this;
	}
}

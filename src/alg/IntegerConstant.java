
package alg;

public class IntegerConstant extends RealConstant {
	
	private int value;
	
	public IntegerConstant(int val) {
		super(val);
		value = val;
	}
	
	public int getIntValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}
	
	@Override
	public String representation() {
		return "int(" + toString() + ")";
	}
	
	@Override
	public AlgebraicExpression negate() {
		return new IntegerConstant(-value);
	}
}

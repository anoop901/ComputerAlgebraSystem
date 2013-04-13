
package alg;

public class RealConstant extends AlgebraicExpression {
	
	private double value;
	
	public RealConstant(double val) {
		value = val;
	}
	
	public double getValue() {
		return value;
	}
	
	public boolean isNegative() {
		return value < 0;
	}

	@Override
	public AlgebraicExpression simplify() {
		if ((double) (int) value == value) {
			return new IntegerConstant((int) value);
		} else
			return new RealConstant(value);
	}
	
	@Override
	public String toString() {
		return Double.toString(value);
	}
	
	@Override
	public AlgebraicExpression negate() {
		return new RealConstant(-value);
	}
	
	public String representation() {
		return "real(" + toString() + ")";
	}
}


package alg;

public enum Sign {

	POSITIVE,
	NEGATIVE;

	@Override
	public String toString() {
		if (this == POSITIVE)
			return "+";
		else
			return "-";
	}

	public Sign negate() {
		if (this == POSITIVE)
			return NEGATIVE;
		else
			return POSITIVE;
	}
	
	public static Sign multiply(Sign s1, Sign s2) {
		return (s1 == POSITIVE) ? (s2) : (s2.negate());
	}
}

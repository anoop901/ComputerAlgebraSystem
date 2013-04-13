

import alg.*;


public class ComputerAlgebraSystem {

    public static void main(String[] args) {
		
		Variable varX = new Variable("x");
		//Variable varY = new Variable("y");
        
		AlgebraicSum sum1 = new AlgebraicSum();
		AlgebraicSum sum2 = new AlgebraicSum();
		
		sum2.appendTerm(Sign.POSITIVE, varX);
		sum2.appendTerm(Sign.NEGATIVE, new IntegerConstant(4));
		
		sum1.appendTerm(Sign.POSITIVE, new IntegerConstant(3));
		sum1.appendTerm(Sign.NEGATIVE, sum2);
		sum1.appendTerm(Sign.POSITIVE, new IntegerConstant(4));
		
		System.out.println(sum1);
		System.out.println(sum1.simplify());
		System.out.println(sum1.representation());
		System.out.println(sum1.simplify().representation());
		System.out.println("YASH IS GREAT");
		
    }

}

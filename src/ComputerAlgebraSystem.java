

import alg.*;


public class ComputerAlgebraSystem {

    public static void main(String[] args) {
		
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
        
		AlgebraicSum sum1 = new AlgebraicSum();
		AlgebraicSum sum2 = new AlgebraicSum();
		AlgebraicSum sum3 = new AlgebraicSum();
		
		sum3.appendTerm(Sign.POSITIVE, new IntegerConstant(849));
		sum3.appendTerm(Sign.NEGATIVE, varY);
		sum3.appendTerm(Sign.NEGATIVE, new IntegerConstant(258));
		
		sum2.appendTerm(Sign.NEGATIVE, varX);
		sum2.appendTerm(Sign.POSITIVE, new IntegerConstant(189));
		sum2.appendTerm(Sign.POSITIVE, sum3);
		
		sum1.appendTerm(Sign.POSITIVE, new IntegerConstant(570));
		sum1.appendTerm(Sign.NEGATIVE, sum2);
		
		AlgebraicExpression simplified = sum1.simplify();
		
		System.out.println(sum1);
		System.out.println(simplified);
    }

}

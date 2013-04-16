

import alg.*;


public class ComputerAlgebraSystem {

    public static void main(String[] args) {
		
		Variable varX = new Variable("x");
		Variable varY = new Variable("y");
        
		AlgebraicSum sum1 = new AlgebraicSum();
		AlgebraicProduct prod = new AlgebraicProduct();
		
		sum1.appendTerm(Sign.POSITIVE, new IntegerConstant(3));
		sum1.appendTerm(Sign.NEGATIVE, varY);
		sum1.appendTerm(Sign.NEGATIVE, new IntegerConstant(1));
		sum1.appendTerm(Sign.POSITIVE, new IntegerConstant(7));
		
		prod.appendFactor(varX);
		prod.appendFactor(new IntegerConstant(3));
		prod.appendFactor(sum1);
		
		//AlgebraicExpression simplified = prod.simplify();
		
		System.out.println(prod);
		System.out.println(prod.representation());
		//System.out.println(simplified);
    }

}

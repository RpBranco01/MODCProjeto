package calculator;

public class Formula {
    private int firstOperator;
    private int secondOperator;
    private String operator;

    public Formula(String formula) {
        String[] lineArray = formula.split(" ");
        firstOperator = Integer.parseInt(lineArray[0]);
        secondOperator = Integer.parseInt(lineArray[2]);
        operator = lineArray[1];
    }

    public int getFirstOperator(){
        return firstOperator;
    }

    public int getSecondOperator(){
        return secondOperator;
    }

    public String getOperator(){
        return operator;
    }
    
    public String makeOperation() {
		String result = "";
		int value;
		switch (operator) {
			case "+":
				value = firstOperator + secondOperator;
				result = formatOperation(firstOperator, secondOperator, value, operator);
				break;
			case "-":
				value = firstOperator - secondOperator;
				result = formatOperation(firstOperator, secondOperator, value, operator);
				break;
			case "/":
				value = firstOperator / secondOperator;
				result = formatOperation(firstOperator, secondOperator, value, operator);
				break;
			case "*":
				value = firstOperator * secondOperator;
				result = formatOperation(firstOperator, secondOperator, value, operator);
				break;
			default:
				break;
		}
		return result;
	}
    
    public String formatOperation(int firstOperator, int secondOperator, int value, String operator){
		return String.valueOf(firstOperator) + operator + String.valueOf(secondOperator) + "=" + String.valueOf(value); 
	}
}

package xxl.storage.Content.Operations.BinaryOperations;

import java.util.Map;
import xxl.storage.Cell;
import xxl.DesignPatterns.Visitor.Visitor;

/*
 * Class that represents a division.
 */
public class Div extends BinaryOperations {

    public Div(String address, String input, Map<String,Cell> cells) {
        setArguments(address,input,cells);
    }

    /**
     * Returns the value of the division.
     * @param cells the cells of the spreadsheet.
     * @return the value of the division.
     */
    @Override
    public String getValue(Map<String,Cell> cells) {
        resetInvalidoperation();
        setArgumentsInt(cells);
     
        if (getInvalidOperation() != null ) { return getInvalidOperation(); }      // if the division is invalid, return #VALUE
        else if (getResult_value_2() == 0) { // if the second argument is 0, return #VALUE
            setInvalidOperation();
            return getInvalidOperation();
        }
        else {
            return String.valueOf(getResult_value_1() / getResult_value_2());
        }
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitDiv(this);
    }
}
package xxl.storage.Content.Operations.BinaryOperations;

import java.util.Map;

import xxl.DesignPatterns.Visitor.SearchValue;
import xxl.DesignPatterns.Visitor.Visitor;
import xxl.storage.Cell;
import xxl.storage.Content.Operations.Operations;

/**
 * Class that represents a binary operation.
 */
public abstract class BinaryOperations extends Operations {

    private String _argument1_int = null;
    private String _argument2_int = null;

    private Cell _argument1_cell = null;
    private Cell _argument2_cell = null;

    private Integer result_value_1 ;
    private Integer result_value_2 ;

    private String getArgument1_int() { return _argument1_int; }
    private String getArgument2_int() { return _argument2_int; }

    private Cell getArgument1_cell() { return _argument1_cell; }
    private Cell getArgument2_cell() { return _argument2_cell; }

    public int getResult_value_1() { return result_value_1; }
    public int getResult_value_2() { return result_value_2; }

    
    /**
     * Sets the arguments of the binary operation.
     * @param content
     */
    public void setArguments(String address ,String content, Map<String,Cell> cells){
        int first_index = content.indexOf("(");
        int last_index = content.lastIndexOf(")");
        String[] arguments = content.substring(first_index+1,last_index).split(",");  // get the arguments of the binary operation
        if (arguments[0].contains(";")){                                // if the first argument is a cell reference
            if( getArgument1_cell() != null ) {                         // if the cell is already an observer of the cell reference, remove it
                getArgument1_cell().removeObserver(cells.get(address));  
            }
            _argument1_cell = cells.get(arguments[0]);
            _argument1_cell.registerObserver(cells.get(address));       // add the cell as an observer of the cell reference
        }
        else {
            _argument1_int = arguments[0];                             // if the first argument is an int
        }
        if (arguments[1].contains(";")){                    // if the second argument is a cell reference
            if( getArgument2_cell() != null ) {             // if the cell is already an observer of the cell reference, remove it
                getArgument2_cell().removeObserver(cells.get(address));
            }
            _argument2_cell = cells.get(arguments[1]);      
            _argument2_cell.registerObserver(cells.get(address));      // add the cell as an observer of the cell reference
        }
    
        else {
            _argument2_int = arguments[1];                        // if the second argument is an int
        }
    }

    /**
     * Sets the arguments of the binary operation in int.
     * @param cells the cells of the spreadsheet.
     * If occurs an error, the operation is invalid and set the invalid operation.
     */
    public void setArgumentsInt(Map<String,Cell> cells){ 

        Visitor visitor = new SearchValue(cells); // visitor that searches for the value of the cell reference

        if (getArgument1_cell() != null) {
            try{
                result_value_1 = Integer.parseInt(getArgument1_cell().getContent().accept(visitor));  // get the value of the cell reference
            }
            catch (NumberFormatException | NullPointerException e){ 
                setInvalidOperation();                                            // if the cell reference is invalid, set the invalid operation
            }
        }
        else {
            try{
                result_value_1 = Integer.parseInt(getArgument1_int());          // try convert the first argument to int
            }
            catch (NumberFormatException | NullPointerException e){
                setInvalidOperation();                                  // if the first argument is invalid, set the invalid operation
            }
        }
        if (getArgument2_cell() != null) {
            try{
                result_value_2 = Integer.parseInt(getArgument2_cell().getContent().accept(visitor));    // get the value of the cell reference  
            }
            catch (NumberFormatException | NullPointerException e){
                setInvalidOperation();                                         // if the cell reference is invalid, set the invalid operation
            }
        }
        else {
            try{
                result_value_2 = Integer.parseInt(getArgument2_int());         // try convert the second argument to int
            }
            catch (NumberFormatException | NullPointerException e){
                setInvalidOperation();                              // if the second argument is invalid, set the invalid operation
            }
        }
    }
}
 
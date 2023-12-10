package xxl.storage.Content.Operations.RangeOperations;

import java.util.List;
import java.util.Map;
import xxl.storage.Cell;
import xxl.storage.Storage;
import xxl.Spreadsheet;
import xxl.DesignPatterns.Visitor.SearchValue;
import xxl.DesignPatterns.Visitor.Visitor;
import xxl.storage.Content.Operations.Operations;

public abstract class RangeOperations extends Operations {

    private String[] _addressList = null;
    private String _invalidRangeArguments = null;
    private Cell[] _referenceList = null;

    public Cell[] getReferenceList() { return _referenceList; }
    public String[] getAddressList() { return _addressList; }
    public String getInvalidRangeArguments() { return _invalidRangeArguments; }
    public void setInvalidRangeArguments() { _invalidRangeArguments = "#VALUE"; }

    /**
     * Sets the addresses of the range operation.
     * @param address
     * @param contentSpecification
     * @param cells
     * @param spreadsheet
     */
    public void setAddresses(String address, String contentSpecification, Map<String,Cell> cells, Spreadsheet spreadsheet) {
        // check if the range is valid
        String[] arguments = contentSpecification.split("[(;:)]+");
        if (!arguments[1].equals(arguments[3]) && !arguments[2].equals(arguments[4])) {
            setInvalidRangeArguments();
        }
        Storage storage = spreadsheet.getStorage();
        if (Integer.parseInt(arguments[1]) > storage.getLines() || Integer.parseInt(arguments[3]) > storage.getLines() ||
        Integer.parseInt(arguments[2]) > storage.getColumns() || Integer.parseInt(arguments[4]) > storage.getColumns()) {
            setInvalidRangeArguments();
        }
        else {
            // create the address list
            arguments = contentSpecification.split("[()]+");
            _addressList = spreadsheet.createAddressList(arguments[1]);
            setReferenceList(address,cells);
        }
    }

    /**
     * Sets the references of the range operation.
     * @param address
     * @param cells
     */
    public void setReferenceList(String address, Map<String,Cell> cells) {

        Cell _cell = cells.get(address);
        
        for (Cell cell : cells.values()) {            // remove the cell as an observer of the cells that are in the range
            if (cell.getObservers().contains(_cell)) {
                cell.removeObserver(_cell);
            }
        }
    
        _referenceList = new Cell[_addressList.length];
        for (int i = 0; i < _addressList.length; i++) {
            _referenceList[i] = cells.get(_addressList[i]);
            _referenceList[i].registerObserver(cells.get(address)); // add the cell as an observer of the cells that are in the range
        
        }
    }

    /**
     * Creates a list with all the values that are of type LiteralInt.
     * @param cells
     * @return values
     */
    public int[] createIntValueList(Map<String,Cell> cells) {
        
        int[] values = new int[_addressList.length];
        Visitor visitor = new SearchValue(cells);           // visitor that searches for the value of the cell reference
        
        for (int i = 0; i < _referenceList.length; i++) {
            try {                                               
                values[i] = Integer.parseInt(_referenceList[i].getContent().accept(visitor));     // if the value is an int, add it to the list
            } catch (NumberFormatException | NullPointerException e) { 
                setInvalidOperation();                                      // if the value is not an int, set the invalid operation
                break; 
            }
        }
        return values;
    }

    /**
     * Creates a list with all the values that are of type LiteralStr.
     * @param cells
     * @return stringsToPrint
     */
    public String[] createStringValueList(Map<String,Cell> cells) {

        List<String> values = new java.util.ArrayList<String>();
        Visitor visitor = new SearchValue(cells);

        for (int i = 0; i < _referenceList.length; i++) {
            try {
                String value = _referenceList[i].getContent().accept(visitor);   // if the value is a string, add it to the list
                if (value.startsWith("'")) { values.add(value.substring(1)); }  // if the value is a string, add it to the list                                  
            }
            catch (NullPointerException e) {}                                
        }
        return values.toArray(new String[values.size()]);
    }
}
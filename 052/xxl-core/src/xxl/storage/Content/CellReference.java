package xxl.storage.Content;

import java.util.Map;
import xxl.storage.Cell;
import xxl.DesignPatterns.Visitor.Visitor;
import xxl.DesignPatterns.Visitor.SearchValue;

/*
 * Class that represents a cell reference.
 */
public class CellReference extends Content  {

    private String reference_address;
    private String _address;
    private String _result = null;
    private Cell _cell;

    public CellReference(String address ,String referenceaddress, Map<String,Cell> cells) {
        _address = address;
        _cell = cells.get(_address);
        reference_address = referenceaddress  ;
            for (Cell cell : cells.values()) {
                if (cell.getObservers().contains(_cell)) {      // if the cell reference is already an observer of the cell, remove it 
                    cell.removeObserver(_cell);
                    break;
                }
            }
            Cell cell = cells.get(reference_address);         
            cell.registerObserver(_cell);                     // add the cell reference as an observer of the cell
    }

    public String getReference() { return reference_address; }
    public String getAddress() { return _address; }
    public void setResult(String result) { _result = result; }
    public String getResult() { return _result; }
    public Content getContent(Map<String,Cell> cells, String address) {
        return cells.get(reference_address).getContent();
    }
    
    /**
    * Returns the value of the cell reference.
    * @param cells the cells of the spreadsheet.
    * @return the value of the cell reference.
    */
    @Override
    public String getValue(Map<String,Cell> cells) {

        resetInvalidoperation(); 
        
        Visitor visitor = new SearchValue(cells);

        if ( getContent(cells,getReference()) == null ) { 
            setInvalidoperation();                     
            return getInvalidoperation();               // if the cell reference is invalid, return #VALUE
        } 
        if (_cell.getFlag() || getResult()==null ){     // if the referenced cell has changed or the cell reference has not been calculated yet 
            _cell.setFlag(false);   
            setResult(getContent(cells,getReference()).accept(visitor)); // if the cell reference is valid, return the value of the cell reference
            return getResult();
        }
        return getResult();
    }

    /**
     * Returns the value of the cell reference.
     */
    @Override
    public String accept(Visitor visitor) {
        return visitor.visitCellReference(this);
    }

}
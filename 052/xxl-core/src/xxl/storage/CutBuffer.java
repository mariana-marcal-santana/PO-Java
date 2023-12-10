package xxl.storage;

import xxl.Spreadsheet;
import xxl.DesignPatterns.Visitor.SearchValue;
import xxl.DesignPatterns.Visitor.Visitor;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class representing a CutBuffer
 */

public class CutBuffer {

    private int _lines;
    private int _columns;
    private Map<String,String[]> _contents = new TreeMap<String,String[]>();

    public CutBuffer(int lines, int columns) {
        _lines = lines;
        _columns = columns;
        for (int i = 1; i <= lines; i++) {
            for (int j = 1; j <= columns; j++) {
                String address = Integer.toString(i) + ";" + Integer.toString(j);
                String[] content = new String[2];
                _contents.put(address, content);
            }
        }
    }

    public int getLines() { return _lines; }
    public int getColumns() { return _columns; }
    public Map<String,String[]> getContents() { return _contents; }

    /**
     * Creates a list of the contents of the cutbuffer.
     * @return stringsToPrint
     */
    public String[] showCutBuffer() {

        ArrayList<String> stringsToPrint = new ArrayList<String>();
        
        for (String address : _contents.keySet()) {
            
            String toPrint = address + "|";
            if (_contents.get(address)[0] == null) { toPrint += ""; }
            else { toPrint += _contents.get(address)[0]; }

            if (_contents.get(address)[1] != null) { toPrint += _contents.get(address)[1]; } 

            stringsToPrint.add(toPrint);
        }
        return stringsToPrint.toArray(new String[0]);
    }

    /**
     * Copies the contents of the cells mentioned in addressList onto the cutbuffer.
     * @param addressList
     * @param cells
     * @param spreadsheet
     */
    public void copyCellRange(String[] addressList, Map<String,Cell> cells, Spreadsheet spreadsheet) {

        Visitor visitor = new SearchValue(cells);        // visitor that searches for the value of the cell reference

        int index = 0;
        for (String address : _contents.keySet()) {
            Cell cellToCopy = cells.get(addressList[index]);
            String[] cellToPaste = _contents.get(address);

            if (cellToCopy.getContent() != null) {
                // cellToPaste[0] has the value of the cell it copied
                cellToPaste[0] = cellToCopy.getContent().accept(visitor);
                // if cellToCopy has an inputString set it has cellToPaste[1]
                try { cellToPaste[1] = cellToCopy.getContent().getInputString(); }
                catch (NullPointerException e) { cellToPaste[1] = null; }
            }
            else {
                // case for empty cell
                cellToPaste[0] = null;
                cellToPaste[1] = null;
            }
            index ++;
        }
    }

    /**
     * Creates respective contents in the cells mentioned in rangeSpecification to the ones on the cutbuffer.
     * @param rangeSpecification
     * @param spreadsheet
     */
    public void pasteContents(String rangeSpecification, Spreadsheet spreadsheet) {
        
        String[] addressList;

        if (!rangeSpecification.contains(":")) {

            String[] addressArguments = rangeSpecification.split(";");
            // create a newAddress based on the "direction" of the cutbuffer (vertical or horizontal in the spreadsheet)
            String newAddress = (getLines() == 1) ?
                addressArguments[0] + ";" + Integer.toString(Integer.parseInt(addressArguments[1]) + getColumns() - 1) :
                Integer.toString(Integer.parseInt(addressArguments[0]) + getLines() - 1) + ";" + addressArguments[1];
            // set a newRange and create the list of addresses in the range
            String newRange = rangeSpecification + ":" + newAddress;
            addressList = spreadsheet.createAddressList(newRange);
        }

        else {
            String[] rangeArguments = rangeSpecification.split("[;:]+");
            // calculate the number of lines and columns of the specified range
            int numberOfLines = Math.abs(Integer.parseInt(rangeArguments[0]) - Integer.parseInt(rangeArguments[2])) + 1;
            int numberOfColumns = Math.abs(Integer.parseInt(rangeArguments[1]) - Integer.parseInt(rangeArguments[3])) + 1;
            // check if the dimension of the range is the same as the cutbuffer's
            if (getLines() != numberOfLines || getColumns() != numberOfColumns) { return; }
            else { addressList = spreadsheet.createAddressList(rangeSpecification); }
        }

        int index = 0;
        for (String[] contents : _contents.values()) {

            Cell cellToChange = spreadsheet.getStorage().getCells().get(addressList[index]);
    
            if (cellToChange == null) { return; } // if the cells is out of the spreadsheet's bounds, return
            // initialize content if it is an operation or cellReference
            try { cellToChange.checkWriteContent(contents[1], spreadsheet.getStorage().getCells(), spreadsheet); } 
            catch (NullPointerException e) { 
                // initialize content if it is a Literal
                try { cellToChange.checkWriteContent(contents[0], spreadsheet.getStorage().getCells(), spreadsheet); }
                // delete the cell, if the cutbuffer entry in empty
                catch (NullPointerException ee) { cellToChange.deleteCellContent(); }
            }
            index++;
        }
    }
}
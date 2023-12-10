package xxl.storage;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import xxl.Spreadsheet;
import xxl.DesignPatterns.Visitor.SearchFunction;
import xxl.DesignPatterns.Visitor.SearchValue;
import xxl.DesignPatterns.Visitor.Visitor;
import xxl.storage.Content.Content;

/** 
 * Class representing a storage. 
 */

public class Storage implements Serializable  {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private int _lines;
    private int _columns;
    private Map<String,Cell> _cells = null;

    public Storage (int numberOfLines, int numberOfColumns) {
        _lines = numberOfLines;
        _columns = numberOfColumns;
        _cells = setStorage(numberOfLines, numberOfColumns);
    }

    public int getColumns () { return _columns; }
    public int getLines () { return _lines; }
    public Map<String,Cell> getCells () { return _cells; }

    /**
     * Initializes the storage with the specified number of lines and columns.
     * @param lines
     * @param columns
     * @return cells
     */
    public Map<String,Cell> setStorage (int lines, int columns) {
        Map<String,Cell> cells = new TreeMap<String,Cell>();
        int i , j;
        for (i = 1; i <= lines; i++) {
              for (j = 1; j <= columns; j++) {
                String line = Integer.toString(i);
                String column = Integer.toString(j);
                String adress = line + ";" + column;
                Cell cell = new Cell(adress, i, j);
                cells.put(adress, cell);
            }
        }
        return cells;
    }

    /**
     * Sets the address key's value to null.
     * @param adress
     */
    public void deleteCellContent(String adress){
        Cell cellToDelete = _cells.get(adress);
        cellToDelete.deleteCellContent();
    }

    /**
     * Inserts the specified content in the specified address' cell.
     * @param address
     * @param contentInput
     */
    public void insertCellContent(String address, String contentInput, Spreadsheet spreadsheet) {
        Cell cell = _cells.get(address);
        cell.checkWriteContent(contentInput, _cells, spreadsheet);
    }

    /**
     * Creates a list with information about the cells with addresses
     * mentioned in addressList.
     * @param addresslist
     * @return contents
     */
    public String[] showCellContents(String[] addresslist){
        String[] contents = new String[addresslist.length];
        int i = 0;
        for (String address : addresslist) {
            Cell cell = _cells.get(address);
            contents[i] = cell.toString(_cells);
            i++;
        }
        return contents;
    }

    /**
     * Creates a list with the cells that have the specified value.
     * @param valueSpecification
     * @return stringArray
     */
    public String[] showValues(String valueSpecification) {
            
        List<String> addressList = new ArrayList<String>();

        Visitor visitor = new SearchValue(_cells) ;

        // create list with contents of cells that have the specified value
        for (Cell cell : _cells.values()) {
            if (cell.getContent() != null && cell.getContent().accept(visitor).equals(valueSpecification)) {
                addressList.add(cell.toString(_cells));
            }
        }

        String[] stringArray = addressList.toArray(new String[0]);
        return stringArray;
    }

    /**
     * Creates a list with the cells that have the specified function.
     * @param functionSpecification
     * @return finalList
     */
    public String[] showFunctions(String functionSpecification) {

        List<String> list = new ArrayList<String>();

        Visitor visitor = new SearchFunction();

        // create list with contents of cells that have the specified function
        for (Cell cell : _cells.values()) {

            Content content = cell.getContent();

            if (content != null && content.getInputString() != null
            && content.accept(visitor).contains(functionSpecification)) {
                
                list.add(cell.toString(_cells));
            }
        }
        // sort by function name
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String content1, String content2) {
                String s1 = content1.split("=")[1];
                String s2 = content2.split("=")[1];
                return s1.compareTo(s2);
            }
        });

        ArrayList<ArrayList<String>> functionList = new ArrayList<ArrayList<String>>();
        int i = 0;
        // divide list into sublists by function name
        while (i < list.size()) {
            String function = list.get(i).split("[=()]+")[1];
            ArrayList<String> oneFunction = new ArrayList<String>();
            while (i < list.size() && list.get(i).contains(function)) {
                oneFunction.add(list.get(i));
                i++;
            }
            functionList.add(oneFunction);
        }

        ArrayList<String> finalList = new ArrayList<String>();
        // sort sublists by address and add to finalList
        for (ArrayList<String> oneFunction : functionList) {
            Collections.sort(oneFunction);
            finalList.addAll(oneFunction);
        }

        return finalList.toArray(new String[0]);
    }
}
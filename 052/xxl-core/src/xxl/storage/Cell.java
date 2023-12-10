package xxl.storage;

import java.io.Serializable;
import java.util.Map;
import xxl.Spreadsheet;
import xxl.storage.Content.*;
import xxl.storage.Content.Operations.BinaryOperations.*;
import xxl.storage.Content.Operations.RangeOperations.*;
import xxl.DesignPatterns.Observer.*;
import java.util.ArrayList;
import xxl.DesignPatterns.Visitor.*;

/** Class representing a cell. */

public class Cell implements Serializable , Subject , Observer { 

    private String _address;
    private int _line;
    private int _column;
    private Content _content = null;
    private boolean _flag = false;
    private ArrayList<Observer> _observers ;

    /**
     * Register an observer (type Cell) in this cell.
     * @param observer
     */
    @Override
    public void registerObserver(Observer observer) { _observers.add(observer); }

    /**
     * Remove an observer (type Cell) from this cell.
     * @param observer
     */
    @Override
    public void removeObserver(Observer observer){ _observers.remove(observer);}
 
    /**
     * Notify all observers (type Cell) that this cell has changed.
     * @param observer
     */
    @Override
    public void notifyObservers(){
        for (Observer observer : _observers) {
            observer.update();
        }
    }

    /**
     * Update the cell's flag , and notify recursively all observers (type Cell) that this cell has changed.
     *  @param observer
     */
    @Override
    public void update() {
        _flag = true;
        notifyObservers();
    }

    public Cell(String address, int line, int column) {
        _address = address;
        _line = line;
        _column = column;
        _observers = new ArrayList<Observer>();
    }

    public String getAddress() { return _address; }
    public int getLine() { return _line; }
    public int getColumn() { return _column; }
    public Content getContent() { return _content; }
    public void deleteCellContent() { _content = null; }
    public ArrayList<Observer> getObservers() { return _observers; }
    public boolean getFlag() { return _flag; }
    public void setFlag(boolean flag) { _flag = flag; }


    /**
     * Checks if the content is a LiteralInt, a LiteralStr or
     * an operation or cell reference.
     * @param contentInput
     * @param cells
     */
    public void checkWriteContent(String contentInput, Map<String,Cell> cells, Spreadsheet spreadsheet) {
        if (contentInput.charAt(0) == '\'') {
            _content = new LiteralStr(contentInput);
        }
        else if (contentInput.startsWith("=")) {
            checkWhichOperation(contentInput, cells, spreadsheet);
        }
        else {
            _content = new LiteralInt(contentInput);
        }
        notifyObservers();
    }

    /**
     * Checks which operation is in the contentInput or cell reference and creates the corresponding object.
     * @param contentInput
     * @param cells
     */
    public void checkWhichOperation(String contentInput, Map<String,Cell> cells, Spreadsheet spreadsheet) {
        
        String content = contentInput.substring(1);
        
        if (contentInput.contains("ADD")) {
            _content = new Add(getAddress(),content, cells);
        }
        else if (contentInput.contains("SUB")) {
            _content = new Sub(getAddress(),content, cells);
        }
        else if (contentInput.contains("MUL")) {
            _content = new Mul(getAddress(),content, cells);
        }
        else if (contentInput.contains("DIV")) {
            _content = new Div(getAddress(),content, cells);
        }
        else if (contentInput.contains("AVERAGE")) {
            _content = new Average(getAddress(),content, cells, spreadsheet);
        }
        else if (contentInput.contains("PRODUCT")) {
            _content = new Product(getAddress(),content, cells, spreadsheet);
        }
        else if (contentInput.contains("CONCAT")) {
            _content = new Concat(getAddress(),content, cells, spreadsheet);
        }
        else if (contentInput.contains("COALESCE")) {
            _content = new Coalesce(getAddress(),content, cells, spreadsheet);
        }
        else {
            _content = new CellReference(getAddress(),content, cells);
        }
        _content.setInputString(contentInput);
    }

    /**
     * Gets the cell's info based on its content.
     * @param cells
     * @return cell's info
     */
    public String toString(Map<String,Cell> cells){
        
        Visitor visitor = new ToString(cells);  // Create a visitor to get the cell's info

        if (_content == null) { return _address + "|" ; }
        else {
            return _address + "|" + _content.accept(visitor); // Accept the visitor and get the cell's info
        }
    }
}
  
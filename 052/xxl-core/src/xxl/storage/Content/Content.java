package xxl.storage.Content;

import java.io.Serializable;
import java.util.Map;
import xxl.storage.Cell;
import xxl.DesignPatterns.Visitor.Visitable;
/*
 * Class that represents a content.
 */
public abstract class Content implements Serializable , Visitable  {

    private String _invalidOperation = null;
    private String _inputString;

    public void setInputString(String inputString) {
        _inputString = inputString;
    }

    public String getInputString() {
        return _inputString;
    }

    public String getInvalidoperation() {
        return _invalidOperation;
    }

    public void setInvalidoperation() {
        _invalidOperation = "#VALUE";
    }

    public void resetInvalidoperation() {
        _invalidOperation = null;
    }

    public abstract String getValue(Map<String,Cell> cells);
    
}
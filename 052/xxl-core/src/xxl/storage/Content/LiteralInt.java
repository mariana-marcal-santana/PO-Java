package xxl.storage.Content;

import java.util.Map;
import xxl.storage.Cell;
import xxl.DesignPatterns.Visitor.Visitor;
/*
 * Class that represents a literal int.
 */
public class LiteralInt extends Content {

    private int _value;

    public LiteralInt(String value) {
        _value = Integer.parseInt(value);
    }

    /**
     * Returns the value of the literal int.
     * @param cells the cells of the spreadsheet.
     * @return the value of the literal int.
     */
    @Override
    public String getValue(Map<String,Cell> cells) {
        return String.valueOf(_value);
    }

    /**
     * Returns the value of the literal int.
     * @return the value of the literal int.
     */
    @Override
    public String accept(Visitor visitor) {
        return visitor.visitLiteralInt(this);  
    }

}

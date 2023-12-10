package xxl.storage.Content;
import java.util.Map;
import xxl.storage.Cell;
import xxl.DesignPatterns.Visitor.Visitor;
/*
 * Class that represents a LiteralStr.
 */
public class LiteralStr extends Content {

    private String _value;

    public LiteralStr(String value) {
        _value = value;
    }

    /**
     * Returns the value of the LiteralStr.
     * @param cells the cells of the spreadsheet.
     * @return the value of the LiteralStr.
     */
    @Override
    public String getValue(Map<String,Cell> cells) {
        return _value;
    }

    /**
     * Returns the value of the LiteralStr.
     * @return the value of the LiteralStr.
     */
    @Override
    public String accept(Visitor visitor) {
        return visitor.visitLiteralString(this);
    }
    
}

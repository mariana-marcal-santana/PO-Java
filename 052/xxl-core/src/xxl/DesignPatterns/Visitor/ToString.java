package xxl.DesignPatterns.Visitor;

import java.util.Map;

import xxl.storage.Cell;
import xxl.storage.Content.*;
import xxl.storage.Content.Operations.BinaryOperations.*;
import xxl.storage.Content.Operations.RangeOperations.*;

/**
 * Class that represents a visitor that searches for the value of a cell.
 */
public class ToString implements Visitor {

    private Map<String,Cell> _cells;

    public ToString(Map<String,Cell> cells) {_cells = cells; }

    public String visitAdd(Add add) { return add.getValue(_cells) + add.getInputString(); }
    public String visitSub(Sub sub) { return sub.getValue(_cells) + sub.getInputString(); }
    public String visitMul(Mul mul) { return mul.getValue(_cells) + mul.getInputString(); }
    public String visitDiv(Div div) { return div.getValue(_cells) + div.getInputString(); }
    public String visitCoalesce(Coalesce coalesce) { return coalesce.getValue(_cells) + coalesce.getInputString(); }
    public String visitConcat(Concat concat) { return concat.getValue(_cells) + concat.getInputString(); }
    public String visitProduct(Product product) { return product.getValue(_cells) + product.getInputString(); }
    public String visitAverage(Average average) { return average.getValue(_cells) + average.getInputString(); }
    public String visitLiteralInt(LiteralInt literalInt) { return literalInt.getValue(_cells); }
    public String visitLiteralString(LiteralStr literalString) { return literalString.getValue(_cells); }
    public String visitCellReference(CellReference cellReference) { return cellReference.getValue(_cells) + cellReference.getInputString();}

}

package xxl.DesignPatterns.Visitor;

import java.util.Map;

import xxl.storage.Cell;
import xxl.storage.Content.*;
import xxl.storage.Content.Operations.BinaryOperations.*;
import xxl.storage.Content.Operations.RangeOperations.*;


/**
 * Class that represents a visitor that searches for the value of a cell.
 */
public class SearchValue implements Visitor {

    private Map<String,Cell> _cells;

    public SearchValue(Map<String,Cell> cells) {_cells = cells; }
    
    public String visitAdd(Add add) { return add.getValue(_cells); }
    public String visitSub(Sub sub) { return sub.getValue(_cells); }
    public String visitMul(Mul mul) { return mul.getValue(_cells); }
    public String visitDiv(Div div) { return div.getValue(_cells); }
    public String visitCoalesce(Coalesce coalesce) { return coalesce.getValue(_cells); }
    public String visitConcat(Concat concat) { return concat.getValue(_cells); }
    public String visitProduct(Product product) { return product.getValue(_cells); }
    public String visitAverage(Average average) { return average.getValue(_cells); }
    public String visitLiteralInt(LiteralInt literalInt) { return literalInt.getValue(_cells); }
    public String visitLiteralString(LiteralStr literalString) { return literalString.getValue(_cells); }
    public String visitCellReference(CellReference cellReference) { return cellReference.getValue(_cells);}
}

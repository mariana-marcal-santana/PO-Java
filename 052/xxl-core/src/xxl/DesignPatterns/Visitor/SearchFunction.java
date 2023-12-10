package xxl.DesignPatterns.Visitor;

import xxl.storage.Content.*;
import xxl.storage.Content.Operations.BinaryOperations.*;
import xxl.storage.Content.Operations.RangeOperations.*;

/**
 * Class that represents a visitor to search function.
 */
public class SearchFunction implements Visitor {
    
    public String visitCoalesce(Coalesce coalesce) { return "COALESCE"; }
    public String visitConcat(Concat concat) { return "CONCAT"; }
    public String visitProduct(Product product) { return "PRODUCT"; }
    public String visitAverage(Average average) { return "AVERAGE"; }
    

    public String visitAdd(Add add) { return "ADD"; }
    public String visitSub(Sub sub) { return "SUB"; }
    public String visitMul(Mul mul) { return "MUL"; }
    public String visitDiv(Div div) { return "DIV"; }
    

    public String visitLiteralInt(LiteralInt literalInt) { return ""; }
    public String visitLiteralString(LiteralStr literalString) { return ""; }
    public String visitCellReference(CellReference cellReference) { return ""; }

}

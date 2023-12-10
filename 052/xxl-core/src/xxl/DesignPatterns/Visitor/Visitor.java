package xxl.DesignPatterns.Visitor;

import xxl.storage.Content.*;
import xxl.storage.Content.Operations.BinaryOperations.*;
import xxl.storage.Content.Operations.RangeOperations.*;

/**
 * Interface that represents a visitor.
 */
public interface Visitor {
    
    public String visitCoalesce(Coalesce coalesce);
    public String visitConcat(Concat concat);
    public String visitProduct(Product product);
    public String visitAverage(Average average);

    public String visitLiteralInt(LiteralInt literalInt);
    public String visitLiteralString(LiteralStr literalString);
    public String visitCellReference(CellReference cellReference);
    
    public String visitAdd(Add add);
    public String visitSub(Sub sub);
    public String visitMul(Mul mul);
    public String visitDiv(Div div);

}

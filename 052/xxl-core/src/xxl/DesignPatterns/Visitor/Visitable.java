package xxl.DesignPatterns.Visitor;

/**
 * Interface that represents a visitable.
 */
public interface Visitable {
    
    public String accept(Visitor visitor);

}

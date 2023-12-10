package xxl.storage.Content.Operations.RangeOperations;

import java.util.Map;

import xxl.Spreadsheet;
import xxl.storage.Cell;
import xxl.storage.Content.Content;
import xxl.DesignPatterns.Visitor.Visitor;
import xxl.DesignPatterns.Visitor.SearchValue;

public class Coalesce extends RangeOperations {
    
    public Coalesce(String address ,String input, Map<String,Cell> cells, Spreadsheet spreadsheet) {
        setAddresses(address,input,cells, spreadsheet);
    }

    @Override
    public String getValue(Map<String,Cell> cells) {

        Visitor visitor = new SearchValue(cells);

        if (getInvalidRangeArguments() != null) { return getInvalidRangeArguments(); }
        resetInvalidoperation();
        
        for (String address : getAddressList()) {

            Content content = cells.get(address).getContent();

            if (content != null && content.getValue(cells) != null
            && content.accept(visitor).startsWith("'")) {
                return content.accept(visitor);                     
            }
        }
        return "'";
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitCoalesce(this);
    }
}
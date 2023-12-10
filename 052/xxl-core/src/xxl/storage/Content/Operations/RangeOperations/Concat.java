package xxl.storage.Content.Operations.RangeOperations;

import java.util.Map;

import xxl.Spreadsheet;
import xxl.storage.Cell;


import xxl.DesignPatterns.Visitor.Visitor;

public class Concat extends RangeOperations  {
   
    public Concat(String address ,String input, Map<String,Cell> cells, Spreadsheet spreadsheet) {
        setAddresses(address,input,cells, spreadsheet);
    }

    @Override
    public String getValue(Map<String,Cell> cells) {

        if (getInvalidRangeArguments() != null) { return getInvalidRangeArguments(); }
        
        resetInvalidoperation();
        String[] values = createStringValueList(cells);
        String result = "'";
        if (values.length == 0) { return result; }
        for (String s : values) { result += s;}
        return result;
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitConcat(this);
    }
}
package xxl.storage.Content.Operations.RangeOperations;

import java.util.Map;

import xxl.Spreadsheet;
import xxl.storage.Cell;

import xxl.DesignPatterns.Visitor.Visitor;


public class Average extends RangeOperations  {

    public Average(String address ,String input, Map<String,Cell> cells, Spreadsheet spreadsheet) {
        setAddresses(address, input, cells, spreadsheet);
    }

    @Override
    public String getValue(Map<String,Cell> cells) {
        if (getInvalidRangeArguments() != null) { return getInvalidRangeArguments(); }
        resetInvalidoperation();
        int[] values = createIntValueList(cells);

        if (getInvalidOperation() != null) { return getInvalidOperation(); }

        int result = 0;
        for (int i : values) { result += i;}
        return Integer.toString(result/values.length);
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visitAverage(this);
    }
}
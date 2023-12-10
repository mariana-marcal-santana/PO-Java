package xxl.exceptions;

import java.io.Serial;

public class CoreInvalidCellRangeException extends Exception{

    @Serial
	private static final long serialVersionUID = 202308312359L;

    private String _addressSpecification;

    public CoreInvalidCellRangeException(String address) {
        _addressSpecification = address;
    }

    public String getInvalidCellRange() {
        return _addressSpecification;
    }
}
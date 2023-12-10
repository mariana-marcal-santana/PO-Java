package xxl.exceptions;

public class CoreUnknownFunctionException extends Exception {
    
        private static final long serialVersionUID = 202308312359L;
    
        private String _functionName;
    
        public CoreUnknownFunctionException(String functionName) {
            _functionName = functionName;
        }
    
        public String getUnknownFunction() {
            return _functionName;
        }
}
package xxl.storage.Content.Operations;

import xxl.storage.Content.Content;

/*
 * Class that represents an operation.
 */
public abstract class Operations extends Content {
    
    public void setInputString(String inputString) {
        super.setInputString(inputString);
    }

    public String getInputString() {
        return super.getInputString();
    }

    public void setInvalidOperation() {
        super.setInvalidoperation();
    }

    public String getInvalidOperation() {
        return super.getInvalidoperation();
    }
}
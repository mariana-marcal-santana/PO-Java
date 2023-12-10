package xxl;

import java.io.Serial;
import java.io.Serializable;

import xxl.exceptions.CoreInvalidCellRangeException;
import xxl.exceptions.CoreUnknownFunctionException;
import xxl.storage.CutBuffer;
import xxl.storage.Storage;
import java.util.Map;
import java.util.TreeMap;

/*Class representing a spreadsheet.*/

public class Spreadsheet implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private Storage _storage = null;
    private boolean _changed = false;
    private CutBuffer _cutBuffer = null;
    private User _currentUser = null;
    private Map<String, User> _users = new TreeMap<String, User>();

    public Spreadsheet(int lines, int columns) {
        _storage = new Storage(lines, columns);
        _changed = true;
        _currentUser = new User("root");
        _users.put("root", _currentUser);
    }

    public boolean getChanged() { return _changed;}
    public User getCurrentUser() { return _currentUser; }
    public Storage getStorage() { return _storage; }
    public CutBuffer getCutBuffer() { return _cutBuffer; }

    public void setCurrentUser(User user) { _currentUser = user; }
    public void setChanged(boolean changed) { _changed = changed; }
    public void putUser(String username, User user) { _users.put(username, user); }

    /**
     * Sets the cut buffer and copies the ranges contents onto the cutbuffer.
     * @param rangeSpecification
     */
    public void setCutBuffer (String rangeSpecification) {
        if (!rangeSpecification.contains(":")) {
            _cutBuffer = new CutBuffer(1,1);
        }
        else {
            // split in different cells addresses
            String first_address = rangeSpecification.split(":")[0];
            String second_address = rangeSpecification.split(":")[1];
            // get info about the first cell address
            int first_line_adress = Integer.parseInt(first_address.split(";")[0]);
            int first_column_adress = Integer.parseInt(first_address.split(";")[1]);
            // get info about the second cell address
            int second_line_adress = Integer.parseInt(second_address.split(";")[0]);
            int second_column_adress = Integer.parseInt(second_address.split(";")[1]);    
            // calculate the dimensions of the cutbuffer
            int numberOfLines = Math.abs(first_line_adress - second_line_adress) + 1;
            int numberOfColumns = Math.abs(first_column_adress - second_column_adress) + 1;
            _cutBuffer = new CutBuffer(numberOfLines, numberOfColumns); 
        }
        // copy the contents of the range into the cutbuffer
        String[] addressList = createAddressList(rangeSpecification);
        _cutBuffer.copyCellRange(addressList, _storage.getCells(), this);
    }

    /**
     * Insert specified content in specified range.
     * @param rangeSpecification
     * @param contentSpecification
     * @throws CoreInvalidCellRangeException
     * @throws CoreUnknownFunctionException
     */
    public void insertContents(String rangeSpecification, String contentSpecification) throws CoreInvalidCellRangeException, CoreUnknownFunctionException {
        if (contentSpecification.isBlank()) { 
            deleteContents(rangeSpecification); 
            return; }
        String[] arguments = contentSpecification.split("[=(,)]+");
        if (arguments.length == 4) { //is a function
            if (!validFunction(arguments[1])) { throw new CoreUnknownFunctionException(arguments[1]);}
            if (arguments[2].contains(";"))
                if (!validCell(arguments[2])) { throw new CoreInvalidCellRangeException(arguments[2]); }
            if (arguments[3].contains(";"))
                if (!validCell(arguments[3])) { throw new CoreInvalidCellRangeException(arguments[3]); }
        }
        else if (arguments.length == 2) { //is a cell reference
            if (!validCell(arguments[1])) { throw new CoreInvalidCellRangeException(arguments[1]); }
        }

        String[] adressList = createAddressList(rangeSpecification);
        for (int i = 0; i < adressList.length; i++) {
            _storage.insertCellContent(adressList[i], contentSpecification, this);
        }
        setChanged(true);
    }

    /**
     * Checks if a specified function is a valid function.
     * @param function
     */
    public boolean validFunction(String function) {
        return (function.equals("ADD") || function.equals("SUB") || function.equals("MUL") ||
            function.equals("DIV") || function.equals("AVERAGE") || function.equals("PRODUCT") ||
            function.equals("CONCAT") || function.equals("COALESCE"));
    }

    /**
     *  Checks if specified cell is a valid cell in the spreadsheet.
     *  @param cellSpecification
     */
    public boolean validCell(String cellSpecification) {
        // get info about the cell address
        int line_adress = Integer.parseInt(cellSpecification.split(";")[0]);
        int column_adress = Integer.parseInt(cellSpecification.split(";")[1]);
        // if cell is inside the spreadsheet's storage
        if (line_adress > _storage.getLines() || column_adress > _storage.getColumns() || 
            line_adress < 1 || column_adress < 1)
            return false;

        return true;
    }

    /**
     * Checks if specified range is a valid range in the spreadsheet.
     * @param rangeSpecification
     */
    public boolean validRange(String rangeSpecification) {
        // split in different cells addresses
        String first_address = rangeSpecification.split(":")[0];
        String second_address = rangeSpecification.split(":")[1];
        // get info about the first cell address
        int first_line_adress = Integer.parseInt(first_address.split(";")[0]);
        int first_column_adress = Integer.parseInt(first_address.split(";")[1]);
        // get info about the second cell address
        int second_line_adress = Integer.parseInt(second_address.split(";")[0]);
        int second_column_adress = Integer.parseInt(second_address.split(";")[1]);
        // checks if both addresses are valid for the spreadsheet
        if (!validCell(first_address) || !validCell(second_address)) { return false; }
        // cehcks if the range is either a line or a column
        else if (first_line_adress != second_line_adress && first_column_adress != second_column_adress)
            return false;

        return true;
    }

    /**
     * Checks if specified address is a valid address in the spreadsheet.
     * @param addressSpecification
     * @throws CoreInvalidCellRangeException
     */
    public void validAddress(String addressSpecification) throws CoreInvalidCellRangeException {
        if (addressSpecification.contains(":")) { //is a range
            if(!validRange(addressSpecification)) { throw new CoreInvalidCellRangeException(addressSpecification); }
        }
        else { //is an individual cell
            if (!validCell(addressSpecification)) { throw new CoreInvalidCellRangeException(addressSpecification); }
        }
    }

    /**
     * Deletes the contents of cells from a specified range.
     * @param rangeSpecification
     */
    public void deleteContents(String rangeSpecification) {
        String[] addressList = createAddressList(rangeSpecification);
        for (int i = 0; i < addressList.length; i++) {
            _storage.deleteCellContent(addressList[i]);
        }
        setChanged(true);
    }

    /**
     * Creates a list of addresses from a range specification.
     * @param rangeSpecification
     * @return addressList
     */
    public String[] createAddressList(String rangeSpecification) {

        String[] addressList;

        if (!rangeSpecification.contains(":")) { //for induvidual cells
            addressList = new String[1];
            addressList[0] = rangeSpecification;
            return addressList;
        }

        // split in different cells addresses
        String first_adress = rangeSpecification.split(":")[0];
        String second_adress = rangeSpecification.split(":")[1];
        // get info about the first cell address
        int first_line_adress = Integer.parseInt(first_adress.split(";")[0]);
        int first_column_adress = Integer.parseInt(first_adress.split(";")[1]);
        // get info about the second cell address
        int second_line_adress = Integer.parseInt(second_adress.split(";")[0]);
        int second_column_adress = Integer.parseInt(second_adress.split(";")[1]);

        // if the range is not in the right order, switch it
        if (first_line_adress > second_line_adress) {
            int temp = first_line_adress;
            first_line_adress = second_line_adress;
            second_line_adress = temp;
        }
        if (first_column_adress > second_column_adress) {
            int temp = first_column_adress;
            first_column_adress = second_column_adress;
            second_column_adress = temp;
        }

        // if the range is a line
        if (first_line_adress == second_line_adress) {
            addressList = new String[second_column_adress - first_column_adress + 1];
            for (int i = first_column_adress; i <= second_column_adress; i++) {
                String line = Integer.toString(first_line_adress);
                String column = Integer.toString(i);
                String adress = line + ";" + column;
                addressList[i - first_column_adress] = adress;
            }
        }
        // if the range is a column
        else if (first_column_adress == second_column_adress) {
            addressList = new String[second_line_adress - first_line_adress + 1];
            for (int i = first_line_adress; i <= second_line_adress; i++) {
                String line = Integer.toString(i);
                String column = Integer.toString(first_column_adress);
                String adress = line + ";" + column;
                addressList[i - first_line_adress] = adress;
            }       
        }
        else { addressList = new String[0]; }
        
        return addressList;
    }

    /**
     * Showns the contents of cells from a specified range.
     * @param rangeSpecification
     * @return stringsToPrint
     */
    public String[] showCellContents(String rangeSpecification) {
        String[] addressList = createAddressList(rangeSpecification);
        String[] stringsToPrint = _storage.showCellContents(addressList);
        return stringsToPrint;
    }

    /**
     * Shows the cells of a specified function.
     * @param functionSpecification
     * @return stringsToPrint
     */
    public String[] showFunctions(String functionSpecification) {
        String[] stringsToPrint = _storage.showFunctions(functionSpecification);
        return stringsToPrint;
    }

    /**
     * Shows the cells of a specified value.
     * @param valueSpecification
     * @return stringsToPrint
     */
    public String[] showValues(String valueSpecification) {
        String[] stringsToPrint = _storage.showValues(valueSpecification);
        return stringsToPrint;
    }

    /**
     * Shows the cut buffer.
     * @return stringsToPrint
     */
    public String[] showCutBuffer() {
        if (_cutBuffer == null) { return new String[0]; }
        String[] stringsToPrint = _cutBuffer.showCutBuffer();
        return stringsToPrint;
    }

    /**
     * Copies the contents of cells from the cutbuffer onto specified range.
     * @param rangeSpecification
     */
    public void pasteContents(String rangeSpecification) {
        if (_cutBuffer == null) { return; }
        _cutBuffer.pasteContents(rangeSpecification, this);
        setChanged(true);
    }
}
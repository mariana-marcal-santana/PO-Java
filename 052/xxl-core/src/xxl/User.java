package xxl;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/** 
 * Class representing a user. 
 */

public  class User implements Serializable {

    private String _name;
    private Map<String, Spreadsheet> _spreadsheets;  

    public User(String name) {
        _name = name;
        _spreadsheets = new TreeMap<String, Spreadsheet>();
    }
    public void setName(String name) { _name = name; }
    public String getName() { return _name; }
    public Map<String, Spreadsheet> getSpreadsheets() { return _spreadsheets; }
    public void addSpreadsheet(String id, Spreadsheet spreadsheet) { _spreadsheets.put(id, spreadsheet); }
}

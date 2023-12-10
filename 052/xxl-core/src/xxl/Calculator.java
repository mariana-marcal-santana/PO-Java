package xxl;

import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.ActiveSpreadsheetException;
import xxl.storage.Storage;

/** Class representing a spreadsheet application. */

public class Calculator {

    private String _fileName = "";
    private Spreadsheet _spreadsheet = null;

    public Spreadsheet getSpreadsheet() { return _spreadsheet; }
    public void createSpreadsheet(int lines, int columns) { _spreadsheet = new Spreadsheet(lines, columns); }
    public String getFileName() { return _fileName; }
    public void resetFileName() { _fileName = ""; }

    /**
     * Saves the serialized application's state into the file associated to the current network.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save () throws FileNotFoundException, MissingFileAssociationException, IOException {
        // if the spreadsheet has not changed, there is nothing to save
        if (!_spreadsheet.getChanged()) { return; }
        // if the filename is empty, there is no file associated to the spreadsheet
        if (_fileName == "") { throw new MissingFileAssociationException(); }
        // otherwise, save the spreadsheet to the file
        try (ObjectOutputStream oos = new ObjectOutputStream (new BufferedOutputStream (new FileOutputStream (_fileName)))) {
            oos.writeObject(_spreadsheet);
            _spreadsheet.setChanged(false);
        }
    }

    /**
     * Saves the serialized application's state into the specified file. The current network is
     * associated to this file.
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void saveAs (String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        // associate the filename to the calculator
        _fileName = filename;
        save();
    }

    /**
     * Loads the serialized application's state from the specified file.
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load (String filename) throws UnavailableFileException {
        // load the spreadsheet from the file
        try (ObjectInputStream ois = new ObjectInputStream (new BufferedInputStream (new FileInputStream (filename)))) {
            _spreadsheet = (Spreadsheet) ois.readObject();
            _fileName = filename;
            // the spreadsheet has just been loaded, so it has not changed
            _spreadsheet.setChanged(false);
        }
        catch (ClassNotFoundException | IOException e) { throw new UnavailableFileException (filename); }
    }

    /**
     * Read text input file and create domain entitie
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    public void importFile(String filename) throws ImportFileException {

        String currentLine;
        String[] splitLine = new String [2];

           try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                //read number of lines and columns
                currentLine = reader.readLine();
                splitLine = currentLine.split("=");
                int numberOfLines = Integer.parseInt(splitLine[1]);
                currentLine = reader.readLine();
                splitLine = currentLine.split("=");
                int numberOfColumns = Integer.parseInt(splitLine[1]);
                // create new spreadsheet
                _spreadsheet = new Spreadsheet(numberOfLines, numberOfColumns);
                _spreadsheet.setChanged(true);
                Storage _storage = _spreadsheet.getStorage();
                // read the rest of the file and insert the content in the storage
                while ((currentLine = reader.readLine()) != null) {
                    splitLine = currentLine.split("\\|");
                    if (splitLine.length == 2)
                        _storage.insertCellContent(splitLine[0], splitLine[1], _spreadsheet);
                }
            }
            catch (FileNotFoundException e) { throw new ImportFileException(filename); }
            catch (IOException e) { throw new ImportFileException(filename); }
    }

    /**
     * Checks if there's a spreadsheet loaded and if it has changed.
     * @throws ActiveSpreadsheetException
     */
    public void checkSaveBeforeExit() throws ActiveSpreadsheetException {
        if (_spreadsheet != null && _spreadsheet.getChanged()) throw new ActiveSpreadsheetException(); 
    }

}
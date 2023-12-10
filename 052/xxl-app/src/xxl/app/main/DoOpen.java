package xxl.app.main;

import xxl.exceptions.UnavailableFileException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.exceptions.ActiveSpreadsheetException;
import xxl.Calculator;

/**
 * Open existing file.
 */
class DoOpen extends Command<Calculator> {

    DoOpen(Calculator receiver) {
        super(Label.OPEN, receiver);
    }

    @Override
    public final void execute() throws CommandException, FileOpenFailedException {

        try { _receiver.checkSaveBeforeExit(); }
        catch (ActiveSpreadsheetException e) {
            Boolean _answer = Form.confirm(Prompt.saveBeforeExit());
            if (_answer) {
                DoSave _save = new DoSave(_receiver);
                try { _save.execute(); } catch (NullPointerException ee) {}
            }
        }      
        
        String fileName = Form.requestString(Prompt.openFile());
        try { _receiver.load(fileName); }
        catch (UnavailableFileException e) { throw new FileOpenFailedException(e); }
    }

}
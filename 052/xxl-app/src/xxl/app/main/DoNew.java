package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Calculator;
import xxl.exceptions.ActiveSpreadsheetException;
/**
 * Open a new file.
 */
class DoNew extends Command<Calculator> {

    DoNew(Calculator receiver) {
        super(Label.NEW, receiver);
    }

    @Override
    protected final void execute() throws CommandException {

        try { _receiver.checkSaveBeforeExit(); }
        catch (ActiveSpreadsheetException e) {
            Boolean _answer = Form.confirm(Prompt.saveBeforeExit());
            if (_answer) {
                DoSave _save = new DoSave(_receiver);
                try { _save.execute(); } catch (NullPointerException ee) {}
            }
        }

        int _lines = Form.requestInteger(Prompt.lines());
        int _columns = Form.requestInteger(Prompt.columns());
        _receiver.createSpreadsheet(_lines, _columns);
        _receiver.resetFileName();
    }
}
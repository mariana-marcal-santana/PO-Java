package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.exceptions.CoreInvalidCellRangeException;
import xxl.Spreadsheet;

/**
 * Class for searching functions.
 */
class DoShow extends Command<Spreadsheet> {

    DoShow(Spreadsheet receiver) {
        super(Label.SHOW, receiver);
    }

    @Override
    protected final void execute() throws CommandException, InvalidCellRangeException {

        String _input = Form.requestString(Prompt.address());
        String[] stringsToPrint;

        try { _receiver.validAddress(_input); } 
        catch (CoreInvalidCellRangeException e) { throw new InvalidCellRangeException(e.getInvalidCellRange()); }

        stringsToPrint = _receiver.showCellContents(_input);

        for (String string : stringsToPrint) {
            _display.popup(string);
        }
    }
}
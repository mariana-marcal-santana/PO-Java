package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.exceptions.CoreInvalidCellRangeException;
import xxl.Spreadsheet;

/**
 * Copy command.
 */
class DoCopy extends Command<Spreadsheet> {

    DoCopy(Spreadsheet receiver) {
        super(Label.COPY, receiver);
    }

    @Override
    protected final void execute() throws CommandException {

        String _address = Form.requestString(Prompt.address());

        try { _receiver.validAddress(_address);

            _receiver.setCutBuffer(_address);

        } catch (CoreInvalidCellRangeException e) { throw new InvalidCellRangeException(e.getInvalidCellRange()); }
    }
}

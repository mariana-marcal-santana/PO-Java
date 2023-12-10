package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import pt.tecnico.uilib.forms.Form;
import xxl.exceptions.CoreInvalidCellRangeException;
//import java.io.PrintStream;
import xxl.exceptions.CoreUnknownFunctionException;

/**
 * Class for inserting data.
 */
class DoInsert extends Command<Spreadsheet> {

    DoInsert(Spreadsheet receiver) {
        super(Label.INSERT, receiver);
    }

    @Override
    protected final void execute() throws CommandException, InvalidCellRangeException, UnknownFunctionException {

        String _address = Form.requestString(Prompt.address());
        String _content = Form.requestString(Prompt.content());

        try { _receiver.validAddress(_address);
          
            try { _receiver.insertContents(_address, _content); }  
          
            catch (CoreUnknownFunctionException e) { throw new UnknownFunctionException(e.getUnknownFunction()); }
            catch (CoreInvalidCellRangeException e) { throw new InvalidCellRangeException(e.getInvalidCellRange()); }

        } catch (CoreInvalidCellRangeException e) { throw new InvalidCellRangeException(e.getInvalidCellRange()); }
    }
}
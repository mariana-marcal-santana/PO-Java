package xxl.app.search;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;

/**
 * Command for searching function names.
 */
class DoShowFunctions extends Command<Spreadsheet> {

    DoShowFunctions(Spreadsheet receiver) {
        super(Label.SEARCH_FUNCTIONS, receiver);
    }

    @Override
    protected final void execute() {

        String functionToSearch = Form.requestString(Prompt.searchFunction());

        String[] stringsToPrint = _receiver.showFunctions(functionToSearch);
        for (String string : stringsToPrint) { _display.popup(string); }
    }
}

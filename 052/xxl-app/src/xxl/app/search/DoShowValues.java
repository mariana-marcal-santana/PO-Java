package xxl.app.search;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;

/**
 * Command for searching content values.
 */
class DoShowValues extends Command<Spreadsheet> {

    DoShowValues(Spreadsheet receiver) {
        super(Label.SEARCH_VALUES, receiver);
    }

    @Override
    protected final void execute() {

        String valueToSearch = Form.requestString(Prompt.searchValue());

        String[] stringsToPrint = _receiver.showValues(valueToSearch);
        for (String string : stringsToPrint) {
            _display.popup(string);
        }
    }

}

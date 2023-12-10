package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;

/**
 * Show cut buffer command.
 */
class DoShowCutBuffer extends Command<Spreadsheet> {

    DoShowCutBuffer(Spreadsheet receiver) {
        super(Label.SHOW_CUT_BUFFER, receiver);
    }

    @Override
    protected final void execute() {

        String[] stringsToPrint = _receiver.showCutBuffer();
        for (String string : stringsToPrint) { _display.popup(string); }
    }

}

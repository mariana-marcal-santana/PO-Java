package xxl.app.main;

import java.io.IOException;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.Calculator;
import java.io.FileNotFoundException;

/**
 * Save to file under current name (if unnamed, query for name).
 */
class DoSave extends Command<Calculator> {

    DoSave(Calculator receiver) {
        super(Label.SAVE, receiver, xxl -> xxl.getSpreadsheet() != null);
    }

    @Override
	protected final void execute() throws CommandException {
		try { _receiver.save(); } 
        catch (MissingFileAssociationException e) {
			try { _receiver.saveAs(Form.requestString(Prompt.newSaveAs())); } 
            catch (MissingFileAssociationException ee) {  e.printStackTrace(); }
            catch (IOException ee) { e.printStackTrace(); }
		} 
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
	}
}





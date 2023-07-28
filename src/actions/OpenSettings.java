package actions;

import org.eclipse.jface.action.Action;

import dialogs.SettingsDialog;

public class OpenSettings extends Action{

    public OpenSettings() {
        super("Settings");
    }
    
    @Override
    public void run() {
    	SettingsDialog settingsDialog = new SettingsDialog();
    	settingsDialog.open();
    	
    }
}



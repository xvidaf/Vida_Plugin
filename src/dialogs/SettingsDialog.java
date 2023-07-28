package dialogs;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import models.OpenedProjects;
import models.Settings;

public class SettingsDialog extends TitleAreaDialog{
	private Combo selectProject;
    
    public SettingsDialog() {
        super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    }

    @Override
    public void create() {
        super.create();
        setTitle("Edit settings for the plugin");
        setMessage("You can edit the settings for the plugin here.\nConfirm the creation with the save button.", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        createProjectName(container);

        return area;
    }
    
    @Override
    protected void configureShell(Shell newShell)
    {
      super.configureShell(newShell);

      newShell.setText("Plugin Settings");
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
     super.createButtonsForButtonBar(parent);

     Button ok = getButton(IDialogConstants.OK_ID);
     ok.setText("Save");
     setButtonLayoutData(ok);

     Button cancel = getButton(IDialogConstants.CANCEL_ID);
     cancel.setText("Cancel");
     setButtonLayoutData(cancel);
  }

    private void createProjectName(Composite container) {
        Label lbtProjectName = new Label(container, SWT.NONE);
        lbtProjectName.setText("Main project designation");

        GridData dataProjectName = new GridData();
        dataProjectName.grabExcessHorizontalSpace = true;
        dataProjectName.horizontalAlignment = GridData.FILL;
        
        selectProject = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
        selectProject.setLayoutData(dataProjectName);
        
        
        for(IJavaProject selectedClass : OpenedProjects.getInstance().getProjects()) {
        	selectProject.add(selectedClass.getElementName());
        }
        selectProject.add("None");
        
        if(Settings.getInstance().getDesignatedMainProject() != null) {
        	selectProject.setText(Settings.getInstance().getDesignatedMainProject().getElementName());
        }
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected void okPressed() {
    	if(selectProject.getText() != "" && selectProject.getText() != "None") {
    		Settings.getInstance().setDesignatedMainProject(OpenedProjects.getInstance().findProjectByName(selectProject.getText()));
    	} else if(selectProject.getText() == "None") {
    		Settings.getInstance().setDesignatedMainProject(null);
    	}
    	super.okPressed();	
    }
}

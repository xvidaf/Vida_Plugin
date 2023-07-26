package dialogs;

import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
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
import models.RootManager;

public class CreateClassDialog extends TitleAreaDialog{
	private Combo selectClass;
	private Boolean created;
	private models.Class createdObject;
    
    public CreateClassDialog() {
        super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    }

    @Override
    public void create() {
        super.create();
        setTitle("Create a New Class Reference");
        setMessage("You can specify which class you want to reference.\nNote that the class choices are taken from opened projects.\nConfirm the creation with the create button.", IMessageProvider.INFORMATION);
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

      newShell.setText("Create new class reference");
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
     super.createButtonsForButtonBar(parent);

     Button ok = getButton(IDialogConstants.OK_ID);
     ok.setText("Create");
     setButtonLayoutData(ok);

     Button cancel = getButton(IDialogConstants.CANCEL_ID);
     cancel.setText("Cancel");
     setButtonLayoutData(cancel);
  }

    private void createProjectName(Composite container) {
        Label lbtProjectName = new Label(container, SWT.NONE);
        lbtProjectName.setText("Class to reference");

        GridData dataProjectName = new GridData();
        dataProjectName.grabExcessHorizontalSpace = true;
        dataProjectName.horizontalAlignment = GridData.FILL;
        
        selectClass = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
        selectClass.setLayoutData(dataProjectName);
        
        
        for(IType selectedClass : OpenedProjects.getInstance().getClasses()) {
        	selectClass.add(selectedClass.getFullyQualifiedName());
        }
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected void okPressed() {
    	if(selectClass.getText() != "") {
        	//If we want to create an element with an existing name, we throw an error
        	if(RootManager.getInstance().getAllInstances().containsKey(selectClass.getText())) {
        		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "The name of the element must be unique.");
        	} else {
        		IType createdClass = OpenedProjects.getInstance().findClassByFullyQualifiedName(selectClass.getText());
        		this.createdObject = new models.Class(createdClass);
            	this.created = true;
                super.okPressed();	
        	}
    	}
    }

	public Boolean isCreated() {
		return created;
	}

	public models.Class getCreatedElement() {
		return createdObject;
	}
}

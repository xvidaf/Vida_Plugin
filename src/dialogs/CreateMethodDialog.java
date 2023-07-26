package dialogs;

import org.eclipse.jdt.core.IMethod;
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

public class CreateMethodDialog extends TitleAreaDialog{
	
	private Combo selectMethod;
	private Boolean created;
	private models.Method createdObject;
    
    public CreateMethodDialog() {
        super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    }

    @Override
    public void create() {
        super.create();
        setTitle("Create a New Method Reference");
        setMessage("You can specify which method you want to reference.\nNote that the method choices are taken from opened projects.\nConfirm the creation with the create button.", IMessageProvider.INFORMATION);
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

      newShell.setText("Create new method reference");
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
        lbtProjectName.setText("Method to reference");

        GridData dataProjectName = new GridData();
        dataProjectName.grabExcessHorizontalSpace = true;
        dataProjectName.horizontalAlignment = GridData.FILL;
        
        selectMethod = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
        selectMethod.setLayoutData(dataProjectName);
        
        for(IMethod selectedMethod : OpenedProjects.getInstance().getMethods()) {
        	selectMethod.add(OpenedProjects.getInstance().getMethodFullName(selectedMethod));
        }
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected void okPressed() {
    	if(selectMethod.getText() != "") {
        	//If we want to create an element with an existing name, we throw an error
        	if(RootManager.getInstance().getAllInstances().containsKey(selectMethod.getText())) {
        		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "The name of the element must be unique.");
        	} else {
        		IMethod createdClass = OpenedProjects.getInstance().findMethodByFullyQualifiedName(selectMethod.getText());
        		this.createdObject = new models.Method(createdClass);
            	this.created = true;
                super.okPressed();	
        	}
    	}
    }

	public Boolean isCreated() {
		return created;
	}

	public models.Method getCreatedElement() {
		return createdObject;
	}

}

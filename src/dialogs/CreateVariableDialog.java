package dialogs;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
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
import models.Variable;

public class CreateVariableDialog extends TitleAreaDialog{
	private Combo selectClass;
	private Boolean created;
	private Variable createdObject;
    
    public CreateVariableDialog() {
        super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    }

    @Override
    public void create() {
        super.create();
        setTitle("Create a New Variable Reference");
        setMessage("You can specify which variable you want to reference.\nNote that the choices are taken from opened projects.\nConfirm the creation with the create button.", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        createVariableNames(container);

        return area;
    }
    
    @Override
    protected void configureShell(Shell newShell)
    {
      super.configureShell(newShell);

      newShell.setText("Create new variable reference");
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

    private void createVariableNames(Composite container) {
        Label lbtProjectName = new Label(container, SWT.NONE);
        lbtProjectName.setText("Variable to reference");

        GridData dataProjectName = new GridData();
        dataProjectName.grabExcessHorizontalSpace = true;
        dataProjectName.horizontalAlignment = GridData.FILL;
        
        selectClass = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
        selectClass.setLayoutData(dataProjectName);
        
        
        for(IType selectedClass : OpenedProjects.getInstance().getClasses()) {       	
        	try {
        		IField[] fields = selectedClass.getFields();
        	    for (IField field : fields) {
        	        selectClass.add(selectedClass.getFullyQualifiedName() + " " + field.getTypeSignature() + " " + field.getElementName());
        	    }
        	} catch (JavaModelException e) {
        	    e.printStackTrace();
        	}
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
        		IField createdVariable = null;
        		IType associatedClass = null;

        		for(IType selectedClass : OpenedProjects.getInstance().getClasses()) {       	
                	try {
                		IField[] fields = selectedClass.getFields();
                	    for (IField field : fields) {
                	        if(this.selectClass.getText().equals(selectedClass.getFullyQualifiedName() + " " + field.getTypeSignature() + " " + field.getElementName())) {
                	        	createdVariable = field;
                	        	associatedClass = selectedClass;
                	        	break;
                	        }
                	    }
                	} catch (JavaModelException e) {
                	    e.printStackTrace();
                	}
                }
        		
        		
        		try {
					this.createdObject = new Variable(createdVariable, associatedClass);
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
            	this.created = true;
                super.okPressed();	
        	}
    	}
    }

	public Boolean isCreated() {
		return created;
	}

	public Variable getCreatedElement() {
		return createdObject;
	}
}

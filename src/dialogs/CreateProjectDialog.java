package dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import models.Project;
import models.RootManager;

public class CreateProjectDialog extends TitleAreaDialog{
	private Text projectName;
	private Boolean created;
	private Project createdProject;
    
    public CreateProjectDialog() {
        super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    }

    @Override
    public void create() {
        super.create();
        setTitle("Element Details");
        setMessage("You can specify the project attributes below.\nConfirm the creation with the create button.", IMessageProvider.INFORMATION);
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

      newShell.setText("Element Detail");
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
        lbtProjectName.setText("Element Name");

        GridData dataProjectName = new GridData();
        dataProjectName.grabExcessHorizontalSpace = true;
        dataProjectName.horizontalAlignment = GridData.FILL;

        projectName = new Text(container, SWT.BORDER);
        projectName.setLayoutData(dataProjectName);
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected void okPressed() {
    	//If we want to create an element with an existing name, we throw an error
    	if(RootManager.getInstance().getAllInstances().containsKey(projectName.getText())) {
    		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "The name of the element must be unique.");
    	} else {
    		this.createdProject = new Project(projectName.getText());
        	this.created = true;
            super.okPressed();	
    	}
    }

	public Boolean isCreated() {
		return created;
	}

	public Project getCreatedElement() {
		return createdProject;
	}
}

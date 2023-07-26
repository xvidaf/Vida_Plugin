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

import models.Element;
import models.RootManager;

public class ElementDetail extends TitleAreaDialog{
	private Text elementName;

    private Element selectedElement;
    
    public ElementDetail(Element selectedElement) {
        super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        this.selectedElement = selectedElement;
    }

    @Override
    public void create() {
        super.create();
        setTitle("Element Details");
        setMessage("You can change the values of the attributes of the selected element.\nConfirm the changes with the save button.", IMessageProvider.INFORMATION);
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
     ok.setText("Save");
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

        elementName = new Text(container, SWT.BORDER);
        elementName.setLayoutData(dataProjectName);
        elementName.setText(this.selectedElement.getName());
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected void okPressed() {
    	//If we want to change the name of the Element, but the name already exists, we throw error
    	if(selectedElement.getName() != elementName.getText() && RootManager.getInstance().getAllInstances().containsKey(elementName.getText()) && selectedElement.getName() != "") {
    		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "The name of the element must be unique.");
    	} else {
        	this.selectedElement.setName(elementName.getText());
            super.okPressed();	
    	}
    }
}


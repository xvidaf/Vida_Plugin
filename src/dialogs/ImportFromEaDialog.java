package dialogs;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import models.RootManager;

public class ImportFromEaDialog extends TitleAreaDialog{
	private Boolean successful;
	private Text fileText;
    
    public ImportFromEaDialog() {
        super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    }

    @Override
    public void create() {
        super.create();
        setTitle("Import From EA");
        setMessage("Select the exported XML from from Enterprise Architect.", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(3, false);
        container.setLayout(layout);

        createGUI(container);

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
     ok.setText("Import");
     setButtonLayoutData(ok);

     Button cancel = getButton(IDialogConstants.CANCEL_ID);
     cancel.setText("Cancel");
     setButtonLayoutData(cancel);
  }

    private void createGUI(Composite container) {
		Label textLabel = new Label(container, SWT.NONE);
		textLabel.setText("EA file to import:");
		textLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		fileText = new Text(container, SWT.BORDER);
		fileText.setText("Please Select a File");
		fileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Button browseButton = new Button(container, SWT.PUSH);
		browseButton.setText("Browse");
		browseButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		Listener browseListener = new Listener() {

    		@Override
    		public void handleEvent(Event arg0) {
    			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    			 
    	        int r = j.showOpenDialog(null);

    	        if (r == JFileChooser.APPROVE_OPTION)
    	        {
    	        	fileText.setText(j.getSelectedFile().getAbsolutePath());
    	        }
    		}
        };
		
		browseButton.addListener(SWT.Selection, browseListener);
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected void okPressed() {
    	//If we want to create an element with an existing name, we throw an error
    	if(fileText.getText() != "") {
        	if(RootManager.getInstance().getAllInstances().containsKey(fileText.getText())) {
        		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Failed to import the project, did you select the correct file ?");
        	} else {
        		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "The import was succesful, but it is not implemented yet :).");
        	}
    	}
    }

	public Boolean isSuccessful() {
		return successful;
	}

}

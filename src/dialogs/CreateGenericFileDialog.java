package dialogs;

import java.io.File;

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

import models.GenericFile;
import models.RootManager;

public class CreateGenericFileDialog extends TitleAreaDialog{
	private Boolean created;
	private Text fileText;
	private GenericFile createdObject;
    
    public CreateGenericFileDialog() {        
        super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    }

    @Override
    public void create() {
        super.create();
        setTitle("Create a Generic File");
        setMessage("Select a file from the file system.", IMessageProvider.INFORMATION);
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

      newShell.setText("File Detail");
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

    private void createGUI(Composite container) {
		Label textLabel = new Label(container, SWT.NONE);
		textLabel.setText("File to import:");
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
    			// FileNameExtensionFilter filter = new FileNameExtensionFilter("EA xml files", "xml");
    			// j.setFileFilter(filter);
    			
    			
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
    		File selectedFile = new File(fileText.getText());
    		if(RootManager.getInstance().getAllInstances().containsKey(selectedFile.getName())) {
        		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "The name of the element must be unique.");
        	} else {
        		this.createdObject = new GenericFile(selectedFile);
            	this.created = true;
                super.okPressed();	
        	}
    	}
    }

	public Boolean isCreated() {
		return created;
	}

	public GenericFile getCreatedElement() {
		return createdObject;
	}
}

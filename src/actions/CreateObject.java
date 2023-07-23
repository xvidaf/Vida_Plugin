package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.CreateObjectDialog;
import models.Element;
import models.RootManager;

public class CreateObject extends Action{
	private TreeViewer treeViewer;

    public CreateObject(TreeViewer treeViewer) {
        super("Create New Object");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
    	CreateObjectDialog createObjectDialog = new CreateObjectDialog();
    	createObjectDialog.open();
    	
    	if(createObjectDialog.isCreated() != null) {
    		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
            Element selectedElement = (Element) selection.getFirstElement();
            
    		Element newInstance = createObjectDialog.getCreatedElement();
    		
    		selectedElement.addChild(newInstance);
            RootManager.getInstance().addChild(newInstance);
            treeViewer.refresh();
    	}
    }
}

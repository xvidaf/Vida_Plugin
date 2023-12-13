package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.CreateActionDialog;
import models.Element;
import models.RootManager;

public class CreateAction extends Action{
	private TreeViewer treeViewer;

    public CreateAction(TreeViewer treeViewer) {
        super("Create New Action");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
    	CreateActionDialog createObjectDialog = new CreateActionDialog();
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

package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.CreateClassDialog;
import models.Element;
import models.RootManager;

public class CreateClass extends Action{
	private TreeViewer treeViewer;

    public CreateClass(TreeViewer treeViewer) {
        super("Create New Class Reference");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
    	CreateClassDialog createClassDialog = new CreateClassDialog();
    	createClassDialog.open();
    	
    	if(createClassDialog.isCreated() != null) {
    		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
            Element selectedElement = (Element) selection.getFirstElement();
            
    		Element newInstance = createClassDialog.getCreatedElement();
    		
    		selectedElement.addChild(newInstance);
            RootManager.getInstance().addChild(newInstance);
            treeViewer.refresh();
    	}
    }
}

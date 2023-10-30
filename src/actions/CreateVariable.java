package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.CreateVariableDialog;
import models.Element;
import models.RootManager;

public class CreateVariable extends Action{
	private TreeViewer treeViewer;

    public CreateVariable(TreeViewer treeViewer) {
        super("Create New Variable Reference");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
    	CreateVariableDialog createVariableDialog = new CreateVariableDialog();
    	createVariableDialog.open();
    	
    	if(createVariableDialog.isCreated() != null) {
    		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
            Element selectedElement = (Element) selection.getFirstElement();
            
    		Element newInstance = createVariableDialog.getCreatedElement();
    		
    		selectedElement.addChild(newInstance);
            RootManager.getInstance().addChild(newInstance);
            treeViewer.refresh();
    	}
    }
}

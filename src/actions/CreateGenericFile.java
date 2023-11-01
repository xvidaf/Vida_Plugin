package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.CreateGenericFileDialog;
import models.Element;
import models.RootManager;

public class CreateGenericFile extends Action{
	
	private TreeViewer treeViewer;
	
	public CreateGenericFile(TreeViewer treeViewer) {
        super("Create New File Reference");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
    	CreateGenericFileDialog genericFileDialog = new CreateGenericFileDialog();
    	genericFileDialog.open();
    	
    	if(genericFileDialog.isCreated() != null) {
    		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
            Element selectedElement = (Element) selection.getFirstElement();
            
    		Element newInstance = genericFileDialog.getCreatedElement();
    		
    		selectedElement.addChild(newInstance);
            RootManager.getInstance().addChild(newInstance);
            treeViewer.refresh();
    	}
    	
        treeViewer.refresh();
    }

}

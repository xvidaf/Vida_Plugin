package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import models.Element;
import models.RootManager;

public class RemoveObject extends Action{
	
	private TreeViewer treeViewer;
	
    public RemoveObject(TreeViewer treeViewer) {
        super("Remove Selected Element");
        this.treeViewer = treeViewer;
    }
    
    public void run() {
        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        Element selectedElement = (Element) selection.getFirstElement();
        
        selectedElement.getParent().removeChild(selectedElement);
        //Remove all children of the removed element from master list
        RootManager.getInstance().removeChildrenFromList(selectedElement.getAllChildren());
        //Remove the element from the master list
        RootManager.getInstance().removeChild(selectedElement);
        
        treeViewer.refresh();
    }

}

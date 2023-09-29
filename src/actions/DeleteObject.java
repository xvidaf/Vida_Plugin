package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PlatformUI;

import models.Element;
import models.Final;
import models.Initial;
import models.ActivityDiagram;
import models.Project;
import models.RootManager;

public class DeleteObject extends Action{
	
	private TreeViewer treeViewer;
	
    public DeleteObject(TreeViewer treeViewer) {
        super("Delete");
        this.treeViewer = treeViewer;
    }
    
    public void run() {
        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        Element selectedElement = (Element) selection.getFirstElement();
        
        if (this.askForConfirmation(selectedElement)) {
            selectedElement.getParent().removeChild(selectedElement);
            //Remove all children of the removed element from master list
            RootManager.getInstance().removeChildrenFromList(selectedElement.getAllChildren());
            //Remove the element from the master list
            RootManager.getInstance().removeChild(selectedElement);
            
            treeViewer.refresh();
        }
    }
    
    public boolean askForConfirmation(Element element) {
    	if (element instanceof Project) {
    		return MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Delete", "Are you sure you want to delete the selected project ? \nNote that everything in the project will be deleted as well.");
    	} else if (element instanceof ActivityDiagram) {
    		return MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Delete", "Are you sure you want to delete the selected object ? \nNote that any children will be deleted as well.");
    	} else if (element instanceof Final) {
    		return MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Delete", "Are you sure you want to delete the selected final node ? \nNote that any children will be deleted as well.");
    	} else if (element instanceof Initial) {
    		return MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Delete", "Are you sure you want to delete the selected initial node ? \nNote that any children will be deleted as well.");
    	}
    	return MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Delete", "Are you sure you want to delete the selected element ? \nNote that any children will be deleted as well.");
    }

}

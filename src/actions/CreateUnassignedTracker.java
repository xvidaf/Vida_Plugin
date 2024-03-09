package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import models.Element;
import models.RootManager;
import models.UnassignedClasses;

public class CreateUnassignedTracker extends Action{
	private TreeViewer treeViewer;
	
    public CreateUnassignedTracker(TreeViewer treeViewer) {
        super("Create Tracker for Unassigned Classes");
        this.treeViewer = treeViewer;
    }

    @Override
    public void run() {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        Element selectedElement = (Element) selection.getFirstElement();
        
        UnassignedClasses newInstance = new UnassignedClasses();
        
        selectedElement.addChild(newInstance);
        RootManager.getInstance().addChild(newInstance);
    	treeViewer.refresh();
    }
}

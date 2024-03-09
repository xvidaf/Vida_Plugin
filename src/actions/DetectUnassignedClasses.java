package actions;

import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import models.Element;
import models.OpenedProjects;
import models.RootManager;

public class DetectUnassignedClasses extends Action{
	
	private TreeViewer treeViewer;

    public DetectUnassignedClasses(TreeViewer treeViewer) {
        super("Detect Unassigned Classes");
        this.treeViewer = treeViewer;
    }
    
    public void run() {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        Element selectedElement = (Element) selection.getFirstElement();
        
        for(IType selectedClass : OpenedProjects.getInstance().getClasses()) {
        	if(RootManager.getInstance().getAllInstances().containsKey(selectedClass.getFullyQualifiedName())) {
        		continue;
        	} else {
        		IType createdClass = OpenedProjects.getInstance().findClassByFullyQualifiedName(selectedClass.getFullyQualifiedName());
        		models.Class newInstance = new models.Class(createdClass);	
                selectedElement.addChild(newInstance);
                RootManager.getInstance().addChild(newInstance);
        	}
        }
    	treeViewer.refresh();
    }
}

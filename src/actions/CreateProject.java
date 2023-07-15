package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import models.Element;
import models.Project;
import models.RootManager;

public class CreateProject extends Action{
	private TreeViewer treeViewer;

    public CreateProject(TreeViewer treeViewer) {
        super("Create New Project");
        this.treeViewer = treeViewer;
    }
    
    public boolean isVisible() {
        // Get the selected item from the tree viewer
        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        Object selectedElement = selection.getFirstElement();

        // Check if the selected element is of a certain type
        // Modify this condition to match your specific logic
        return selectedElement instanceof RootManager;
    }

    @Override
    public void run() {
        // Prompt for any necessary input to create a new instance
        // or directly create a new instance of the dummy class
        Element newInstance = createNewInstance();

        RootManager.getInstance().addChild(newInstance);
        
        treeViewer.refresh();
    }

    private Element createNewInstance() {
    	return new Project("New Project");
    }

}

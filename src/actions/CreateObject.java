package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import models.Element;
import models.MyObject;
import models.RootManager;

public class CreateObject extends Action{
	private TreeViewer treeViewer;

    public CreateObject(TreeViewer treeViewer) {
        super("Create New Object");
        this.treeViewer = treeViewer;
    }
    
    public boolean isVisible() {
        // Get the selected item from the tree viewer
        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        Object selectedElement = selection.getFirstElement();

        // Check if the selected element is of a certain type
        // Modify this condition to match your specific logic
        return selectedElement instanceof Element;
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
    	return new MyObject("New Object");
    }

}

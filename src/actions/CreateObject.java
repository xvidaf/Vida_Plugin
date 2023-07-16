package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import models.Element;
import models.MyObject;

public class CreateObject extends Action{
	private TreeViewer treeViewer;

    public CreateObject(TreeViewer treeViewer) {
        super("Create New Object");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
        Element newInstance = createNewInstance();
        
        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        Element selectedElement = (Element) selection.getFirstElement();
        
        selectedElement.addChild(newInstance);
        
        treeViewer.refresh();
    }

    private Element createNewInstance() {
    	return new MyObject("New Object");
    }

}

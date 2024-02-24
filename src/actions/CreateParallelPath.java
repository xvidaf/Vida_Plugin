package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.CreateParallelPathDialog;
import models.Element;
import models.RootManager;

public class CreateParallelPath extends Action{
	
	private TreeViewer treeViewer;

    public CreateParallelPath(TreeViewer treeViewer) {
        super("Create New Path");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
    	CreateParallelPathDialog createParallelPath = new CreateParallelPathDialog();
    	createParallelPath.open();
    	
    	if(createParallelPath.isCreated() != null) {    		
    		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
            Element selectedElement = (Element) selection.getFirstElement();
            
            Element newInstance = createParallelPath.getCreatedElement();
                		
    		selectedElement.addChild(newInstance);
    		
            RootManager.getInstance().addChild(newInstance);
            treeViewer.refresh();
    	}
    }

}

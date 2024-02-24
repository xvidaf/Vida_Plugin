package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.CreateForkJoinDialog;
import models.Element;
import models.RootManager;

public class CreateForkJoin extends Action{
	
	private TreeViewer treeViewer;

    public CreateForkJoin(TreeViewer treeViewer) {
        super("Create New Fork/Join");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
    	CreateForkJoinDialog createForkJoin = new CreateForkJoinDialog();
    	createForkJoin.open();
    	
    	if(createForkJoin.isCreated() != null) {    		
    		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
            Element selectedElement = (Element) selection.getFirstElement();
            
            Element newInstance = createForkJoin.getCreatedElement();
                		
    		selectedElement.addChild(newInstance);
    		
            RootManager.getInstance().addChild(newInstance);
            treeViewer.refresh();
    	}
    }

}

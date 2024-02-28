package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.CreateActivityDiagramDialog;
import models.Element;
import models.RootManager;

public class CreateActivityDiagram extends Action{
	private TreeViewer treeViewer;

    public CreateActivityDiagram(TreeViewer treeViewer) {
        super("Create New Activity Diagram");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
    	CreateActivityDiagramDialog createObjectDialog = new CreateActivityDiagramDialog();
    	createObjectDialog.open();
    	
    	if(createObjectDialog.isCreated() != null) {
    		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
            Element selectedElement = (Element) selection.getFirstElement();
            
    		Element newInstance = createObjectDialog.getCreatedElement();
    		
    		selectedElement.addChild(newInstance);
            RootManager.getInstance().addChild(newInstance);
            treeViewer.refresh();
    	}
    }

}

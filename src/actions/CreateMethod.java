package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.CreateMethodDialog;
import models.Element;
import models.RootManager;

public class CreateMethod extends Action{
	private TreeViewer treeViewer;

    public CreateMethod(TreeViewer treeViewer) {
        super("Create New Method Reference");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
    	CreateMethodDialog createMethodDialog = new CreateMethodDialog();
    	createMethodDialog.open();
    	
    	if(createMethodDialog.isCreated() != null) {
    		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
            Element selectedElement = (Element) selection.getFirstElement();
            
    		Element newInstance = createMethodDialog.getCreatedElement();
    		
    		selectedElement.addChild(newInstance);
            RootManager.getInstance().addChild(newInstance);
            treeViewer.refresh();
    	}
    }
}

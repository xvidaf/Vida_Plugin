package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.CreateProjectDialog;
import models.Element;
import models.RootManager;

public class CreateProject extends Action{
	private TreeViewer treeViewer;

    public CreateProject(TreeViewer treeViewer) {
        super("Create New Project");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
    	CreateProjectDialog createProject = new CreateProjectDialog();
    	createProject.open();
    	
    	if(createProject.isCreated() != null) {
    		Element newInstance = createProject.getCreatedElement();
            RootManager.getInstance().addChild(newInstance);
            treeViewer.refresh();
    	}
    }

}

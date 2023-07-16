package actions;

import org.eclipse.jface.action.Action;
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
    
    @Override
    public void run() {
        Element newInstance = createNewInstance();

        RootManager.getInstance().addChild(newInstance);
        
        treeViewer.refresh();
    }

    private Element createNewInstance() {
    	return new Project("New Project");
    }

}

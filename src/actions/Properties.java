package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.ElementDetail;
import dialogs.ProjectDetail;
import models.Element;
import models.Project;

public class Properties extends Action{
	
	private TreeViewer treeViewer;

    public Properties(TreeViewer treeViewer) {
        super("Properties");
        this.treeViewer = treeViewer;
    }
	
    public void run() {
        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        Element selectedElement = (Element) selection.getFirstElement();
        
        
        if(selectedElement instanceof Project) {
            ProjectDetail projectDetail = new ProjectDetail( (Project) selectedElement);
            projectDetail.open();
        } else if(selectedElement instanceof Element){
            ElementDetail elementDetail = new ElementDetail(selectedElement);
            elementDetail.open();
        }
        treeViewer.refresh();
    }

}

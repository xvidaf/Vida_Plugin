package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import dialogs.ImportFromEaDialog;
import models.Element;

public class ImportFromEA extends Action{
	private TreeViewer treeViewer;
	
	public ImportFromEA(TreeViewer treeViewer) {
        super("Import Activity Diagram from EA");
        this.treeViewer = treeViewer;
    }
    
    @Override
    public void run() {
    	IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        Element selectedElement = (Element) selection.getFirstElement();
        
    	ImportFromEaDialog importFromEaDialog = new ImportFromEaDialog(selectedElement);
    	importFromEaDialog.open();
    	
        treeViewer.refresh();

    }
}

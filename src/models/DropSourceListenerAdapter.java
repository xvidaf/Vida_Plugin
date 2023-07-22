package models;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.TreeItem;

public class DropSourceListenerAdapter extends DropTargetAdapter{
	
	private TreeViewer treeViewer;
	private Element selectedElement;
	
	public DropSourceListenerAdapter(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
		this.selectedElement = null;
	}
	
	public void dragOver(DropTargetEvent event) {
    }

    public void drop(DropTargetEvent event) {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        selectedElement = (Element) selection.getFirstElement();
        TreeItem targetInstanceToFind = (TreeItem) event.item;
        if (targetInstanceToFind != null) {
            Element targetInstance = RootManager.getInstance().findInstance(targetInstanceToFind.getText());
            if (selectedElement != targetInstance) {
                selectedElement.getParent().removeChild(selectedElement);
                targetInstance.addChild(selectedElement);
                treeViewer.refresh();
            }	
        }
    }
}

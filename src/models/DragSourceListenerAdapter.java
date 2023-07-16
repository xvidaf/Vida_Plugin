package models;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

public class DragSourceListenerAdapter implements DragSourceListener{

	private TreeViewer treeViewer;
	private Element selectedElement;
	
	public DragSourceListenerAdapter(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
		this.selectedElement = null;
	}
	
	
	@Override
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        selectedElement = (Element) selection.getFirstElement();
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		event.data = selectedElement.getName();
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		selectedElement = null;
	}

}

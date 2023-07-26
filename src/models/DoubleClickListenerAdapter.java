package models;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class DoubleClickListenerAdapter implements IDoubleClickListener{
	
	private TreeViewer treeViewer;
	private Element selectedElement;
	
	public DoubleClickListenerAdapter(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
		this.selectedElement = null;
	}
	
	@Override
	public void doubleClick(DoubleClickEvent event) {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        selectedElement = (Element) selection.getFirstElement();
        
		try {
	        if (selectedElement instanceof models.Class) {
	        	Class classToOpen = (Class) selectedElement;
	        	if(JavaUI.openInEditor(classToOpen.getReferencedClass()) == null) {
	        		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "The selected class cannot be found, was it deleted?");
	        	}
	        } else if(selectedElement instanceof models.Method){
	        	Method methodToOpen = (Method) selectedElement;
				if(JavaUI.openInEditor(methodToOpen.getReferencedMethod()) == null) {
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "The selected method cannot be found, was it deleted?");
				}
	        }
		} catch (PartInitException e) {
			e.printStackTrace();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}

	}

}

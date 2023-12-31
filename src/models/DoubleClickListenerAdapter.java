package models;

import java.io.IOException;

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
	        	classToOpen.updateReferencedClass();
	        	if(JavaUI.openInEditor(classToOpen.getReferencedClass()) == null) {
	        		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "The selected class cannot be found, was it deleted?");
	        	}
	        } else if(selectedElement instanceof models.Method){
	        	Method methodToOpen = (Method) selectedElement;
	        	methodToOpen.updateReferencedClass();
				if(JavaUI.openInEditor(methodToOpen.getReferencedMethod()) == null) {
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "The selected method cannot be found, was it deleted?");
				}
	        } else if(selectedElement instanceof Variable){
	        	Variable variableToOpen = (Variable) selectedElement;
	        	variableToOpen.updateReferencedClass();
	        	variableToOpen.updateReferencedVariable();
				if(JavaUI.openInEditor(variableToOpen.getReferencedVariable()) == null) {
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "The selected method cannot be found, was it deleted?");
				}
	        } else if(selectedElement instanceof GenericFile){
	        	try {
	        		GenericFile toOpen = (GenericFile) selectedElement;
	        	    String filePath = toOpen.getAbsolutePath(); // Replace with your file path
	        	    Runtime.getRuntime().exec("explorer.exe /select," + filePath);
	        	} catch (IOException e) {
	        	    e.printStackTrace();
	        	}
	        }
		} catch (PartInitException e) {
			e.printStackTrace();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}

	}

}

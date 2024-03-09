package views;



import org.eclipse.jface.viewers.ITreeContentProvider;

import models.ActivityDiagram;
import models.ForkJoin;
import models.ParallelPath;
import models.Project;
import models.RootManager;
import models.UMLAction;
import models.UnassignedClasses;

public class MyContentProvider implements ITreeContentProvider {
    @Override
    public Object[] getElements(Object inputElement) {
    	//ArrayList<MyObject> rootElements = createDummyModels();
        //return rootElements.toArray();
    	if (inputElement instanceof RootManager) {
    		RootManager myObject = (RootManager) inputElement;
            return myObject.getChildren().toArray();
        }
    	if (inputElement instanceof Project) {
            Project myObject = (Project) inputElement;
            return myObject.getChildren().toArray();
        }
    	if (inputElement instanceof ActivityDiagram) {
            ActivityDiagram myObject = (ActivityDiagram) inputElement;
            return myObject.getChildren().toArray();
        }
        return new Object[0];
    	//return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parentElement) {
    	if (parentElement instanceof RootManager) {
    		RootManager myObject = (RootManager) parentElement;
            return myObject.getChildren().toArray();
        }
        if (parentElement instanceof ActivityDiagram) {
            ActivityDiagram myObject = (ActivityDiagram) parentElement;
            return myObject.getChildren().toArray();
        }
    	if (parentElement instanceof Project) {
            Project myObject = (Project) parentElement;
            return myObject.getChildren().toArray();
        }
    	if (parentElement instanceof UMLAction) {
    		UMLAction myObject = (UMLAction) parentElement;
            return myObject.getChildren().toArray();
        }
    	if (parentElement instanceof ForkJoin) {
    		ForkJoin myObject = (ForkJoin) parentElement;
            return myObject.getChildren().toArray();
        }
    	if (parentElement instanceof ParallelPath) {
    		ParallelPath myObject = (ParallelPath) parentElement;
            return myObject.getChildren().toArray();
        }
    	if (parentElement instanceof UnassignedClasses) {
    		UnassignedClasses myObject = (UnassignedClasses) parentElement;
            return myObject.getChildren().toArray();
        }
    	return new Object[0];
    }

    @Override
    public boolean hasChildren(Object element) {
    	if (element instanceof RootManager) {
    		RootManager myObject = (RootManager) element;
            return myObject.hasChildren();
        }
        if (element instanceof ActivityDiagram) {
            ActivityDiagram myObject = (ActivityDiagram) element;
            return myObject.hasChildren();
        }
    	if (element instanceof Project) {
            Project myObject = (Project) element;
            return myObject.hasChildren();
        }
    	if (element instanceof UMLAction) {
    		UMLAction myObject = (UMLAction) element;
            return myObject.hasChildren();
        }
    	if (element instanceof ForkJoin) {
    		ForkJoin myObject = (ForkJoin) element;
            return myObject.hasChildren();
        }
    	if (element instanceof ParallelPath) {
    		ParallelPath myObject = (ParallelPath) element;
            return myObject.hasChildren();
        }
    	if (element instanceof UnassignedClasses) {
    		UnassignedClasses myObject = (UnassignedClasses) element;
            return myObject.hasChildren();
        }
        return false;
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }
   
}

package views;



import org.eclipse.jface.viewers.ITreeContentProvider;

import models.MyObject;
import models.Project;
import models.RootManager;

public class MyContentProvider implements ITreeContentProvider {
    @Override
    public Object[] getElements(Object inputElement) {
        // Return the root elements of your object hierarchy
        // For example, let's assume you have a list of dummy models
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
    	if (inputElement instanceof MyObject) {
            MyObject myObject = (MyObject) inputElement;
            return myObject.getChildren().toArray();
        }
        return new Object[0];
    	//return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        // Return the children of the given parent element
        // For example, let's assume your MyObject class has a `getChildren` method
    	if (parentElement instanceof RootManager) {
    		RootManager myObject = (RootManager) parentElement;
            return myObject.getChildren().toArray();
        }
        if (parentElement instanceof MyObject) {
            MyObject myObject = (MyObject) parentElement;
            return myObject.getChildren().toArray();
        }
    	if (parentElement instanceof Project) {
            Project myObject = (Project) parentElement;
            return myObject.getChildren().toArray();
        }
    	return new Object[0];
    }

    @Override
    public boolean hasChildren(Object element) {
        // Return whether the given element has children
        // For example, let's assume your MyObject class has a `hasChildren` method
    	if (element instanceof RootManager) {
    		RootManager myObject = (RootManager) element;
            return myObject.hasChildren();
        }
        if (element instanceof MyObject) {
            MyObject myObject = (MyObject) element;
            return myObject.hasChildren();
        }
    	if (element instanceof Project) {
            Project myObject = (Project) element;
            return myObject.hasChildren();
        }
        return false;
    }

    @Override
    public Object getParent(Object element) {
        // Return the parent of the given element
        // If your objects have a reference to their parent, you can return it here
        return null;
    }
   
}

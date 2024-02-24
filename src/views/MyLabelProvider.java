package views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import models.Element;
import models.ActivityDiagram;
import models.Project;

public class MyLabelProvider extends LabelProvider {

	@Override
    public String getText(Object element) {
        if (element instanceof ActivityDiagram) {
            ActivityDiagram myObject = (ActivityDiagram) element;
            return myObject.getName();
        }
        if (element instanceof Project) {
        	Project myObject = (Project) element;
            return myObject.getName();
        }
        if (element instanceof models.Class) {
        	Element myObject = (Element) element;
            return myObject.getName() + ".java";
        }
        if (element instanceof Element) {
        	Element myObject = (Element) element;
            return myObject.getName();
        }
        return super.getText(element);
    }

    @Override
    public Image getImage(Object element) {
    	// TODO Doplnit ostatne
        // Return the image for the given element
        // You can use different icons based on the object type or other criteria
        if (element instanceof ActivityDiagram) {
        	return ImageDescriptor.createFromURL((this.getClass().getClassLoader().getResource("/icons/adwords.png"))).createImage();
        } else if (element instanceof models.Class) {
        	return ImageDescriptor.createFromURL((this.getClass().getClassLoader().getResource("/icons/class_obj.png"))).createImage();
        } else if (element instanceof models.Method) {
        	return ImageDescriptor.createFromURL((this.getClass().getClassLoader().getResource("/icons/methdef_obj.png"))).createImage();
        } else if (element instanceof models.Project) {
        	return ImageDescriptor.createFromURL((this.getClass().getClassLoader().getResource("/icons/presentation.png"))).createImage();
        } else if (element instanceof models.Initial) {
        	return ImageDescriptor.createFromURL((this.getClass().getClassLoader().getResource("/icons/initial.png"))).createImage();
        } else if (element instanceof models.Final) {
        	return ImageDescriptor.createFromURL((this.getClass().getClassLoader().getResource("/icons/final.png"))).createImage();
        } else if (element instanceof models.UMLAction) {
        	return ImageDescriptor.createFromURL((this.getClass().getClassLoader().getResource("/icons/Action.png"))).createImage();
        } else if (element instanceof models.ForkJoin) {
        	return ImageDescriptor.createFromURL((this.getClass().getClassLoader().getResource("/icons/forkjoin.png"))).createImage();
        } else if (element instanceof models.ParallelPath) {
        	return ImageDescriptor.createFromURL((this.getClass().getClassLoader().getResource("/icons/path.png"))).createImage();
        }
        return super.getImage(element);
    }
}

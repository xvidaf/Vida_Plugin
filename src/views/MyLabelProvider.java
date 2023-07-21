package views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import models.Element;
import models.MyObject;
import models.Project;

public class MyLabelProvider extends LabelProvider {

	@Override
    public String getText(Object element) {
        if (element instanceof MyObject) {
            MyObject myObject = (MyObject) element;
            return myObject.getName();
        }
        if (element instanceof Project) {
        	Project myObject = (Project) element;
            return myObject.getName();
        }
        if (element instanceof Element) {
        	Element myObject = (Element) element;
            return myObject.getName();
        }
        return super.getText(element);
    }

    @Override
    public Image getImage(Object element) {
        // Return the image for the given element
        // You can use different icons based on the object type or other criteria
        if (element instanceof MyObject) {
            return getImageForMyObject((MyObject) element);
        }
        return super.getImage(element);
    }

    private Image getImageForMyObject(MyObject myObject) {
        // Logic to determine the appropriate image based on the MyObject instance
        // You can use image descriptors or create images from files
        // For example, if you have different icons for different object types:
        // if (myObject instanceof SomeType) {
        //     return Activator.getImageDescriptor("path/to/icon1.png").createImage();
        // } else if (myObject instanceof AnotherType) {
        //     return Activator.getImageDescriptor("path/to/icon2.png").createImage();
        // }
        // Return a default image if needed
        return null;
    }

}

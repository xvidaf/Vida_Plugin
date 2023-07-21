package views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import models.Element;
import models.Final;
import models.Initial;

public class Sorter extends ViewerComparator {
	
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
	    int cat1 = category(e1);
	    int cat2 = category(e2);
	    if (cat1 != cat2) {
	    	return cat1 - cat2;
	    }
	    if (e1 instanceof Element && e2 instanceof Element) {
	    	Element element1 = (Element) e1;
	    	Element element2 = (Element) e2;
	    	if(element1.getSortOrder() - element2.getSortOrder() != 0) {
	    		return element1.getSortOrder() - element2.getSortOrder();
	    	}
	    }
        return super.compare(viewer, e1, e2);
    }
    
	public int category(Object element) {
		if (element instanceof Initial) {
			return 1;
		} else if (element instanceof Final) {
			return 3;
		}
		return 2;
	}
}

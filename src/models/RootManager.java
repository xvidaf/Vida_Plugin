package models;

import java.util.ArrayList;
import java.util.HashMap;

public class RootManager extends Element{

	private static final long serialVersionUID = 6295187106619715998L;
	
	private static RootManager instance;
	private HashMap<String, Element> allInstances;
	
    public RootManager() {
        super();
        this.allInstances = new HashMap<>();
    }

    // Get the singleton instance
    public static RootManager getInstance() {
        if (instance == null) {
            instance = new RootManager();
        }
        return instance;
    }
    
    public Element findInstance(String toFind) {
    	return this.allInstances.get(toFind);
    }
    
    public boolean addChildToList(Element child) {
    	if( this.allInstances.get(child.getName()) != null) {
    		return false;
    	}
    	this.allInstances.put(child.getName(), child);
    	return true;
    }
    
    public boolean removeChildFromList(Element child) {
    	if( this.allInstances.get(child.getName()) == null) {
    		return false;
    	}
    	this.allInstances.remove(child.getName());
    	return true;
    }
    
    public void removeChildrenFromList(ArrayList<Element> children) {
    	for(Element child : children) {
    		this.removeChildFromList(child);
    	}
    }
    
    public HashMap<String, Element> getAllInstances() {
    	return this.allInstances;
    }
    
    public void replaceInstance(RootManager replacement) {
    	instance = replacement;
    }
    
}

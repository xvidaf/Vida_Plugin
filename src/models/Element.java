package models;

import java.io.Serializable;
import java.util.ArrayList;


public class Element implements Serializable{

	private static final long serialVersionUID = -2161167441824704445L;
	
	private String name = "";
    protected Element parent;
    private ArrayList<Element> children;
    private int sortOrder;
    private String alias = "";
    
    public Element() {
    	this.children = new ArrayList<>();
    	this.sortOrder = 1000;
    }
    
    public Element(String name) {
    	this.name = name;
    	this.children = new ArrayList<>();
    }
    
    public Element(String name, String alias) {
    	this.name = name;
    	this.children = new ArrayList<>();
    	this.setAlias(alias);
    }
    
    public Element(String name, int sortOrder) {
    	this.name = name;
    	this.children = new ArrayList<>();
    	this.sortOrder = sortOrder;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
    	//Have to remove the element from master list, since the name is key
        RootManager.getInstance().removeChildFromList(this);
        this.name = name;
        RootManager.getInstance().addChildToList(this);
    }
    
	public Element getParent() {
		return parent;
	}
	
	public void setParent(Element parent) {
		this.parent = parent;
	}
	
    public void addChild(Element child) {
    	if(RootManager.getInstance().addChildToList(child)) {
    		 children.add(child);
    		 child.setParent(this);
    	}
    }
    
    public void removeChild(Element child) {
    	if(RootManager.getInstance().removeChildFromList(child)) {
    		 children.remove(child);
    	}
    }

    public ArrayList<Element> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

	public int getSortOrder() {
		return sortOrder;
	}
	
	public ArrayList<Element> getAllChildren() {
	    ArrayList<Element> allChildren = new ArrayList<Element>();
	    for (Element child : this.getChildren()) {
	    	allChildren.add(child);
	    	allChildren.addAll(child.getAllChildren());
	    }
	    return allChildren;
	}
	
	public boolean isParentOf(Element element) {
		if (this.children.contains(element)) {
			return true;
		}
		return false;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}

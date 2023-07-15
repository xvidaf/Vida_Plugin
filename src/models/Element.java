package models;

import java.util.ArrayList;

public class Element {
    private String name;
    protected Element parent;
    private ArrayList<Element> children;
    
    public Element() {
    	this.children = new ArrayList<>();
    }
    
    public Element(String name) {
    	this.name = name;
    	this.children = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    
	public Element getParent() {
		return parent;
	}
	
    public void addChild(Element child) {
        children.add(child);
    }

    public ArrayList<Element> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

}

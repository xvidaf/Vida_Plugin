package models;

import java.util.ArrayList;

public class Project extends Element{
	private ArrayList<MyObject> children;
	
    public Project(String name) {
        super(name);
        this.children = new ArrayList<>();
    }

	public ArrayList<MyObject> getChildren() {
		return children;
	}
	
	public void addChild(MyObject child) {
		this.children.add(child);
	}
	
    public boolean hasChildren() {
        return !children.isEmpty();
    }
	

}

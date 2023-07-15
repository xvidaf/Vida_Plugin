package models;

import java.util.ArrayList;

public class RootManager {
	private ArrayList<Project> children;
	private static RootManager instance;
	
    public RootManager() {
        this.children = new ArrayList<>();
    }

    // Get the singleton instance
    public static RootManager getInstance() {
        if (instance == null) {
            instance = new RootManager();
        }
        return instance;
    }
    
	public ArrayList<Project> getChildren() {
		return children;
	}
	
	public void addChild(Project child) {
		this.children.add(child);
	}
	
    public boolean hasChildren() {
        return !children.isEmpty();
    }

}

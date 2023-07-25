package models;

public class Project extends Element{
	
	private static final long serialVersionUID = -2341052622334261515L;

	public Project(String name) {
        super(name);
    }
    
    public Project(String name, int sortOrder) {
        super(name);
    }
}

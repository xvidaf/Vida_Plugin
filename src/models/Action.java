package models;

public class Action extends Element{
	private static final long serialVersionUID = 1292883764307783061L;

	public Action(String name) {
        super(name);
    }
	
	public Action(String name, String alias) {
        super(name,alias);
    }
    
    public Action(String name, int sortOrder) {
        super(name, sortOrder);
    }
}

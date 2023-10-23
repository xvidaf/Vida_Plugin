package models;

public class UMLAction extends Element{
	private static final long serialVersionUID = 1292883764307783061L;

	public UMLAction(String name) {
        super(name);
    }
	
	public UMLAction(String name, String alias) {
        super(name,alias);
    }
    
    public UMLAction(String name, int sortOrder) {
        super(name, sortOrder);
    }
}

package models;

public class Initial extends Element{
	
	private static final long serialVersionUID = -7881826205009268845L;

	public Initial(String name) {
        super(name);
    }
	
	public Initial(String name, String alias) {
        super(name,alias);
    }
    
    public Initial(String name, int sortOrder) {
        super(name, sortOrder);
    }

}

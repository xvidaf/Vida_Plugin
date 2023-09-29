package models;

public class Final extends Element{
	
	private static final long serialVersionUID = 2609537539004846530L;

	public Final(String name) {
        super(name);
    }
	
	public Final(String name, String alias) {
        super(name,alias);
    }
    
    public Final(String name, int sortOrder) {
        super(name, sortOrder);
    }
}

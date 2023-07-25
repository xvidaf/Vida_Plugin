package models;


public class MyObject extends Element{

	private static final long serialVersionUID = 1292883764307783061L;

	public MyObject(String name) {
        super(name);
    }
    
    public MyObject(String name, int sortOrder) {
        super(name, sortOrder);
    }
  
}

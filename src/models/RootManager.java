package models;

public class RootManager extends Element{
	private static RootManager instance;
	
    public RootManager() {
        super();
    }

    // Get the singleton instance
    public static RootManager getInstance() {
        if (instance == null) {
            instance = new RootManager();
        }
        return instance;
    }
    
}

package models;

import java.io.Serializable;

import org.eclipse.jdt.core.IJavaProject;

public class Settings implements Serializable{
	
	private static final long serialVersionUID = 6295187106619715998L;
	
	private static Settings instance;
	private transient IJavaProject designatedMainProject;
	
    public Settings() {
        super();
        this.setDesignatedMainProject(null);
    }

    // Get the singleton instance
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

	public IJavaProject getDesignatedMainProject() {
		return designatedMainProject;
	}

	public void setDesignatedMainProject(IJavaProject designatedMainProject) {
		this.designatedMainProject = designatedMainProject;
	}
	
    public void replaceInstance(Settings replacement) {
    	instance = replacement;
    }
    
    public void restoredesignatedMainProject(String toRestore) {
    	this.designatedMainProject = OpenedProjects.getInstance().findProjectByName(toRestore);
    }


}

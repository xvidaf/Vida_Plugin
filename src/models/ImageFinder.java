package models;

import java.io.File;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

public class ImageFinder {
	
	private static ImageFinder instance;
	
	File dir = null; //new File("PATH_TO_YOUR_DIRECTORY");
	
	IWorkspace workspace = ResourcesPlugin.getWorkspace();
	
	File workspaceDirectory = workspace.getRoot().getLocation().toFile();
	
	File[] listOfImages;
	
    public ImageFinder() {
        super();
        this.setPathToImageDir();
    }
	
    // Get the singleton instance
    public static ImageFinder getInstance() {
        if (instance == null) {
            instance = new ImageFinder();
        }
        return instance;
    }
	
	public void setPathToImageDir(){
		if (Settings.getInstance().getDesignatedMainProject() != null) {
			File dir = new File(Settings.getInstance().getDesignatedMainProject().getProject().getLocation().toString() + "\\images");
			listOfImages = dir.listFiles();
		} else {
			System.out.println("Main project not set.");
		}
	}
	
	public File[] getImages() {
		return this.listOfImages;
	}
	
	public String getPathToImageByName(String name) {
		if (name.equals("None")) {
			return "None";
		}
		for(File image : ImageFinder.getInstance().getImages()) {
        	if(image.getName().equals(name)) {
        		return image.getAbsolutePath().toString();
        	}
        }
		return "None";
	}

}

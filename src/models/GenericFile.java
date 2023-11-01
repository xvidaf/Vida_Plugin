package models;

import java.io.File;


public class GenericFile extends Element{

	private static final long serialVersionUID = -7013788112246826483L;
	
	String absolutePath;
	
	public GenericFile(File referencedFile) {
		super(referencedFile.getName());
		this.setAbsolutePath(referencedFile.getAbsolutePath());
	}
	
	public GenericFile(File referencedFile, int sortOrder) {
		super(referencedFile.getName(), sortOrder);
		this.setAbsolutePath(referencedFile.getAbsolutePath());
	}

	public void setAbsolutePath(String path) {
		this.absolutePath = path;
	}
	
	public String getAbsolutePath() {
		return absolutePath;
	}
}

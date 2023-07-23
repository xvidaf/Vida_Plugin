package models;

import java.util.ArrayList;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

public class OpenedProjects {
	private static OpenedProjects instance;
	private ArrayList<IJavaProject> projects;
	private ArrayList<IPackageFragment> packages;
	private ArrayList<IType> classes;
	private ArrayList<IMethod> methods;
	
    public OpenedProjects() {
        super();
        this.projects = new ArrayList<>();
        this.packages = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    // Get the singleton instance
    public static OpenedProjects getInstance() {
        if (instance == null) {
            instance = new OpenedProjects();
        }
        return instance;
    }
    
    public void updateProjects(ArrayList<IJavaProject> projects, ArrayList<IPackageFragment> packages, ArrayList<IType> classes, ArrayList<IMethod> methods) {
    	this.projects = projects;
        this.packages = packages;
        this.classes = classes;
        this.methods = methods;
    }

	public ArrayList<IJavaProject> getProjects() {
		return projects;
	}

	public ArrayList<IPackageFragment> getPackages() {
		return packages;
	}

	public ArrayList<IType> getClasses() {
		return classes;
	}

	public ArrayList<IMethod> getMethods() {
		return methods;
	}
}

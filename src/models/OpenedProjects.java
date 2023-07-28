package models;

import java.util.ArrayList;
import java.util.Map;

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
	
	public IJavaProject findProjectByName(String nameToFind) {
        for(IJavaProject selectedProject : this.projects) {
        	if(nameToFind.equals(selectedProject.getElementName())) {
        		return selectedProject;
        	}
        }
        return null;
	}
	
	public IType findClassByFullyQualifiedName(String nameToFind) {
        for(IType selectedClass : this.classes) {
        	if(nameToFind.equals(selectedClass.getFullyQualifiedName())) {
        		return selectedClass;
        	}
        }
        return null;
	}
	
	public IMethod findMethodByFullyQualifiedName(String methodToFind) {
        for(IMethod selectedMethod : this.methods) {
        	if(methodToFind.equals(getMethodFullName(selectedMethod))) {
        		return selectedMethod;
        	}
        }
        return null;
	}
	
	public String getMethodFullName(IMethod iMethod)
	{
	    StringBuilder name = new StringBuilder();
	    name.append(iMethod.getDeclaringType().getFullyQualifiedName());
	    name.append(".");
	    name.append(iMethod.getElementName());
	    name.append("(");
	
	    String comma = "";
	    for (String type : iMethod.getParameterTypes()) {
	            name.append(comma);
	            comma = ", ";
	            name.append(type);
	    }
	    name.append(")");
	
	    return name.toString();
	}
	
	//References cannot be serialized, have to be updated manually
	public void refreshReferences() {
		for (Map.Entry<String, Element> set : RootManager.getInstance().getAllInstances().entrySet()) {
			if (set.getValue() instanceof models.Class) {
				models.Class classToUpdate = (models.Class) set.getValue();
				classToUpdate.updateReferencedClass();
			} else if (set.getValue() instanceof models.Method) {
				models.Method methodToUpdate = (models.Method) set.getValue();
				methodToUpdate.updateReferencedClass();
			}

       }
	}
}

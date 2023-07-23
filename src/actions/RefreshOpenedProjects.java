package actions;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;

import models.OpenedProjects;

public class RefreshOpenedProjects extends Action {
	
	private ArrayList<IJavaProject> javaProjects;
	private ArrayList<IPackageFragment> packages;
	private ArrayList<IType> classes;
	private ArrayList<IMethod> methods;

    public RefreshOpenedProjects() {
        super("Refresh Opened Projects");
        this.javaProjects = new ArrayList<>();
        this.packages = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    @Override
    public void run() {
        // Get the root of the workspace
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

        try {
            // Iterate through the projects
            for (IProject project : projects) {
                // Only pick Java projects, which are open
                if (project.isOpen() && project.hasNature(JavaCore.NATURE_ID)) {
                    IJavaProject javaProject = JavaCore.create(project);
                    //System.out.println("Opened project: " + javaProject.getElementName());
                    this.javaProjects.add(javaProject);
                    IPackageFragment[] packageFragments = javaProject.getPackageFragments();
                    for (IPackageFragment packageFragment : packageFragments) {
                    	this.packages.add(packageFragment);
                        if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
                            ICompilationUnit[] compilationUnits = packageFragment.getCompilationUnits();
                            for (ICompilationUnit compilationUnit : compilationUnits) {
                                IType[] types = compilationUnit.getTypes();
                                for (IType type : types) {
                                    //System.out.println("Class name: " + type.getFullyQualifiedName());
                                    //Just for testing, delete later
                                    JavaUI.openInEditor(type);
                                    this.classes.add(type);
                                    // Get all methods declared in the class
                                    IMethod[] methods = type.getMethods();
                                    for (IMethod method : methods) {
                                    	this.methods.add(method);
                                        //System.out.println("Method: " + method.getElementName());
                                        // Can also open methods if needed
                                        // IEditorPart editorPart = JavaUI.openInEditor(method);

                                    }
                                }
                            }
                        }
                    }

                }
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
        OpenedProjects.getInstance().updateProjects(javaProjects, packages, classes, methods);
        // System.out.println(getJavaProjects());
    }
}

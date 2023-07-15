package views;


import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import models.MyObject;
import models.Project;
import models.RootManager;

public class MainView extends org.eclipse.ui.part.ViewPart{
	
	private TreeViewer treeViewer;

	@Override
    public void createPartControl(Composite parent) {
        // Create a composite as the container for the MainView
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout());

        // Create the MainView
        treeViewer = new TreeViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

        // Set the content provider
        treeViewer.setContentProvider(new MyContentProvider());

        // Set the label provider
        treeViewer.setLabelProvider(new MyLabelProvider());

        // Set any additional configuration options for the MainView
        // For example:
        // MainView.setAutoExpandLevel(2);
        // MainView.setInput(...);

        // Set the MainView as the control for the view
        this.setPartName("My Custom View");
        //this.setTitleImage(Activator.getImageDescriptor("path/to/icon.png").createImage());
        
        //Old, did not seem to work
        //this.setPartControl(container);
        
        treeViewer.setInput(getInitialInput());
        getSite().setSelectionProvider(treeViewer);
    }

    @Override
    public void setFocus() {
        // Set focus to the MainView or any other control that requires focus
    	treeViewer.getControl().setFocus();
    }
    
    private Object getInitialInput() {
    	
    	Project rootProject = new Project("Project 1");
    	
        MyObject root1 = new MyObject("Root 1");
        MyObject child11 = new MyObject("Child 1-1");
        MyObject child12 = new MyObject("Child 1-2");
        root1.addChild(child11);
        root1.addChild(child12);

        MyObject root2 = new MyObject("Root 2");
        MyObject child21 = new MyObject("Child 2-1");
        root2.addChild(child21);
        
        rootProject.addChild(root1);
        rootProject.addChild(root2);
        RootManager.getInstance().addChild(rootProject);
        return RootManager.getInstance();
        //return rootProject;
    }
    

}

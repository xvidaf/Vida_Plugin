package views;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import actions.CreateObject;
import actions.CreateProject;
import actions.RefreshTree;
import actions.RemoveObject;
import models.DragSourceListenerAdapter;
import models.DropSourceListenerAdapter;
import models.Element;
import models.Final;
import models.Initial;
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
        
        // Set the MainView as the control for the view
        this.setPartName("My Custom View");
        //this.setTitleImage(Activator.getImageDescriptor("path/to/icon.png").createImage());
        
        createContextMenu();
        
        DragSource dragSource = new DragSource(treeViewer.getControl(), DND.DROP_MOVE);
        dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
        dragSource.addDragListener(new DragSourceListenerAdapter(this.treeViewer));
        
        DropTarget target = new DropTarget(treeViewer.getControl(), DND.DROP_MOVE);
        target.setTransfer(new Transfer[] { TextTransfer.getInstance() });
        target.addDropListener(new DropSourceListenerAdapter(this.treeViewer));
        
        
        Sorter sorter = new Sorter();
   
        treeViewer.setInput(getInitialInput());
        treeViewer.setComparator(sorter);
        getSite().setSelectionProvider(treeViewer);
    }

    @Override
    public void setFocus() {
        // Set focus to the MainView or any other control that requires focus
    	treeViewer.getControl().setFocus();
    }
    
	private void fillContextMenu(IMenuManager menuManager, Action createProject, Action createObject, Action refreshTree, Action removeObject) {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        Object selectedElement = selection.getFirstElement();
        
        if(selectedElement instanceof Project) {
            MenuManager newMenuManager = new MenuManager("New");
            menuManager.add(newMenuManager);
            newMenuManager.add(createObject);
        } else if (selectedElement instanceof MyObject) {
            MenuManager newMenuManager = new MenuManager("New");
            menuManager.add(newMenuManager);
            newMenuManager.add(createObject);
        }
        menuManager.add(createProject);
        if(selectedElement instanceof Element) {
        	menuManager.add(removeObject);
        }
        menuManager.add(refreshTree);
	}
	
	private void createContextMenu() {
		MenuManager menuManager = new MenuManager();
        Menu menu = menuManager.createContextMenu(treeViewer.getControl());
        treeViewer.getControl().setMenu(menu);
        
        getSite().registerContextMenu(menuManager, treeViewer);
        
        CreateProject createProject = new CreateProject(treeViewer);
        CreateObject createObject = new CreateObject(treeViewer);
        RefreshTree refreshTree = new RefreshTree(treeViewer);
        RemoveObject removeObject = new RemoveObject(treeViewer);

        menuManager.setRemoveAllWhenShown(true);
        
        menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager menuManager) {
				fillContextMenu(menuManager, createProject, createObject, refreshTree, removeObject);
			}
		});
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
        
        Initial initialNode = new Initial("I should be first");
        Final finalNode = new Final("I should be last");
        
        rootProject.addChild(root1);
        rootProject.addChild(root2);
        rootProject.addChild(initialNode);
        rootProject.addChild(finalNode);
        RootManager.getInstance().addChild(rootProject);
        return RootManager.getInstance();
        //return rootProject;
    }

    
    

}

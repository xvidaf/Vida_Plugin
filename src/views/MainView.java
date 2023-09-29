package views;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Base64;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
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
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;

import actions.CreateClass;
import actions.CreateMethod;
import actions.CreateObject;
import actions.CreateProject;
import actions.RefreshTree;
import actions.RefreshOpenedProjects;
import actions.DeleteObject;
import actions.ImportFromEA;
import actions.OpenSettings;
import actions.Properties;
import models.DoubleClickListenerAdapter;
import models.DragSourceListenerAdapter;
import models.DropSourceListenerAdapter;
import models.Element;
import models.Final;
import models.Initial;
import models.ActivityDiagram;
import models.OpenedProjects;
import models.Project;
import models.RootManager;
import models.Settings;

public class MainView extends org.eclipse.ui.part.ViewPart {

	private TreeViewer treeViewer;

	private IMemento memento;

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
		// this.setTitleImage(Activator.getImageDescriptor("path/to/icon.png").createImage());

		createContextMenu();

		DragSource dragSource = new DragSource(treeViewer.getControl(), DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceListenerAdapter(this.treeViewer));

		DropTarget target = new DropTarget(treeViewer.getControl(), DND.DROP_MOVE);
		target.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		target.addDropListener(new DropSourceListenerAdapter(this.treeViewer));
		
		treeViewer.addDoubleClickListener(new DoubleClickListenerAdapter(this.treeViewer));

		// Get the toolbar manager of the TreeViewer
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();

		// Add action to the toolbar manager
		RefreshOpenedProjects refreshButton = new RefreshOpenedProjects(treeViewer);
		toolbarManager.add(refreshButton);
		
		OpenSettings openSettings = new OpenSettings();
		toolbarManager.add(openSettings);

		Sorter sorter = new Sorter();

		treeViewer.setComparator(sorter);
		
		refreshButton.run();

		if (this.restoreState() == false) {
			treeViewer.setInput(getInitialInput());
		}

		getSite().setSelectionProvider(treeViewer);
	}

	public void saveState(IMemento memento) {
		try {
			//Save RootManager
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bos);
			os.writeObject(RootManager.getInstance());
			String serialized_root = Base64.getEncoder().encodeToString(bos.toByteArray());
			
			//Save settings
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			os.writeObject(Settings.getInstance());
			String serialized_settings = Base64.getEncoder().encodeToString(bos.toByteArray());
			
			//close streams
			os.close();
			bos.close();
			
			memento.putString("RootManager", serialized_root);
			memento.putString("Settings", serialized_settings);
			memento.putString("SettingsProject", Settings.getInstance().getDesignatedMainProject().getElementName());
			
			
			//Save rootmanager to a file, to ensure git compatability
			//get object which represents the workspace  
			IWorkspace workspace = ResourcesPlugin.getWorkspace();  

			//get location of workspace (java.io.File)  
			File workspaceDirectory = workspace.getRoot().getLocation().toFile();
			
			if(Settings.getInstance().getDesignatedMainProject() != null) {
				try (PrintWriter out = new PrintWriter(workspaceDirectory.getAbsolutePath() + Settings.getInstance().getDesignatedMainProject().getPath() + "/plugin_state")) {
					try (OutputStream stream = new FileOutputStream(workspaceDirectory.getAbsolutePath() + Settings.getInstance().getDesignatedMainProject().getPath() + "/plugin_state")) {
						os = new ObjectOutputStream(stream);
						os.writeObject(RootManager.getInstance());
					}
				}
			}
			

		} catch (Exception ex) {
			memento = null;
		}
	}

	public void init(IViewSite site, IMemento memento) throws PartInitException {
		init(site);
		this.memento = memento;
	}

	private boolean restoreState() {
		if (this.memento == null) {
			return false;
		}

		try {
			//Restore RootManager
			final byte[] data = Base64.getDecoder().decode(memento.getString("RootManager"));
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInputStream oInputStream = new ObjectInputStream(bis);
			RootManager restoredRoot = (RootManager) oInputStream.readObject();
			RootManager.getInstance().replaceInstance(restoredRoot);
			
			OpenedProjects.getInstance().refreshReferences();
			
			//Restore Settings
			final byte[] dataSettings = Base64.getDecoder().decode(memento.getString("Settings"));
			bis = new ByteArrayInputStream(dataSettings);
			oInputStream = new ObjectInputStream(bis);
			Settings restoredSettings = (Settings) oInputStream.readObject();
			Settings.getInstance().replaceInstance(restoredSettings);
			//Restore designated project
			Settings.getInstance().restoredesignatedMainProject(memento.getString("SettingsProject"));
			
			oInputStream.close();
			bis.close();
			memento = null;
			
			//If a state file exists, restore it
			
			//get object which represents the workspace  
			IWorkspace workspace = ResourcesPlugin.getWorkspace();  

			//get location of workspace (java.io.File)  
			File workspaceDirectory = workspace.getRoot().getLocation().toFile();
			
			if(Settings.getInstance().getDesignatedMainProject() != null) {
				//If exists
				if(new File(workspaceDirectory.getAbsolutePath() + Settings.getInstance().getDesignatedMainProject().getPath() + "/plugin_state").isFile()) {
					FileInputStream fi = new FileInputStream(new File(workspaceDirectory.getAbsolutePath() + Settings.getInstance().getDesignatedMainProject().getPath() + "/plugin_state"));
					oInputStream = new ObjectInputStream(fi);
					RootManager restoredRootFromFile = (RootManager) oInputStream.readObject();
					RootManager.getInstance().replaceInstance(restoredRootFromFile);
				}
			}
			
			treeViewer.setInput(RootManager.getInstance());
			return true;
		} catch (Exception ex) {
			memento = null;
			return false;
		}
	}

	@Override
	public void setFocus() {
		// Set focus to the MainView or any other control that requires focus
		treeViewer.getControl().setFocus();
	}

	private void fillContextMenu(IMenuManager menuManager, Action createProject, Action createObject,
			Action refreshTree, Action removeObject, Action properties, Action createClass, Action createMethod, Action importFromEa) {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		Object selectedElement = selection.getFirstElement();

		if (selectedElement instanceof Project) {
			MenuManager newMenuManager = new MenuManager("New");
			menuManager.add(newMenuManager);
			newMenuManager.add(createObject);
			menuManager.add(importFromEa);
		} else if (selectedElement instanceof ActivityDiagram || selectedElement instanceof Action) {
			MenuManager newMenuManager = new MenuManager("New");
			menuManager.add(newMenuManager);
			newMenuManager.add(createObject);
			newMenuManager.add(createClass);
			newMenuManager.add(createMethod);
		}
		menuManager.add(createProject);
		if (selectedElement instanceof Element) {
			menuManager.add(removeObject);
			menuManager.add(properties);
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
		DeleteObject removeObject = new DeleteObject(treeViewer);
		Properties properties = new Properties(treeViewer);
		CreateClass createClass = new CreateClass(treeViewer);
		CreateMethod createMethod = new CreateMethod(treeViewer);
		ImportFromEA importFromEa = new ImportFromEA(treeViewer);

		menuManager.setRemoveAllWhenShown(true);

		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager menuManager) {
				fillContextMenu(menuManager, createProject, createObject, refreshTree, removeObject, properties, createClass, createMethod, importFromEa);
			}
		});
	}

	private Object getInitialInput() {

		Project rootProject = new Project("Project 1");

		ActivityDiagram root1 = new ActivityDiagram("Root 1");
		ActivityDiagram child11 = new ActivityDiagram("Child 1-1");
		ActivityDiagram child12 = new ActivityDiagram("Child 1-2");
		root1.addChild(child11);
		root1.addChild(child12);

		ActivityDiagram root2 = new ActivityDiagram("Root 2");
		ActivityDiagram child21 = new ActivityDiagram("Child 2-1");
		root2.addChild(child21);

		Initial initialNode = new Initial("I should be first");
		Final finalNode = new Final("I should be last");

		rootProject.addChild(root1);
		rootProject.addChild(root2);
		rootProject.addChild(initialNode);
		rootProject.addChild(finalNode);
		RootManager.getInstance().addChild(rootProject);
		return RootManager.getInstance();
		// return rootProject;
	}

}

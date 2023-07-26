package views;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

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
import actions.Properties;
import models.DoubleClickListenerAdapter;
import models.DragSourceListenerAdapter;
import models.DropSourceListenerAdapter;
import models.Element;
import models.Final;
import models.Initial;
import models.MyObject;
import models.OpenedProjects;
import models.Project;
import models.RootManager;

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

		// Add your custom action to the toolbar manager
		RefreshOpenedProjects refreshButton = new RefreshOpenedProjects(treeViewer);
		toolbarManager.add(refreshButton);

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
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bos);
			os.writeObject(RootManager.getInstance());
			String serialized_patches1 = Base64.getEncoder().encodeToString(bos.toByteArray());
			os.close();

			memento.putString("RootManager", serialized_patches1);

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
			final byte[] data = Base64.getDecoder().decode(memento.getString("RootManager"));
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInputStream oInputStream = new ObjectInputStream(bis);
			RootManager restored_root = (RootManager) oInputStream.readObject();
			oInputStream.close();
			RootManager.getInstance().replaceInstance(restored_root);
			memento = null;
			OpenedProjects.getInstance().refreshReferences();
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
			Action refreshTree, Action removeObject, Action properties, Action createClass, Action createMethod) {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		Object selectedElement = selection.getFirstElement();

		if (selectedElement instanceof Project) {
			MenuManager newMenuManager = new MenuManager("New");
			menuManager.add(newMenuManager);
			newMenuManager.add(createObject);
		} else if (selectedElement instanceof MyObject) {
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

		menuManager.setRemoveAllWhenShown(true);

		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager menuManager) {
				fillContextMenu(menuManager, createProject, createObject, refreshTree, removeObject, properties, createClass, createMethod);
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
		// return rootProject;
	}

}

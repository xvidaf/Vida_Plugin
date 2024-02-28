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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;

import actions.CreateAction;
import actions.CreateActivityDiagram;
import actions.CreateClass;
import actions.CreateForkJoin;
import actions.CreateGenericFile;
import actions.CreateMethod;
import actions.CreateObject;
import actions.CreateParallelPath;
import actions.CreateProject;
import actions.CreateVariable;
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
import models.ForkJoin;
import models.ImageFinder;
import models.Initial;
import models.ActivityDiagram;
import models.OpenedProjects;
import models.ParallelPath;
import models.Project;
import models.RootManager;
import models.Settings;
import models.UMLAction;

public class MainView extends org.eclipse.ui.part.ViewPart {

	private TreeViewer treeViewer;

	private IMemento memento;
	
	@Override
	public void createPartControl(Composite parent) {
		// Create a composite as the container for the MainView
		Composite container = new Composite(parent, SWT.NONE);
		//container.setLayout(new FillLayout());
		container.setLayout(new GridLayout(1, false));
		
		// Create the MainView
		treeViewer = new TreeViewer(container, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		
	    GridData treeViewerLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
	    treeViewerLayoutData.verticalSpan = 75; // Span 1 column
	    treeViewer.getControl().setLayoutData(treeViewerLayoutData);

		// Set the content provider
		treeViewer.setContentProvider(new MyContentProvider());

		// Set the label provider
		treeViewer.setLabelProvider(new MyLabelProvider());
		
        Composite comboArea = new Composite(container, SWT.CENTER);
        comboArea.setLayout(new GridLayout(2, false));
        comboArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Label lbtProjectName = new Label(comboArea, SWT.TOP);
        lbtProjectName.setText("Displayed Image:");
        Combo selectImage = new Combo(comboArea, SWT.BORDER | SWT.READ_ONLY);
        
		// Create a ScrolledComposite as a placeholder for the ImageViewer
	    ScrolledComposite scrolledComposite = new ScrolledComposite(comboArea, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	    GridData scrolledLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
	    scrolledLayoutData.horizontalSpan = 2;
	    scrolledComposite.setLayoutData(scrolledLayoutData);
	    
	    // Create a composite to hold the image
	    Composite imageComposite = new Composite(scrolledComposite, SWT.CENTER);
	    imageComposite.setLayout(new GridLayout(1, false));

	    // Load an image (replace "path/to/your/image.png" with the actual path to your image)
	    Image image = new Image(null, "C:\\Users\\vidaf\\Desktop\\53b04264-831d-4940-a193-da0ac2c22706.png");

	    // Create a Label to display the image
	    Label imageLabel = new Label(imageComposite, SWT.CENTER);
	    imageLabel.setImage(null);
	    
	    // Set the size of the Label to the size of the image
	    imageLabel.setSize(image.getBounds().width, image.getBounds().height);

	    // Set the content of the ScrolledComposite
	    scrolledComposite.setContent(imageComposite);

	    // Set the minimum size for scrolling
	    scrolledComposite.setMinSize(imageComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	    // Enable automatic scrolling
	    scrolledComposite.setExpandHorizontal(true);
	    scrolledComposite.setExpandVertical(true);
		
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
		
		ImageFinder.getInstance().setPathToImageDir();
        
		if(ImageFinder.getInstance().getImages() != null) {
	        for(File newImage : ImageFinder.getInstance().getImages()) {
	        	selectImage.add(newImage.getName());
	        }
		}
        selectImage.add("None");
		
        selectImage.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(final SelectionEvent event)
          {           
            if(ImageFinder.getInstance().getImages() != null) {
            	if(!ImageFinder.getInstance().getPathToImageByName(selectImage.getText()).equals("None")) {
            		Image newImage = new Image(null, ImageFinder.getInstance().getPathToImageByName(selectImage.getText()));
            		imageLabel.setSize(newImage.getBounds().width, newImage.getBounds().height);
            		imageLabel.setImage(newImage); 
            	    scrolledComposite.setContent(imageComposite);
            	    scrolledComposite.setSize(newImage.getBounds().width, newImage.getBounds().height);
            	    comboArea.setSize(newImage.getBounds().width, newImage.getBounds().height);
            	    scrolledComposite.setMinSize(imageComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
            	    scrolledComposite.setExpandHorizontal(true);
            	    scrolledComposite.setExpandVertical(true);
            	} else {
            		imageLabel.setImage(null);    
            	}   
            }else {
            	imageLabel.setImage(null);  
            }
          }
        });
		
	}

	public void saveState(IMemento memento) {
		try {
			//TODO file saving seems to be broken
			// Save RootManager
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bos);
			os.writeObject(RootManager.getInstance());
			String serialized_root = Base64.getEncoder().encodeToString(bos.toByteArray());

			// Save settings
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			os.writeObject(Settings.getInstance());
			String serialized_settings = Base64.getEncoder().encodeToString(bos.toByteArray());

			// close streams
			os.close();
			bos.close();

			memento.putString("RootManager", serialized_root);
			memento.putString("Settings", serialized_settings);
			memento.putString("SettingsProject", Settings.getInstance().getDesignatedMainProject().getElementName());

			// Save rootmanager to a file, to ensure git compatability
			// get object which represents the workspace
			IWorkspace workspace = ResourcesPlugin.getWorkspace();

			// get location of workspace (java.io.File)
			File workspaceDirectory = workspace.getRoot().getLocation().toFile();

			if (Settings.getInstance().getDesignatedMainProject() != null) {
				try (PrintWriter out = new PrintWriter(workspaceDirectory.getAbsolutePath()
						+ Settings.getInstance().getDesignatedMainProject().getPath() + "/plugin_state")) {
					try (OutputStream stream = new FileOutputStream(workspaceDirectory.getAbsolutePath()
							+ Settings.getInstance().getDesignatedMainProject().getPath() + "/plugin_state")) {
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
			// Restore RootManager
			final byte[] data = Base64.getDecoder().decode(memento.getString("RootManager"));
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInputStream oInputStream = new ObjectInputStream(bis);
			RootManager restoredRoot = (RootManager) oInputStream.readObject();
			RootManager.getInstance().replaceInstance(restoredRoot);

			OpenedProjects.getInstance().refreshReferences();

			// Restore Settings
			final byte[] dataSettings = Base64.getDecoder().decode(memento.getString("Settings"));
			bis = new ByteArrayInputStream(dataSettings);
			oInputStream = new ObjectInputStream(bis);
			Settings restoredSettings = (Settings) oInputStream.readObject();
			Settings.getInstance().replaceInstance(restoredSettings);
			// Restore designated project
			Settings.getInstance().restoredesignatedMainProject(memento.getString("SettingsProject"));

			oInputStream.close();
			bis.close();
			memento = null;

			// If a state file exists, restore it

			// get object which represents the workspace
			IWorkspace workspace = ResourcesPlugin.getWorkspace();

			// get location of workspace (java.io.File)
			File workspaceDirectory = workspace.getRoot().getLocation().toFile();

			if (Settings.getInstance().getDesignatedMainProject() != null) {
				// If exists
				if (new File(workspaceDirectory.getAbsolutePath()
						+ Settings.getInstance().getDesignatedMainProject().getPath() + "/plugin_state").isFile()) {
					FileInputStream fi = new FileInputStream(new File(workspaceDirectory.getAbsolutePath()
							+ Settings.getInstance().getDesignatedMainProject().getPath() + "/plugin_state"));
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
			Action refreshTree, Action removeObject, Action properties, Action createClass, Action createMethod,
			Action importFromEa, Action createVariable, Action createFile, Action createAction, Action createForkJoin, Action createParallelPath, Action createADAction) {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		Object selectedElement = selection.getFirstElement();

		if (selectedElement instanceof Project) {
			MenuManager newMenuManager = new MenuManager("New");
			menuManager.add(newMenuManager);
			newMenuManager.add(createADAction);
			newMenuManager.add(createObject);
			menuManager.add(importFromEa);
		} else if (selectedElement instanceof ActivityDiagram || selectedElement instanceof ParallelPath) {
			MenuManager newMenuManager = new MenuManager("New");
			menuManager.add(newMenuManager);
			newMenuManager.add(createAction);
			newMenuManager.add(createObject);
			newMenuManager.add(createClass);
			newMenuManager.add(createMethod);
			newMenuManager.add(createVariable);
			newMenuManager.add(createFile);
			newMenuManager.add(createForkJoin);
		} else if (selectedElement instanceof UMLAction) {
			MenuManager newMenuManager = new MenuManager("New");
			menuManager.add(newMenuManager);
			newMenuManager.add(createObject);
			newMenuManager.add(createClass);
			newMenuManager.add(createMethod);
			newMenuManager.add(createVariable);
			newMenuManager.add(createFile);
		} else if (selectedElement instanceof ForkJoin) {
			MenuManager newMenuManager = new MenuManager("New");
			menuManager.add(newMenuManager);
			newMenuManager.add(createParallelPath);
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
		CreateVariable createVariable = new CreateVariable(treeViewer);
		CreateGenericFile createFile = new CreateGenericFile(treeViewer);
		CreateAction createAction = new CreateAction(treeViewer);
		CreateForkJoin createForkJoin = new CreateForkJoin(treeViewer);
		CreateParallelPath createParallelAction = new CreateParallelPath(treeViewer);
		CreateActivityDiagram createADAction = new CreateActivityDiagram(treeViewer);

		menuManager.setRemoveAllWhenShown(true);

		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager menuManager) {
				fillContextMenu(menuManager, createProject, createObject, refreshTree, removeObject, properties,
						createClass, createMethod, importFromEa, createVariable, createFile, createAction, createForkJoin, createParallelAction, createADAction);
			}
		});
	}

	private Object getInitialInput() {

		Initial initialNode = new Initial("Start Node");
		Final finalNode = new Final("Final Node");

		Project rootProject = new Project("Evaluation");
		ActivityDiagram orderRoot = new ActivityDiagram("Order");
		UMLAction child11_1 = new UMLAction("Display Catalog List", 1);
		UMLAction child11_2 = new UMLAction("Request Catalog Content", 2);
		UMLAction child11_3 = new UMLAction("Display Product List", 3);
		UMLAction child11_4 = new UMLAction("Select Product", 4);
		UMLAction child11_5 = new UMLAction("Request Product Info", 5);
		UMLAction child11_6 = new UMLAction("Display Product Info", 6);
		UMLAction child11_7 = new UMLAction("Input Quality and Confirm Order", 7);
		UMLAction child11_8 = new UMLAction("Add product to cart", 8);
		UMLAction child11_9 = new UMLAction("Calculate Subtotal", 9);
		UMLAction child11_10 = new UMLAction("Display Cart", 10);

		orderRoot.addChild(child11_1);
		orderRoot.addChild(child11_2);
		orderRoot.addChild(child11_3);
		orderRoot.addChild(child11_4);
		orderRoot.addChild(child11_5);
		orderRoot.addChild(child11_6);
		orderRoot.addChild(child11_7);
		orderRoot.addChild(child11_8);
		orderRoot.addChild(child11_9);
		orderRoot.addChild(child11_10);
		orderRoot.addChild(initialNode);
		orderRoot.addChild(finalNode);

		ActivityDiagram loginRoot = new ActivityDiagram("Log In");
		UMLAction child11 = new UMLAction("Display Login Form", 1);
		UMLAction child12 = new UMLAction("Input credentials", 2);
		UMLAction child13 = new UMLAction("Authenticate", 3);
		UMLAction child14 = new UMLAction("Display Main Page", 4);

		rootProject.addChild(loginRoot);
		loginRoot.addChild(child11);
		loginRoot.addChild(child12);
		loginRoot.addChild(child13);
		loginRoot.addChild(child14);
		loginRoot.addChild(initialNode);
		loginRoot.addChild(finalNode);
		
		Initial initialNodePar = new Initial("StartNode");
		Final finalNodePar = new Final("FinalNode");
		
		ActivityDiagram parRoot = new ActivityDiagram("Parallel Demonstration");
		UMLAction par_child11_1 = new UMLAction("Login", 1);
		ForkJoin par_child11_2 = new ForkJoin("Fork/Join1", 2);
		ParallelPath par_child11_3 = new ParallelPath("Path1.1", 5);
		ParallelPath par_child11_4 = new ParallelPath("Path1.2", 6);
		UMLAction par_child11_5 = new UMLAction("Chat", 3);
		UMLAction par_child11_6 = new UMLAction("Play", 4);
		ForkJoin par_child11_7 = new ForkJoin("Fork/Join2", 5);
		ParallelPath par_child11_8 = new ParallelPath("Path2.1", 2);
		ParallelPath par_child11_9 = new ParallelPath("Path2.2", 3);
		UMLAction par_child11_10 = new UMLAction("Defeat Monsters", 6);
		UMLAction par_child11_11 = new UMLAction("Defend Innocents", 7);
		UMLAction par_child11_12 = new UMLAction("Logout", 8);
		
		
		parRoot.addChild(initialNodePar);
		parRoot.addChild(finalNodePar);
		parRoot.addChild(par_child11_1);
		parRoot.addChild(par_child11_2);
		par_child11_2.addChild(par_child11_3);
		par_child11_2.addChild(par_child11_4);
		par_child11_3.addChild(par_child11_5);
		par_child11_4.addChild(par_child11_6);
		par_child11_4.addChild(par_child11_7);
		par_child11_7.addChild(par_child11_8);
		par_child11_7.addChild(par_child11_9);
		par_child11_8.addChild(par_child11_10);
		par_child11_9.addChild(par_child11_11);
		parRoot.addChild(par_child11_12);

		
		rootProject.addChild(orderRoot);
		rootProject.addChild(initialNode);
		rootProject.addChild(finalNode);
		rootProject.addChild(parRoot);
		RootManager.getInstance().addChild(rootProject);
		return RootManager.getInstance();
		// return rootProject;
	}

}

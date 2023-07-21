package actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

public class RefreshTree extends Action{
	
	private TreeViewer treeViewer;

    public RefreshTree(TreeViewer treeViewer) {
        super("Refresh");
        this.treeViewer = treeViewer;
    }
	
    @Override
    public void run() {
        treeViewer.refresh();
    }
}

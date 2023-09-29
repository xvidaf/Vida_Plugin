package models;

import org.eclipse.jdt.core.IMethod;

public class Method extends Element{

	private static final long serialVersionUID = 1007632003230368473L;
	
	private transient IMethod referencedMethod;
	
	public Method(IMethod referencedMethod, int sortOrder) {
		super(OpenedProjects.getInstance().getMethodFullName(referencedMethod), sortOrder);
		this.setReferencedMethod(referencedMethod);
	}
	
	public Method(IMethod referencedMethod) {
		super(OpenedProjects.getInstance().getMethodFullName(referencedMethod));
		this.setReferencedMethod(referencedMethod);
	}

	public IMethod getReferencedMethod() {
		return referencedMethod;
	}

	public void setReferencedMethod(IMethod referencedMethod) {
		this.referencedMethod = referencedMethod;
	}
	
	public void updateReferencedClass() {
		this.referencedMethod = OpenedProjects.getInstance().findMethodByFullyQualifiedName(this.getName());
	}


}

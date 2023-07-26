package models;

import org.eclipse.jdt.core.IType;

public class Class extends Element{

	private static final long serialVersionUID = 5593793495309182681L;
	
	private transient IType referencedClass;
	
	public Class(IType referencedClass) {
		super(referencedClass.getFullyQualifiedName());
		this.setReferencedClass(referencedClass);
	}
	
	public Class(IType referencedClass, int sortOrder) {
		super(referencedClass.getFullyQualifiedName(), sortOrder);
		this.setReferencedClass(referencedClass);
	}

	public IType getReferencedClass() {
		return referencedClass;
	}

	public void setReferencedClass(IType referencedClass) {
		this.referencedClass = referencedClass;
	}
	
	public void updateReferencedClass() {
		this.referencedClass = OpenedProjects.getInstance().findClassByFullyQualifiedName(this.getName());
	}

}

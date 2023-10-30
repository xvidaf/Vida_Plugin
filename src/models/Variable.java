package models;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class Variable extends Element{

	private static final long serialVersionUID = 8414151239845500319L;
	
	private transient IField referencedVariable;
	
	private transient IType referencedClass;
	
	public Variable(IField referencedVariable, IType selectedClass) throws JavaModelException {
		super(selectedClass.getFullyQualifiedName() + " " + referencedVariable.getTypeSignature() + " " + referencedVariable.getElementName());
		this.setReferencedVariable(referencedVariable);
		this.setReferencedClass(selectedClass);
	}
	
	public Variable(IField referencedVariable, IType selectedClass, int sortOrder) throws JavaModelException {
		super(selectedClass.getFullyQualifiedName() + " " + referencedVariable.getTypeSignature() + " " + referencedVariable.getElementName(), sortOrder);
		this.setReferencedVariable(referencedVariable);
		this.setReferencedClass(selectedClass);
	}

	public IField getReferencedVariable() {
		return referencedVariable;
	}
	
	public IType getReferencedClass() {
		return referencedClass;
	}

	public void setReferencedVariable(IField referencedVariable) {
		this.referencedVariable = referencedVariable;
	}
	
	public void setReferencedClass(IType referencedClass) {
		this.referencedClass = referencedClass;
	}
	
	public void updateReferencedVariable() {
		for(IType selectedClass : OpenedProjects.getInstance().getClasses()) {       	
        	try {
        		IField[] fields = selectedClass.getFields();
        	    for (IField field : fields) {
        	        if(this.getName().equals(selectedClass.getFullyQualifiedName() + " " + field.getTypeSignature() + " " + field.getElementName())) {
        	        	this.referencedVariable = field;
        	        	break;
        	        }
        	    }
        	} catch (JavaModelException e) {
        	    e.printStackTrace();
        	}
        }
	}
	
	public void updateReferencedClass() {
		this.referencedClass = OpenedProjects.getInstance().findClassByFullyQualifiedName(this.getName());
	}

}

package utilities;

import java.util.HashMap;
import java.util.List;

public class XmlObject {
	private String ObjectID;
	private String sourceID;
	private String targetID;
	private String type;
	private String name;
	private int orderNum;
	private int activityTypeIndex;
	private String description;
	private String owner;
	private HashMap tags;
	private List classes;
	
	public XmlObject(String ObjectID, String type, String name, String desc, int orderNum, String owner){
		this.ObjectID = ObjectID;
		this.setType(type);
		this.setActivityTypeIndex(this.type);
		this.name = name;
		if((this.name == null) || this.name.isEmpty()){
			this.name = getType();
		}
		this.description = desc;
		this.orderNum = orderNum;
		this.owner = owner;
	}
	
	public XmlObject(String ObjectID, String type, String name, String desc, int orderNum, String owner, HashMap tags, List classes){
		this.ObjectID = ObjectID;
		this.setType(type);
		this.setActivityTypeIndex(this.type);
		this.name = name;
		if((this.name == null) || this.name.isEmpty()){
			this.name = getType();
		}
		this.description = desc;
		this.orderNum = orderNum;
		this.owner = owner;
		this.tags = tags;
		this.classes = classes;
	}
	
	public XmlObject(String ObjectID, String type, String source, String target){
		this.ObjectID = ObjectID;
		this.type = type;
		this.sourceID = source;
		this.targetID = target;
	}
	
	
	public String getObjectID() {
		return ObjectID;
	}
	public void setObjectID(String iD) {
		ObjectID = iD;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		if(type.contains("uml:Action")){
			this.type = "action";
			return;
		}
		else if(type.contains("uml:Decision")){
			this.type = "decision";
			return ;
		}
		else if(type.contains("uml:Merge")){
			this.type = "merge";
			return ;
		}
		else if(type.contains("uml:Fork")){
			this.type = "fork";
			return ;
		}
		else if(type.contains("uml:Join")){
			this.type = "join";
			return ;
		}
		else if(type.contains("uml:InitialNode")){
			this.type = "initial node";
			return;
		}
		else if(type.contains("uml:ActivityFinalNode")){
			this.type = "final node";
			return;
		}
		else if(type.contains("uml:Object")){
			this.type = "object";
			return;
		}
		else{
			this.type = type;
			return;
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSourceID() {
		return sourceID;
	}

	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	public String getTargetID() {
		return targetID;
	}

	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descrition) {
		this.description = descrition;
	}

	public int getActivityTypeIndex() {
		return activityTypeIndex;
	}
	
	public void setActivityTypeIndex(String type) {
		if(type.contains("action")){
			this.activityTypeIndex = 0;
		}
		if(type.contains("action")){
			this.activityTypeIndex = 1;
		}
		else if(type.contains("decision")){;
			this.activityTypeIndex = 2;
		}
		else if(type.contains("merge")){
			this.activityTypeIndex = 3;
		}
		else if(type.contains("fork")){
			this.activityTypeIndex = 4;
		}
		else if(type.contains("join")){
			this.activityTypeIndex = 5;
		}
		else if(type.contains("initial node")){
			this.activityTypeIndex = 6;
		}
		else if(type.contains("final node")){
			this.activityTypeIndex = 7;
		}
		else if(type.contains("terminal node")){
			this.activityTypeIndex = 8;
		}
		else if(type.contains("object")){
			this.activityTypeIndex = 9;
		}
		else if(type.contains("structured activity")){
			this.activityTypeIndex = 10;
		}
	}

	public void setActivityTypeIndex(int activityTypeIndex) {
		this.activityTypeIndex = activityTypeIndex;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public HashMap getTags() {
		return tags;
	}

	public void setTags(HashMap tags) {
		this.tags = tags;
	}

	public List getClasses() {
		return classes;
	}

	public void setClasses(List classes) {
		this.classes = classes;
	}
	
	

}

package utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import models.Action;
import models.ActivityDiagram;
import models.Project;

public class EAXMLReader {
	private static final String Activity = null;
	private Document xmldoc = null;
	private File fXmlFile;
	private ActivityDiagram activityDiagram;
	private Project selectedProject;
	private List<XmlObject> nodeList;
	private List<XmlObject> edgeList;
	private XmlObject initNode;
	private XmlObject initEdge;
	private HashMap<String, XmlObject> nodeMap;
	private HashMap<String, String> classMap;
	private HashMap<String, XmlObject> swimlaneMap;
	
	public EAXMLReader(Project activityDiagramViewObject, String path){
		fXmlFile = new File(path);
		this.selectedProject = activityDiagramViewObject;
		this.activityDiagram = new ActivityDiagram("importedAD");
		this.selectedProject.addChild(activityDiagram);
		this.activityDiagram.setParent(selectedProject);
	}
	
	public void createStructure() throws ParserConfigurationException, SAXException, IOException{
		if(fXmlFile.exists()){
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			xmldoc = dBuilder.parse(fXmlFile);
			xmldoc.getDocumentElement().normalize();
			nodeList = new ArrayList<XmlObject>();
			edgeList = new ArrayList<XmlObject>();
			nodeMap = new HashMap<String, XmlObject>();
			swimlaneMap = new HashMap<String, XmlObject>();
			classMap = new HashMap<String, String>();
			NodeList nList;
			
			nList = xmldoc.getElementsByTagName("uml:Model");
			getADName(nList);
			nList = xmldoc.getElementsByTagName("node");
			craeteNodes(nList);
			nList = xmldoc.getElementsByTagName("source");
			createClasses(nList);
			nList = xmldoc.getElementsByTagName("group");
			createSwimLanes(nList);
			nList = xmldoc.getElementsByTagName("element");
			detailedNodes(nList);

			System.out.print(this.activityDiagram.getName());
			
			//createADElements();
		}
	}
	
	private void getADName(NodeList nList){
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;		
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				if(eElement.getElementsByTagName("packagedElement").item(0).getAttributes().getNamedItem("name") != null){
					String name = eElement.getElementsByTagName("packagedElement").item(0).getAttributes().getNamedItem("name").getNodeValue();
					this.activityDiagram.setName(name);
				}
			}
		}
	}
		
	private void craeteNodes(NodeList nList){
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;		
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				String id = eElement.getAttribute("xmi:id");
				String type = eElement.getAttribute("xmi:type");
				String name = eElement.getAttribute("name");
				String desc = "";
				if(type.contains("uml")){
					XmlObject xmlO = new XmlObject(id, type, name, desc, 0, "");
					nodeMap.put(id, xmlO);
				}   
			}
		}
	}
	
	private void detailedNodes(NodeList nList){
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;		
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				String id = eElement.getAttribute("xmi:idref");
				String type = eElement.getAttribute("xmi:type");
				String name = eElement.getAttribute("name");
				String desc = "";
				String owner = "";
				String className = "";
				int orderNum = 0;
				HashMap<String, String> tags = new HashMap<String, String>();
				List<String> classes = new ArrayList<String>();
				
				
				if(type.contains("uml")){
					if(eElement.getElementsByTagName("model").item(0).getAttributes().getNamedItem("owner") != null){
						String owner_id = eElement.getElementsByTagName("model").item(0).getAttributes().getNamedItem("owner").getNodeValue();
						owner = swimlaneMap.get(owner_id).getName();
					}
					if(eElement.getElementsByTagName("properties").item(0).getAttributes().getNamedItem("documentation") != null){
						desc = eElement.getElementsByTagName("properties").item(0).getAttributes().getNamedItem("documentation").getNodeValue();
					}
					if(eElement.getElementsByTagName("tag").getLength() != 0){
						for(int i = 0; i < eElement.getElementsByTagName("tag").getLength(); i++){
							 String tagName = eElement.getElementsByTagName("tag").item(i).getAttributes().getNamedItem("name").getNodeValue();
							 
							 if(tagName.contains("Order num")){
								 orderNum = Integer.parseInt(eElement.getElementsByTagName("tag").item(i).getAttributes().getNamedItem("value").getNodeValue());
							 }
							 else{
								 tags.put(tagName, eElement.getElementsByTagName("tag").item(i).getAttributes().getNamedItem("value").getNodeValue());
							 }
						}
					}
					
					if((eElement.getElementsByTagName("Dependency") !=  null)){
						for(int i = 0; i < eElement.getElementsByTagName("Dependency").getLength(); i++){
							if(eElement.getElementsByTagName("Dependency").item(i).getAttributes().getNamedItem("start") != null){
								className = eElement.getElementsByTagName("Dependency").item(i).getAttributes().getNamedItem("start").getNodeValue();
								classes.add(classMap.get(className));
							}
						}
					}
					
					if(nodeMap.containsKey(id)){
						XmlObject xmlO = new XmlObject(id, nodeMap.get(id).getType(), name, desc, orderNum, owner, tags, classes);
						nodeMap.put(id, xmlO);
					}
					if(type.contains("uml:Object")){
						XmlObject xmlO = new XmlObject(id, "object", name, desc, orderNum, owner, tags, classes);
						nodeMap.put(id, xmlO);
					}
				}
			}
		}
	}

	
	private void createClasses(NodeList nList){
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;		
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				String id = eElement.getAttribute("xmi:idref");
				String type = eElement.getElementsByTagName("model").item(0).getAttributes().getNamedItem("type").getNodeValue();
				if(type.contains("Class")){
					String name = eElement.getElementsByTagName("model").item(0).getAttributes().getNamedItem("name").getNodeValue();
					classMap.put(id, name);
				}
			}
		}
	}
	
	
	private void createSwimLanes(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;		
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				String id = eElement.getAttribute("xmi:id");
				String type = eElement.getAttribute("xmi:type");
				String name = eElement.getAttribute("name");
				String desc = "";
				if(type.contains("uml")){
					XmlObject xmlO = new XmlObject(id, type, name, desc, 0, "");
					swimlaneMap.put(id, xmlO);
				}   
			}
		}
	}
	
	private void createEdges(NodeList nList){
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;		
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				String id = eElement.getAttribute("xmi:id");
				String type = eElement.getAttribute("xmi:type");
				String source = eElement.getAttribute("source");
				String target = eElement.getAttribute("target");

				XmlObject xmlO = new XmlObject(id, type, source, target);
				edgeList.add(xmlO);
				if(xmlO.getSourceID().contains(initNode.getObjectID())){
					initEdge = xmlO;
				}
			}
		}
	}
}
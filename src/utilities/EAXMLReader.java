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

import models.UMLAction;
import models.ActivityDiagram;
import models.Final;
import models.Initial;
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

	public EAXMLReader(Project activityDiagramViewObject, String path) {
		fXmlFile = new File(path);
		this.selectedProject = activityDiagramViewObject;
		this.activityDiagram = new ActivityDiagram("importedAD");
		this.selectedProject.addChild(activityDiagram);
		this.activityDiagram.setParent(selectedProject);
	}

	public void createStructure() throws ParserConfigurationException, SAXException, IOException {
		if (fXmlFile.exists()) {
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
			createNodes(nList);
			nList = xmldoc.getElementsByTagName("element");
			detailedNodes(nList);
			
			System.out.print(this.activityDiagram.getName());

			this.createADElements();
		}
	}

	private void getADName(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				if (eElement.getElementsByTagName("packagedElement").item(0).getAttributes()
						.getNamedItem("name") != null) {
					String name = eElement.getElementsByTagName("packagedElement").item(0).getAttributes()
							.getNamedItem("name").getNodeValue();
					this.activityDiagram.setName(name);
				}
			}
		}
	}

	private void createNodes(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				String id = eElement.getAttribute("xmi:id");
				String type = eElement.getAttribute("xmi:type");
				String name = eElement.getAttribute("name");
				String desc = "";
				if (type.contains("uml")) {
					XmlObject xmlO = new XmlObject(id, type, name, desc, 0, "", "");
					nodeMap.put(id, xmlO);
				}
			}
		}
	}

	private void detailedNodes(NodeList nList) {
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
				String alias = "";
				int orderNum = 0;
				HashMap<String, String> tags = new HashMap<String, String>();
				List<String> classes = new ArrayList<String>();

				if (type.contains("uml")) {
					if (eElement.getElementsByTagName("model").item(0).getAttributes().getNamedItem("owner") != null) {
						String owner_id = eElement.getElementsByTagName("model").item(0).getAttributes()
								.getNamedItem("owner").getNodeValue();
						owner = swimlaneMap.get(owner_id).getName();
					}
					if (eElement.getElementsByTagName("properties").item(0).getAttributes()
							.getNamedItem("documentation") != null) {
						desc = eElement.getElementsByTagName("properties").item(0).getAttributes()
								.getNamedItem("documentation").getNodeValue();
					}
					if (eElement.getElementsByTagName("properties").item(0).getAttributes()
							.getNamedItem("alias") != null) {
						alias = eElement.getElementsByTagName("properties").item(0).getAttributes()
								.getNamedItem("alias").getNodeValue();
					}
					if (eElement.getElementsByTagName("tag").getLength() != 0) {
						for (int i = 0; i < eElement.getElementsByTagName("tag").getLength(); i++) {
							String tagName = eElement.getElementsByTagName("tag").item(i).getAttributes()
									.getNamedItem("name").getNodeValue();

							if (tagName.contains("Order num")) {
								orderNum = Integer.parseInt(eElement.getElementsByTagName("tag").item(i).getAttributes()
										.getNamedItem("value").getNodeValue());
							} else {
								tags.put(tagName, eElement.getElementsByTagName("tag").item(i).getAttributes()
										.getNamedItem("value").getNodeValue());
							}
						}
					}

					if ((eElement.getElementsByTagName("Dependency") != null)) {
						for (int i = 0; i < eElement.getElementsByTagName("Dependency").getLength(); i++) {
							if (eElement.getElementsByTagName("Dependency").item(i).getAttributes()
									.getNamedItem("start") != null) {
								className = eElement.getElementsByTagName("Dependency").item(i).getAttributes()
										.getNamedItem("start").getNodeValue();
								classes.add(classMap.get(className));
							}
						}
					}

					if (nodeMap.containsKey(id)) {
						XmlObject xmlO = new XmlObject(id, nodeMap.get(id).getType(), name, desc, orderNum, owner, tags,
								classes, alias);
						nodeMap.put(id, xmlO);
					}
					if (type.contains("uml:Object")) {
						XmlObject xmlO = new XmlObject(id, "object", name, desc, orderNum, owner, tags, classes, alias);
						nodeMap.put(id, xmlO);
					}
				}
			}
		}
	}
	
	private void createADElements() {
		for (XmlObject xmls : nodeMap.values()) {
			if (xmls.getType().equals("action")) {
				this.activityDiagram.addChild(new UMLAction(xmls.getName(), xmls.getAlias()));
			} else if (xmls.getType().equals("initial node")) {
				this.activityDiagram.addChild(new Initial(xmls.getName(),xmls.getAlias()));
			} else if (xmls.getType().equals("final node")) {
				this.activityDiagram.addChild(new Final(xmls.getName(), xmls.getAlias()));
			}
		}
	}
}

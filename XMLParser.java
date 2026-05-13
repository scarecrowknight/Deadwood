

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.util.*;



public class XMLParser{
   
   public Document getDocFromFile(String filename)
   throws ParserConfigurationException{
     {
  
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = null;
           
        try{
            doc = db.parse(filename);
        } catch (Exception ex){
            System.out.println("XML parse failure");
            ex.printStackTrace();
        }
        return doc;
     } // exception handling
        
   }
   
   //gets first child with a specific name from a Node
   public Node filterChild(Node node, String childName){
      NodeList nodeChildren = node.getChildNodes();
      int childIndex = -1;
      for (int i=0; i<nodeChildren.getLength(); i++){
         if(nodeChildren.item(i).getNodeName().equals(childName)){
            childIndex = i;
            break;
         } 
      }
      
      return nodeChildren.item(childIndex);
   }
   
   //gets all children with a specific name from a Node
   public ArrayList<Node> filterChildren(Node node, String childName){
      
      NodeList nodeChildren = node.getChildNodes();
      ArrayList<Node> neighbors = new ArrayList<Node>();
      
      for (int i=0; i<nodeChildren.getLength(); i++){
         Node neighbor = nodeChildren.item(i);
         if(neighbor.getNodeName().equals(childName)){
            neighbors.add(neighbor);
         } 
      }
      
      return neighbors;
   }
   
      
}
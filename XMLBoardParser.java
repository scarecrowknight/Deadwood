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



public class XMLBoardParser{
   
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
   
   // reads room info and constructs 12 Room objects, adding them to the board and filling
   // in adjecencies as it goes. Rest of the class is all helper methods for this one task.
   public void readRooms(Document d, Board b){
      Element root = d.getDocumentElement();

      NodeList sets = root.getElementsByTagName("set");
          
      parseSets(sets, b);
      
      NodeList trailerNodeList = root.getElementsByTagName("trailer");
      parseTrailer(trailerNodeList, b);
      
      NodeList officeNodeList = root.getElementsByTagName("office");
      parseOffice(officeNodeList, b);

   }
   
   public void parseTrailer(NodeList trailerNodeList, Board b){
      Node trailerNode = trailerNodeList.item(0);
      String name = "Trailer";
      Room trailer = new Room(name, b);
      b.putRoom(trailer.getName(), trailer);
      b.setTrailer(trailer);
      
      String[] neighbors = parseNeighbors(trailerNode);
      
      for (String neighbor: neighbors){
         b.addEdge(trailer, neighbor);
      }

   }
   
   public void parseOffice(NodeList officeNodeList, Board b){
      Node officeNode = officeNodeList.item(0);
      String name = "office";
      castingOffice office = new castingOffice(name, b);
  
      b.putRoom(office.getName(), office);
      
      String[] neighbors = parseNeighbors(officeNode);
      
      for (String neighbor: neighbors){
         b.addEdge(office, neighbor);
      }

      

      
   }

   
   
   public void parseSets(NodeList sets, Board b){
      
      
      //itterates through each of the rooms in the XML file, creating a Room object for each. 
      for ( int i=0; i<sets.getLength(); i++ ){
         Node setNode = sets.item(i);
         
         String name = setNode.getAttributes().getNamedItem("name").getNodeValue();

         int shotCount = parseTakes(setNode);
         
         ArrayList<Role> roles = parseParts(setNode);
         
         Set set = new Set(name, b, shotCount, roles);
         
         b.putRoom(set.getName(), set);
         
         String[] neighbors = parseNeighbors(setNode);
         
         for (String neighbor: neighbors){
            b.addEdge(set, neighbor);
         }
      }
      
      return;
      
   
   }
      
   public int parseTakes(Node roomNode){
      
      String childName = "takes";
      Node takesContainer = filterChild(roomNode, childName);
      
      childName = "take"; 
      ArrayList<Node> takes = filterChildren(takesContainer, childName);
      return takes.size();
      
   }
   
   public ArrayList<Role> parseParts(Node roomNode){
      
      String childName = "parts";
      Node partsContainer = filterChild(roomNode, childName);
      
      childName = "part"; 
      ArrayList<Node> parts = filterChildren(partsContainer, childName);
      
      ArrayList<Role> roles = new ArrayList<Role>();
      
      for (Node part: parts){
      
         NamedNodeMap attributes = part.getAttributes();
      
         String name = attributes.getNamedItem("name").getNodeValue();
         int level = Integer.parseInt(attributes.getNamedItem("level").getNodeValue());
         
         childName = "line";
         Node lineNode = filterChild(part, childName);
         String quote = lineNode.getTextContent();
         
         Role role = new Role(name, quote, level, false, false);
         
         roles.add(role);
         
         }
         
      return roles;
   }
   
   //set is passed in. Inside we extract the string array of neighbors
   public String[] parseNeighbors(Node roomNode){
      
      
      String childName = "neighbors";
      Node neighborsContainer = filterChild(roomNode, childName);
      //Above is a node contining a NodeList containing the neighbor nodes
      
      childName = "neighbor"; 
      ArrayList<Node> neighbors = filterChildren(neighborsContainer, childName);
      //This is an ArrayList containing the neighbor nodes
      
      String[] neighborNames = getNeighborNames(neighbors);
      
      return neighborNames;
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
      ArrayList<Node> children = new ArrayList<Node>();
      
      for (int i=0; i<nodeChildren.getLength(); i++){
         Node child = nodeChildren.item(i);
         if(child.getNodeName().equals(childName)){
            children.add(child);
         } 
      }
      
      return children;
   }
   
   public String[] getNeighborNames(ArrayList<Node> neighbors){
      String[] names = new String[neighbors.size()];
      int i = 0;
      for (Node neighbor: neighbors){
         NamedNodeMap attributes = neighbor.getAttributes();
         Node nameNode = attributes.getNamedItem("name");
         String name = nameNode.getNodeValue();
         names[i] = name;
         i++;
      }
      return names;
   }
   
   //for testing only, shouldnt need to be run as main
   public static void main(String[] args){
      XMLBoardParser p = new XMLBoardParser();
      Board b = new Board();
      String filename = "board.xml";
      try{
         Document d = p.getDocFromFile(filename);
         p.readRooms(d, b);
      } catch(Exception e) {
         System.out.println(":c");
         e.printStackTrace();
      }
   }
}
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
   
   //returned room[] will always be size 12 because the board always has 12 rooms.
   public void readRooms(Document d, Board b){
      Element root = d.getDocumentElement();

      NodeList sets = root.getElementsByTagName("set");
      
      //fills the first 10 slots of rooms with the Sets. Also adds all sets to the board.    
      parseSets(sets, b);
      
      NodeList trailerNodeList = root.getElementsByTagName("trailer");
      parseTrailer(trailerNodeList, b);
      
      NodeList officeNodeList = root.getElementsByTagName("office");
      parseOffice(officeNodeList, b);

   }
   
   public void parseSets(NodeList sets, Board b){
      
      
      //itterates through each of the rooms in the XML file, creating a Room object for each. 
      for ( int i=0; i<sets.getLength(); i++ ){
         Node setNode = sets.item(i);
         
         String name = setNode.getAttributes().getNamedItem("name").getNodeValue();
         
         Set set = new Set(name, b);
         b.putRoom(set.getName(), set);
         
         String[] neighbors = parseNeighbors(setNode);
         
         
         for (String neighbor: neighbors){
            b.addEdge(set, neighbor);
         }
      }
      
      return;
      
   
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
   
   //set is passed in. Inside we extract the string array of neighbors
   public String[] parseNeighbors(Node roomNode){
      
      //itterates through until it finds nieghbors. This should be used in borader context to also
      //pull shots and roles from the database since the only thing that. Oh hey that means I should implement it 
      //as a method lol.
      
      
      String childName = "neighbors";
      Node neighborsContainer = filterChild(roomNode, childName);
      //Above is a node contining a NodeList containing the neighbor nodes
      
      childName = "neighbor"; 
      ArrayList<Node> neighbors = filterChildren(neighborsContainer, childName);
      //This is an ArrayList containing the neighbor nodes
      
      String[] neighborNames = getNeighborNames(neighbors);
      //getNeighborNames extracts the name attributes from the neighbor nodes
      
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
      ArrayList<Node> neighbors = new ArrayList<Node>();
      
      for (int i=0; i<nodeChildren.getLength(); i++){
         Node neighbor = nodeChildren.item(i);
         if(neighbor.getNodeName().equals(childName)){
            neighbors.add(neighbor);
         } 
      }
      
      return neighbors;
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
      XMLParser p = new XMLParser();
      Board b = new Board();
      String filename = "board.xml";
      try{
         Document d = p.getDocFromFile(filename);
         p.readRooms(d, b);
         Room jail = b.getRoom("General Store");
         for (Room room : jail.getAdjacent()){
            System.out.println(room.getName());
         }
      } catch(Exception e) {
         System.out.println(":c");
         e.printStackTrace();
      }
   }
}
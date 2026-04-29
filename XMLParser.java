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
   public Room[] readRooms(Document d){
      Element root = d.getDocumentElement();

      NodeList sets = root.getElementsByTagName("set");
      
      Room[] rooms = new Room[12];
      
      //reallistically this should be passed in instead of constructed here. IDK ill figure it out later
      Board b = new Board();
      
      //fills the first 10 slots of rooms with the Sets. Also adds all sets to the board.    
      rooms = parseSets(rooms, sets, b);
      
      NodeList trailerNodeList = root.getElementsByTagName("trailer");
      Room room = parseTrailer(trailerNodeList, b);
      //assigned to the 11th slot since the first 10 are full
      rooms[10] = room;
      
      NodeList officeNodeList = root.getElementsByTagName("office");
      room = parseOffice(officeNodeList, b);
      //assigned to the 12th slots since the first 11 are full
      rooms[11] = room;
      
      
      return rooms;
   }
   
   public Room[] parseSets(Room[] rooms, NodeList sets, Board b){
      
      
      //itterates through each of the rooms in the XML file, creating a Room object for each. 
      for ( int i=0; i<sets.getLength(); i++ ){
         Node setNode = sets.item(i);
         
         
         // extracts the name of the node from xml file
         String name = setNode.getAttributes().getNamedItem("name").getNodeValue();
         
         
         Set set = new Set(name, b);
         b.putRoom(set);
         
         String[] neighbors = parseNeighbors(setNode);
         
         
         for (String neighbor: neighbors){
            b.addEdge(set.name, neighbor);
         }

         rooms[i] = set;
      }
      
      return rooms;
      
   
   }
   
   public Room parseTrailer(NodeList trailerNodeList, Board b){
      Node trailerNode = trailerNodeList.item(0);
      String name = "trailer";
      Room room = new Room(name, b);
      
      //needs adjacency
      
      return room;
   }
   
   public Room parseOffice(NodeList officeNodeList, Board b){
      Node officeNode = officeNodeList.item(0);
      String name = "office";
      Room room = new Room(name, b);
      
      //needs adjacency
      
      return room;
      
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
      
      for(String name: neighborNames){
         System.out.println(name);
      }
      
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
      String filename = "board.xml";
      try{
         Document d = p.getDocFromFile(filename);
         p.readRooms(d);
      } catch(Exception e) {
         System.out.println(":c");
         e.printStackTrace();
      }
   }
}
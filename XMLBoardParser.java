import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.util.*;
import java.util.ArrayList;



public class XMLBoardParser extends XMLParser{
   
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
   

   private void parseTrailer(NodeList trailerNodeList, Board b){
      Node trailerNode = trailerNodeList.item(0);
      String name = "Trailer";
      Room trailer = new Room(name, b);
      b.putRoom(trailer.getName(), trailer);
      b.setTrailer(trailer);
      
      String[] neighbors = parseNeighbors(trailerNode);
      
      for (String neighbor: neighbors){
         b.addEdge(trailer, neighbor);
      }


      Node areaNode = filterChild(trailerNode, "area");
      Area area = new Area(areaNode);
      trailer.updateArea(area);


   }
   
   private void parseOffice(NodeList officeNodeList, Board b){
      Node officeNode = officeNodeList.item(0);
      String name = "Casting Office";
      CastingOffice office = new CastingOffice(name, b);
  
      b.putRoom(office.getName(), office);
      
      String[] neighbors = parseNeighbors(officeNode);
      
      for (String neighbor: neighbors){
         b.addEdge(office, neighbor);
      }

      //gets the office's position values
      Node areaNode = filterChild(officeNode, "area");
      Area area = new Area(areaNode);
      office.updateArea(area);

      //gets position values of upgrades inside office and their prices.

      Node upgradesNode = filterChild(officeNode, "upgrades");

      ArrayList<Node> upgrades = filterChildren(upgradesNode, "upgrade");

      for(Node upgrade : upgrades){
         Node upgradeAreaNode = filterChild(upgrade, "area");
         area = new Area(upgradeAreaNode);
         office.addUpgradeArea(area);
      }

      

      
   }  
   
   private void parseSets(NodeList sets, Board b){
      
      //itterates through each of the rooms in the XML file, creating a Room object for each. 
      for ( int i=0; i<sets.getLength(); i++ ){
         Node setNode = sets.item(i);
         
         String name = setNode.getAttributes().getNamedItem("name").getNodeValue();

         ArrayList<Area> takePositions = parseTakes(setNode);
         int shotCount = takePositions.size();
         
         ArrayList<Role> roles = parseParts(setNode);
         //ivy continue from here and add budget in parser :)
         Set set = new Set(name, b, shotCount, roles);
         
         b.putRoom(set.getName(), set);
         
         String[] neighbors = parseNeighbors(setNode);
         
         for (String neighbor: neighbors){
            b.addEdge(set, neighbor);
         }

         Node areaNode = filterChild(setNode, "area");
         Area area = new Area(areaNode);
         set.updateArea(area);

      }
      
      return;
      
   
   }

   private ArrayList<Role> parseParts(Node roomNode){

      Node partsContainer = filterChild(roomNode, "parts");

      ArrayList<Node> parts = filterChildren(partsContainer, "part");

      ArrayList<Role> roles = new ArrayList<Role>();

      for (Node part: parts){

         NamedNodeMap attributes = part.getAttributes();

         String name = attributes.getNamedItem("name").getNodeValue();
         int level = Integer.parseInt(attributes.getNamedItem("level").getNodeValue());

         Node lineNode = filterChild(part, "line");
         String quote = lineNode.getTextContent();

         Role role = new Role(name, quote, level, false, false);

         Node areaNode = filterChild(part, "area");
         Area area = new Area(areaNode);

         role.setArea(area);

         roles.add(role);

      }

      return roles;
   }




   private ArrayList<Area> parseTakes(Node roomNode){

      Node takesContainer = filterChild(roomNode, "takes");

      ArrayList<Node> takes = filterChildren(takesContainer, "take");
      ArrayList<Area> takePositions = new ArrayList<Area>();
      for(Node take : takes){
         Node areaNode = filterChild(take, "area");
         Area area = new Area(areaNode);
         takePositions.add(area);
      }
      return takePositions;
      
   }
   
   //set is passed in. Inside we extract the string array of neighbors
   private String[] parseNeighbors(Node roomNode){
      

      Node neighborsContainer = filterChild(roomNode, "neighbors");
      //Above is a node contining a NodeList containing the neighbor nodes
      
      ArrayList<Node> neighbors = filterChildren(neighborsContainer, "neighbor");
      //This is an ArrayList containing the neighbor nodes
      
      String[] neighborNames = getNeighborNames(neighbors);
      
      return neighborNames;
   }
   
   //gets first child with a specific name from a Node
   
   private String[] getNeighborNames(ArrayList<Node> neighbors){
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
         Room r = b.getRoom("Trailer");
      } catch(Exception e) {
         System.out.println(":c");
         e.printStackTrace();
      }
   }
}
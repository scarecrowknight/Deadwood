import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.util.*;


public class XMLCardParser extends XMLParser{

   public void readCards(Document d, SceneDeck deck){
      Element root = d.getDocumentElement();
       
      NodeList cards = root.getElementsByTagName("card");

      
      for ( int i=0; i<cards.getLength(); i++ ){
         Node cardNode = cards.item(i);
         Card card = parseCard(cardNode);
            deck.add(card);
      }


   }
   
   public Card parseCard(Node cardNode){
      ArrayList<Role> roles = parseParts(cardNode);
      NamedNodeMap attributes = cardNode.getAttributes();   
      int budget = Integer.parseInt(attributes.getNamedItem("budget").getNodeValue());
      String imageFileName = attributes.getNamedItem("img").getNodeValue();
      String name = attributes.getNamedItem("name").getNodeValue();
      String sceneDescription = filterChild(cardNode, "scene").getTextContent();
      Card card  = new Card(name, imageFileName, budget, sceneDescription, roles);
      return card;
   }

   private ArrayList<Role> parseParts(Node roomNode){
      
      String childName = "part"; 
      ArrayList<Node> parts = filterChildren(roomNode, childName);
      
      ArrayList<Role> roles = new ArrayList<Role>();
      
      for (Node part: parts){
      
         NamedNodeMap attributes = part.getAttributes();
      
         String name = attributes.getNamedItem("name").getNodeValue();
         int level = Integer.parseInt(attributes.getNamedItem("level").getNodeValue());
         
         childName = "line";
         Node lineNode = filterChild(part, childName);
         String quote = lineNode.getTextContent();
         
         Role role = new Role(name, quote, level, false, true);
         
         roles.add(role);
         
         }
         
      return roles;
   }

}
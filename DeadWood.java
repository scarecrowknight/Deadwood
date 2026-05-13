import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.*;
import java.util.ArrayList;

public class DeadWood {

   private Board board;
   private SceneDeck deck;
    
    private void makeSceneDeck(){
      XMLCardParser p = new XMLCardParser();
      String filename = "cards.xml";
      SceneDeck deck = new SceneDeck();
      
      try{
         Document d = p.getDocFromFile(filename);
         p.readCards(d, deck);
      } catch(Exception e) {
         System.out.println(":c");
         e.printStackTrace();
      }
      this.deck = deck;
    }
    
    private void makeBoard(){
      XMLBoardParser p = new XMLBoardParser();
      Board b = new Board();
      String filename = "board.xml";
      try{
         Document d = p.getDocFromFile(filename);
         p.readRooms(d, b);
         Room r = b.getRoom("Trailer");
         System.out.println(r.getAdjacent());
      } catch(Exception e) {
         System.out.println(":c");
         e.printStackTrace();
      }
      this.board = b;
    }
    
    private Board getBoard(){
      return this.board;
    }
    private String getDeck(){
    	Card card = this.deck.draw();
      return card.toString();
    }
    
    public DeadWood(){
      this.makeBoard();
      this.makeSceneDeck();
    }
    
    public static void main(String[] args) {
      DeadWood deadwood = new DeadWood();
      System.out.println(deadwood.getDeck());
    }
    
}
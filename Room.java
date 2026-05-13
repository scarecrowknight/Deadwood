import java.util.*;

public class Room {

   private String name;
   private ArrayList<Room> adjacentRooms;
   private ArrayList<Player> players;
   //private Card sceneCard 
   
   public Room(String name, Board board){
      this.name = name;
      ArrayList<Room> adj = new ArrayList<Room>();
      this.adjacentRooms = adj; 

   }
   
   // alt constructor for if you know adj rooms already
   // never referenced so far, probably DELETE if it doesnt become useful
   public Room(String name, ArrayList<Room> adj, Board board){
      this.name = name;
      this.adjacentRooms = adj;

      
   }
   
   public void addAdjacent(Room room){
      this.adjacentRooms.add(room);
   }
   public ArrayList<Room> getAdjacent(){
      return this.adjacentRooms;
   }
   public String getName(){
      return this.name;
   }
   public String toString() {
	   return this.name;
   }

}

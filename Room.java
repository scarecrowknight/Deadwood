import java.util.*;

public class Room {

   private String name;
   private ArrayList<Room> adjacentRooms;
   private ArrayList<Player> players;
   private Area area;



   public Room(String name, Board board){
      this.name = name;
      this.adjacentRooms = new ArrayList<Room>();
      this.players = new ArrayList<Player>();
   }

   public void updateArea(Area area){
      this.area = area;
   }
   
   // alt constructor for if you know adj rooms already
   // never referenced so far, probably DELETE if it doesnt become useful
   public Room(String name, ArrayList<Room> adj, Board board){
      this.name = name;
      this.adjacentRooms = adj;
      this.players = new ArrayList<Player>();
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
   public void addPlayer(Player player) {
      this.players.add(player);
   }
   public void removePlayer(Player player) {
      this.players.remove(player);
   }
   public ArrayList<Player> getPlayers() {
      return this.players;
   }
   public Area getArea() {return this.area;}
}

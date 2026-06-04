import java.util.*;

public class Board {
   private Room trailer;
   //always an array of size 12 with a casting office and a trailer   
   private HashMap<String, Room> rooms;
   
   public Board(){
      this.rooms = new HashMap<String,Room>();
   }
   public Collection<Room> getAllRooms(){
	   return this.rooms.values();
   }
   public void setTrailer(Room trailer){
      this.trailer = trailer;
   }
   
   public Room getTrailer(){
      return this.trailer;
   }
   
   public void putRoom(String name, Room room){
      this.rooms.put(name, room);
   }
   
   public Room getRoom(String name){
      return this.rooms.get(name);
   }
   public int getRemaingScenes() {
	   int count = 0;
	   for(Room room : this.getAllRooms()) {
		   if (room instanceof Set) {
			   Set set = (Set) room;
		   
			   if(set.getActiveCard() != null) {
				   count++;
			   }
		   }
	   }
	   return count;
   }
   //hmm maybe not useful actually
   public void putRoom(String name){
      Room room = new Set(name, this);
      this.rooms.put(name, room);
   }
   
   public void addEdge(Room room1, Room room2){
   
      if(room1 ==null || room2 == null){
         return;
      }
   
      room1.addAdjacent(room2);
      room2.addAdjacent(room1);
   }
   
   public void addEdge(Room room1, String room2Name){

      Room room2 = this.getRoom(room2Name);
      if(room1 ==null || room2 == null){
         return;
      }
      room1.addAdjacent(room2);
      room2.addAdjacent(room1);

   }
   
   
   

}
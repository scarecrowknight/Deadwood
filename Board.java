
import java.util.*;

public class Board {
   
   //always an array of size 12 with a casting office and a trailer   
   private HashMap<String, Room> rooms;
   
   public Board(){
      this.rooms = new HashMap<String,Room>();
   }
   
   public Room getRoom(String name){
      return this.rooms.get(name);
   }
   
   public void putRoom(String name, Room room){
      this.rooms.put(name, room);
   }
   
   //hmm maybe not useful actually
   public void putRoom(String name){
      Room room = new Room(name, this);
      this.rooms.put(name, room);
   }
   
   public void addEdge(Room room1, Room room2){
      room1.addAdjacent(room2);
      room2.addAdjacent(room1);
   }
   
   
   //main is entirely for testing don't mind this
   public static void main(String[] args){
      Board b = new Board();
      
      //String name = (b.rooms[1].getClass().getSimpleName());
      
            
   }
   
   
   /*
   Because the board in Deadwood is static, with the trailer and casting office
   always in the same spot, we will not deal cards to index 5 or 6. If this is unclear
   check the image in the ReadMe it's explained with images there
   */
   
   //Card[] passed must always be size 10.
   
   /*
   public dealSceneCards(Card[] Cards){
      for (Room room: this.Rooms){
         int i = 0;
         if(room.getClass().getSimpleName().equals("Scene")){
            room.setCard(cards[i]);
            i++;
            
         }
      }

   }
   */

}


// to be transfered and compared with Kendall's work


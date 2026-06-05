import java.util.*;

public abstract class Room {

   private String name;
   private ArrayList<Room> adjacentRooms;
   private ArrayList<Player> players;
   private Area area;
   private final boolean[] occupiedSlots = new boolean[8];
   private final Map<Player, Integer> playerSlotMap = new HashMap<>();

   public Room(String name, Board board){
      this.name = name;
      this.adjacentRooms = new ArrayList<Room>();
      this.players = new ArrayList<Player>();
   }

   public void updateArea(Area area){
      this.area = area;
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
      if (player == null || this.players.contains(player)) {
         return;
      }
      this.players.add(player);
      assignSlot(player);
   }
   public void removePlayer(Player player) {
      if (player == null) {
         return;
      }
      Integer slot = playerSlotMap.remove(player);
      if (slot != null && slot >= 0 && slot < occupiedSlots.length) {
         occupiedSlots[slot] = false;
      }
      this.players.remove(player);
   }
   public int getSlotFor(Player player) {
      Integer slot = playerSlotMap.get(player);
      return slot != null ? slot : -1;
   }

   public void releaseSlot(Player player) {
      if (player == null) {
         return;
      }
      Integer slot = playerSlotMap.remove(player);
      if (slot != null && slot >= 0 && slot < occupiedSlots.length) {
         occupiedSlots[slot] = false;
      }
   }

   private void assignSlot(Player player) {
      if (player == null || playerSlotMap.containsKey(player)) {
         return;
      }
      int slot = findFirstAvailableSlot();
      if (slot >= 0) {
         occupiedSlots[slot] = true;
         playerSlotMap.put(player, slot);
      }
   }
   private int findFirstAvailableSlot() {
      for (int i = 0; i < occupiedSlots.length; i++) {
         if (!occupiedSlots[i]) {
            return i;
         }
      }
      return -1;
   }
   public ArrayList<Player> getPlayers() {
      return this.players;
   }
   public Area getArea() {return this.area;}
}

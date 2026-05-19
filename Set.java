import java.util.*;
public class Set extends Room {

	private int shotCount = 0; // xml calls these takes
	private ArrayList<Player> currentPlayers;
	/*the array lines can hold the line for each role "Well, I'll be!",
	also its size is the max number of roles for the scene */
	private ArrayList<Role> roles = null;
	private Card activeCard;
	
   public Set(String name, Board board, int shotCount, ArrayList<Role> roles) {
	   super(name, board);
	   this.shotCount = shotCount; //contains shots to be applied by board manager
	   this.currentPlayers = new ArrayList<Player>(); //list of current players on set
	   this.roles = roles; //containes the the "static roles" to be appended by parser
   }
   
   public Card getActiveCard() {
	   return this.activeCard;
   }
   public void setActiveCard(Card activeCard) {
	   this.activeCard = activeCard;
   }

   public int getShotCount() {
	   return this.shotCount;
   }
   public void setShotCount(int shotCount) {
	   this.shotCount = shotCount;
   }

   public ArrayList<Role> getOffCardRoles() {
	   return this.roles;
   }
   
   public ArrayList<Role> getOnCardroles() {
      return this.activeCard.getRoles();
   }
   
   public void setRoles(ArrayList<Role> roles) {
	   this.roles = roles;
   }
}

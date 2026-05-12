import java.lang.reflect.Array;
import java.util.*;
public class Set extends Room {

	private int shotCount = 0; // xml calls these takes
	private int currentPlayers = 0;
	/*the array lines can hold the line for each role "Well, I'll be!",
	also its size is the max number of roles for the scene */
	private List<role> roles = null;
	private card activeCard = null;
	
   public Set(String name, Board board, int shotCount, int currentPlayers, List<role> roles, card activeCard) {
	   super(name, board);
	   this.shotCount = shotCount; //contains shots to be applied by board manager
	   this.currentPlayers = currentPlayers; //list of current players on set
	   this.roles = roles; //containes the the "static roles" to be appended by parser
	   this.activeCard = activeCard; //contains active card on the set to be applied by board manager
   }
   public card getActiveCard() {
	   return this.activeCard;
   }
   public void setActiveCard(card activeCard) {
	   this.activeCard = activeCard;
   }

   public int getShotCount() {
	   return this.shotCount;
   }
   public void setShotCount(int shotCount) {
	   this.shotCount = shotCount;
   }

   public int getCurrentPlayers() {
	   return this.currentPlayers;
   }
   public void setCurrentPlayers(int currentPlayers) {
	   this.currentPlayers = currentPlayers;
   }

   public List<role> getRoles() {
	   return this.roles;
   }
   public void setRoles(List<role> roles) {
	   this.roles = roles;
   }
}

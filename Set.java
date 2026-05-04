import java.util.*;
public class Set extends Room {

	public String sceneName;
	public int shotCount; // xml calls these takes
	public int currentPlayers;
	/*the array lines can hold the line for each role "Well, I'll be!",
	also its size is the max number of roles for the scene */
	public ArrayList<String> lines; 
	public int maxRoles;
	
   public Set(String sceneName,String name, Board board, int shotCount, int currentPlayers, ArrayList<String> lines) {
	   super(name, board);
	   this.sceneName = sceneName;
	   this.shotCount = shotCount;
	   this.currentPlayers = currentPlayers;
	   this.lines = lines;
	   this.maxRoles = lines.size();
   }
   //THIS IS INCOMPLETE AND LITERALLY JUST FOR LETTING MY XML PARSER COMPILE DELETE LATER
   public Set(String name, Board board){
      super(name, board);
   }
}

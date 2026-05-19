import java.util.*;

public class View{
   
	private Scanner scanner;

	public View(){
		this.scanner = new Scanner(System.in);
	}   

	//startup prompt
	public boolean askStart() {
		System.out.println("Welcome to Deadwood!");
		while (true) {
			System.out.println("Are you ready to begin? (yes/no): ");
			String input = scanner.nextLine().trim().toLowerCase();

			if (input.equals("yes") || input.equals("y")) {
				return true;
			} else if(input.equals("no") || input.equals("n")) {
				return false;
			} else {
				System.out.println("INVALID INPUT PLEASE TYPE 'YES' or 'NO'.");
			}
		}
	}
	
	public void printFullBoard(Board board) {
		System.out.println("______CURRENT BOARD_____");
		
		for (Room room : board.getAllRooms()) {
			System.out.println("Room: " + room.getName());
			System.out.println(" Adjacent to: " + room.getAdjacent());
            if (room.getPlayers() != null && !room.getPlayers().isEmpty()) {
                System.out.print(" Players in room: ");
                for (Player player : room.getPlayers()) {
                    System.out.print(player.getName() + ", ");
                }
                System.out.println();
            }
            
		}
	System.out.println("__________________");
    }
	
	public void showMessage(String message) {
		System.out.println(message);
	}
public List<String> getPlayerNames(){
    Boolean valid = false;
    List<String> names = new ArrayList<>();
    
    while (!valid) {
    names.clear();
    System.out.println("Enter player names, separated by commas (Up to 8 players):");
    System.out.print("> ");
        
        // Grab entire line
        String raw = scanner.nextLine();
        
        // Chop the string
        String[] splitNames = raw.split(",");
        
        // Add names to the list
        for (String name : splitNames) {
            names.add(name.trim());
        }
        if (names.size() >= 2 && names.size() <= 8) {
            valid = true;
        } else {
            System.out.println("Invalid number of players. Please enter between 2 and 8 players.");
        }
    }
    return names;
}
public void printAvailableRoles(Set destination) {
    showMessage("\nAvailable roles at " + destination.getName() + ":");
    boolean foundRoles = false;

    //Check Off-Card Roles (Extras)
    if (destination.getOffCardRoles() != null) { 
        for (Role r : destination.getOffCardRoles()) {
            if (!r.getOccupied()) {
                showMessage("-Extra: " + r.getName() + " (Req. Rank " + r.getRank() + ")");
                foundRoles = true;
      
            }}}

    //Check On-Card Roles (Starring)
    if (destination.getOnCardroles() != null) {
        for (Role r : destination.getOnCardroles()) {
            if (!r.getOccupied()) {
                showMessage("-Starring: " + r.getName() + " (Req. Rank " + r.getRank() + ")");
                foundRoles = true;
            }}}
    if (!foundRoles) {
        showMessage("  (No roles currently available here)");
    }
}
public void render(Packet packet) {
    if (packet.getLastEvent() == null) return;
    
    switch (packet.getLastEvent()) {
    case TURN_START:
    	System.out.println("Hey " + packet.getPlayer().getName() + "! You're on now!" );
    	System.out.println("Current player: " + packet.getPlayer().getName());
    	System.out.println("Location: " + packet.getLocation().getName());
        System.out.println("Money: " + packet.getPlayer().getMoney() + " | Credits: " + packet.getPlayer().getCredits() + " | Rank: " + packet.getPlayer().getRank());
    	break; 	
    case INVALID_ACTION:
    	System.out.println("Invalid action... Don't do that again...");
    	break;
    case MOVED:
    	System.out.println("Moved to:" + packet.getTargetLocation().getName());
    	break;
    case SCENE_REVEALED:
    	System.out.println(packet.getTargetLocation().getName() + " is the new scene!");
    	break;	
    default:
    	break;
    }
}
public String renderAndRequestAction(Packet packet) {
    render(packet);
    if (packet.getLastEvent() == Packet.EventType.QUERY_DESTINATION) {
    	System.out.println("Alright, so where are you goin'? " + packet.getAvailableActions());
    } else if(packet.getLastEvent() == Packet.EventType.QUERY_MOVE) {
    	System.out.println("Are you sure? " + packet.getAvailableActions());
    }else {
    //showing options + requesting input
    	System.out.println("What would you like to do?" + packet.getAvailableActions());
    }
    System.out.print("> ");
    return this.scanner.nextLine();
}
}
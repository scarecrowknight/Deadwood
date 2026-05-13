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
            System.out.println("Invalid number of players. Please enter between 1 and 8 players.");
        }
    }
    return names;
}
public void render(Packet packet) {
    // Implementation for rendering a packet
}
public String renderAndRequestAction(Packet packet) {
    render(packet);
    System.out.print("> ");
    return scanner.nextLine();
}
}
import java.util.*;

public class view{
   
	private Scanner scanner;

	public view(){
		this.scanner = new Scanner(System.in);
	}   


public List<String> getPlayerNames(){
    Boolean valid = false;
    List<String> names = new ArrayList<>();
    while (!valid) {
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
        if (names.size() >= 1 && names.size() <= 8) {
            valid = true;
        } else {
            System.out.println("Invalid number of players. Please enter between 1 and 8 players.");
        }
    }
    return names;
}


}
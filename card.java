import java.util.ArrayList;
import java.util.List;

public class card{	
	private String cardName;
	private String sceneDescription;
	
	// first index of all lists belong to part 1.
	private List<String> partNames = new ArrayList<>();
	private List<Integer> partLevels = new ArrayList<>();
	private List<String> partLines = new ArrayList<>();
	
	public Card(String cardName, String sceneDescription, List<String> partNames, List<String> partLines, List<Integer> partLevels) {	
		this.cardName = cardName;
		this.sceneDescription = sceneDescription;
		this.partNames = partNames;
		this.partLevels = partLevels;
		this.partLines = partLines;	
	}
}
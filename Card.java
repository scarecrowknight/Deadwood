import java.util.ArrayList;
import java.util.List;

public class Card{	
	private String name;
   private int budget;
   private String sceneDescription;
	
   private ArrayList<Role> roles;
	
	public Card() {	
      return;
	}
   
   public ArrayList<Role> getRoles() {
	   return this.roles;
   }

}
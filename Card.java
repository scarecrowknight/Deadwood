import java.util.ArrayList;
import java.util.List;

public class Card{	
	private String name;
   private String imgFileName;
   private int budget;
   private String sceneDescription;
   private ArrayList<Role> roles;
	
	public Card(String name, String imgFileName, int budget, String sceneDescription, ArrayList<Role> roles) {	
      this.name = name;
      this.imgFileName = imgFileName;
      this. budget = budget;
      this. sceneDescription = sceneDescription;
      this. roles = roles;
   }
   public String getName() {
      return this.name;
   }
   public String getImgFileName() {
      return this.imgFileName;
   }
   public String getSceneDescription() {
      return this.sceneDescription;
   }
   public ArrayList<Role> getRoles() {
	   return this.roles;
   }
   
   public String toString() {
	   return this.name;
   }

   public int getBudget() {
      return this.budget;
   }
}
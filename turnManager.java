import java.util.*;

public class turnManager{
    private List<player> players = new ArrayList<>();
    private view view = new view();

    public void addPlayers(){
       
        //List<String> playerNames = ui.getPlayerNames();
        List<String> playerNames = view.getPlayerNames(); //placeholder


        for (int i = 0; i < playerNames.size(); i++){
            String name = playerNames.get(i);
            players.add(new player(i,i, null, 0, name, 0, 0));
        }
    }
    public void randomizeTurnOrder(){
        Collections.shuffle(players);
        for (int i = 0; i < players.size(); i++){
            players.get(i).setTurnOrder(i);
        }
    }
	public void pickAction(int i){
        // Pick an action player
            // Ask the player to pick an action
            //String action = ui.askplayerAction(players.get(i));
            String action = "ooga booga"; //placeholder
            if (action == "move"){
                // Handle the move action
            } else if (action == "act"){
                // Handle the act action
            } else if (action == "rehearse"){
                // Handle the rehearse action
            } else{
                //error handling
            }
    }
}
	

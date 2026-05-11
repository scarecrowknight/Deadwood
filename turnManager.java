import java.util.*;

public class turnManager{
    private List<Player> players = new ArrayList<>();
    private view view = new view();
    private int days = 4;
    private int currentDay = 1;


    public void addPlayers(){
       
        //List<String> playerNames = ui.getPlayerNames();
        List<String> playerNames = view.getPlayerNames(); //placeholder


        
        int numPlayers = players.size();
        int startCredits = 0;
        int startRank = 1;
        
        if (numPlayers <= 3){
            this.days = 3;
        } // If there are 4 or more players, use the default 4 days
        else if (numPlayers == 5){
            startCredits = 2;
        } else if (numPlayers == 6){
            startCredits = 4;
        } else if (numPlayers >= 7){
            startRank = 2;
        }

        for (int i = 0; i < playerNames.size(); i++){
            String name = playerNames.get(i);
            //public Player(int userNumber, int turnOrder, role role, int money,int credits, int rank, String name, int practiceChips) {
            players.add(new Player(i,i, null, 0, startCredits, startRank, name, 0));
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
            String action = "placeholder"; //placeholder
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
	

import java.util.*;

public class GameManager{
    private List<Player> players = new ArrayList<>();
    private View view = new View();
    private Board board;
    private int days = 4;
    private int currentDay = 1;

    public int totalPlayers() {
    	return players.size(); 
    }
    public int daysLeft() {
    	if (players.size() <= 3){
            this.days = 3;
    }
    	return days - currentDay;
    }
    public void addPlayers(){
       
        //List<String> playerNames = ui.getPlayerNames();
        List<String> playerNames = view.getPlayerNames();
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
	public void pickAction(int i) {
        Player currentPlayer = players.get(i);
        boolean turnComplete = false;
        boolean hasMoved = false;

        // Announce Turn Start natively via DTO
        Packet startPacket = new Packet(currentPlayer, currentPlayer.currentLocation(), board, null, Packet.EventType.TURN_START);
        view.render(startPacket); 

        while (!turnComplete) {
            List<String> availableActions = new ArrayList<>();
            Room loc = currentPlayer.currentLocation();

            //determine valid choices based on state
            if (currentPlayer.getRole() != null) {
                //if working a role they MUST work. No moving or upgrading.
                availableActions.add("Work");
            } else {
                if (!hasMoved) {
                    availableActions.add("Move");
                }
                //take a role if on an active Set
                if (loc instanceof Set && ((Set) loc).getActiveCard() != null) {
                    availableActions.add("Take Role");
                }
                //upgrade if in the casting office
                if (loc instanceof CastingOffice) {
                    availableActions.add("Upgrade");
                }
            }
            availableActions.add("End Turn");
            
            // 2. Request Choice via DTO Request
            Packet request = new Packet(currentPlayer, loc, board, availableActions, null);
            String action = view.renderAndRequestAction(request).toLowerCase().trim();

            // 3. Route Choice
            if (action.equals("move") && !hasMoved && currentPlayer.getRole() == null) {
                MoveManager mover = new MoveManager();
                //hasMoved = mover.reallyMove(currentPlayer);
                
            } else if (action.equals("take role") && currentPlayer.getRole() == null) {
                //boolean tookRole = takeRole(currentPlayer);
                //if (tookRole) turnComplete = true; // Taking a role ends action phase
            } else if (action.equals("work") && currentPlayer.getRole() != null) {
                //work(currentPlayer);
                turnComplete = true; // Working consumes the turn
            } else if (action.equals("upgrade") && loc instanceof CastingOffice) {
                UpgradeManager upgradeManager = new UpgradeManager(board, view);
                upgradeManager.upgrade(currentPlayer);
            } else if (action.equals("end turn")) {
                turnComplete = true;
            } else {
                Packet invalid = new Packet(currentPlayer, loc, board, null, Packet.EventType.INVALID_ACTION);
                view.render(invalid);
            }
        }
    }
}

	

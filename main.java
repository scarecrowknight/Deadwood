import java.util.*;

public class main{
    private List<Player> players = new ArrayList<>();
    private view view = new view();
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
                if (loc instanceof castingOffice) {
                    availableActions.add("Upgrade");
                }
            }
            availableActions.add("End Turn");
            
            // 2. Request Choice via DTO Request
            Packet request = new Packet(currentPlayer, loc, board, availableActions, null);
            String action = view.renderAndRequestAction(request).toLowerCase().trim();

            // 3. Route Choice
            if (action.equals("move") && !hasMoved && currentPlayer.getRole() == null) {
                //hasMoved = reallyMove(currentPlayer);
            } else if (action.equals("take role") && currentPlayer.getRole() == null) {
                //boolean tookRole = takeRole(currentPlayer);
                //if (tookRole) turnComplete = true; // Taking a role ends action phase
            } else if (action.equals("work") && currentPlayer.getRole() != null) {
                //work(currentPlayer);
                turnComplete = true; // Working consumes the turn
            } else if (action.equals("upgrade") && loc instanceof castingOffice) {
                upgrade(currentPlayer);
            } else if (action.equals("end turn")) {
                turnComplete = true;
            } else {
                Packet invalid = new Packet(currentPlayer, loc, board, null, Packet.EventType.INVALID_ACTION);
                view.render(invalid);
            }
        }
    }
    //upgrade
    private void upgrade(Player player) {
        castingOffice office = (castingOffice) player.currentLocation();
        int currentRank = player.getRank();

        //find what ranks the player has surpassed, what they can afford viab credits and dollars, and what they canty afford
        List<Integer> affordableRanks = new ArrayList<>();
        List<Integer> unaffordableRanks = new ArrayList<>();

        for (int r = currentRank + 1; r <= 6; r++) {
            int dCost = office.dollarCost(r);
            int cCost = office.creditCost(r);
            
            // If the player holds enough of EITHER currency, the tier is affordable
            if (player.getMoney() >= dCost || player.getCredits() >= cCost) {
                affordableRanks.add(r);
            } else {
                unaffordableRanks.add(r);
            }
        }

        //build list containing the possible ranks + cancel option to be sent out via packet
        List<String> rankOptions = new ArrayList<>();
        for (int r : affordableRanks) {
            rankOptions.add(String.valueOf(r));
        }
        rankOptions.add("Cancel");

        //send packet and recieve response. 
        Packet rankAsk = new Packet(player, office, board, rankOptions, Packet.EventType.QUERY_UPGRADE_RANK);
        rankAsk.setUpgradeQueryRankData(affordableRanks, unaffordableRanks);
        
        String rankInput = view.renderAndRequestAction(rankAsk).trim();
        if (rankInput.equalsIgnoreCase("cancel")) return;

        int targetRank;
        try {
            targetRank = Integer.parseInt(rankInput);
            if (!affordableRanks.contains(targetRank)) {
                upgrade(player); //recurse if invalid string bypassed constraints.
                return;
            }
        } catch (NumberFormatException e) {
            upgrade(player);
            return;
        }

        //find payment constraints/options and build the payment options list
        int dCost = office.dollarCost(targetRank);
        int cCost = office.creditCost(targetRank);
        boolean canPayD = player.getMoney() >= dCost;
        boolean canPayC = player.getCredits() >= cCost;

        List<String> paymentOptions = new ArrayList<>();
        if (canPayD) paymentOptions.add("Dollars");
        if (canPayC) paymentOptions.add("Credits");
        paymentOptions.add("Cancel");

        //send second packet containing the payment options
        Packet paymentAsk = new Packet(player, office, board, paymentOptions, Packet.EventType.QUERY_UPGRADE_PAYMENT);
        paymentAsk.setUpgradeQueryPaymentData(targetRank, dCost, cCost, canPayD, canPayC);

        String paymentChoice = view.renderAndRequestAction(paymentAsk).toLowerCase().trim();
        if (paymentChoice.equals("cancel")) {
            //recurse to upgrade if they cancle at this step
            upgrade(player);
            return;
        }

        //finalized transaction logic
        boolean success = false;
        int spentMoney = 0;
        int spentCredits = 0;

        if (paymentChoice.equals("dollars") && canPayD) {
            player.setMoney(player.getMoney() - dCost);
            spentMoney = -dCost;
            success = true;
        } else if (paymentChoice.equals("credits") && canPayC) {
            player.setCredits(player.getCredits() - cCost);
            spentCredits = -cCost;
            success = true;
        }

        if (success) {
            int rankGain = targetRank - player.getRank();
            player.setRank(targetRank);
            
            Packet confirm = new Packet(player, office, board, null, Packet.EventType.UPGRADED);
            confirm.setUpgradeData(rankGain, spentMoney, spentCredits);
            view.render(confirm);
        } else {
            upgrade(player);
        }
    }
    public void endGame() {
		System.exit(0); // needs real logic
}
}
	

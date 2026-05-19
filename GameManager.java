import java.util.*;

public class GameManager{
    private List<Player> players = new ArrayList<>();
    private View view;
    private Board board;
    private int days = 4;
    private int currentDay = 1;
    private SceneDeck sceneDeck = new SceneDeck();
    
	public GameManager(Board board, View view) {
    	this.board = board;
    	this.view = view;
    }
	public void initializeDeck(List<Card> parsedCards) {
		for (Card c : parsedCards) {
			sceneDeck.add(c);
		}
		sceneDeck.shuffleDeck();
	}
	public void setSceneDeck(SceneDeck parsedDeck) {
		this.sceneDeck = parsedDeck;
		this.sceneDeck.shuffleDeck();
	}
	
	//core game loop: Resets players to trailer, deals 10 scene cards to the sets,
	//                and cycles player turns until scene left = 1.
    public void runGame() {
    	while(currentDay <= days) {
    		
    		// Setup day
    		view.showMessage("It's day " + currentDay);
    		Room trailer = board.getTrailer();
    		for (Player p : players) {
    			p.SetLocation(trailer);
    		}
    		
    		// Deal Scene Cards to sets
    		for (Room room : board.getAllRooms()) {
    			if(room instanceof Set) {
    				Set set = (Set) room;
    				Card drawnCard = sceneDeck.draw();
    				if(drawnCard != null) {
    					set.setActiveCard(drawnCard);
    					// set.resetShotCounters();
    				}
    			}
    		}
    		//player turns/ day loop
    		while(board.getRemaingScenes() > 1) {
    			for(int i = 0; i < players.size(); i++) {
    				pickAction(i);
    			}
    		}
    		//all players have gone
    		view.showMessage("He who learns and runs away, lives to another day." );
    		currentDay++;
    	}
    	view.showMessage("The game is over... time to pack it up...");
    }
    
    public int totalPlayers() {
    	return players.size(); 
    }
    public int daysLeft() {
    	return days - currentDay;
    }
    public void addPlayers(){
        //List<String> playerNames = ui.getPlayerNames();
        List<String> playerNames = view.getPlayerNames();
        int numPlayers = playerNames.size();
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
                if (loc instanceof Set && ((Set) loc).getActiveCard() != null) { //may neeed to add a "is flipped" to card rather than a null atribute
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
            //Movement: grabs adj rooms, moves player view moveManager, and sends an update
            //            to view to announce the move and reveal any flipped cards
            if (action.equals("move") && !hasMoved && currentPlayer.getRole() == null) { 
                MoveManager mover = new MoveManager(board, view);    
                mover.reallyMove(currentPlayer);
                hasMoved = true;           
                /*List<Room> adjRooms = loc.getAdjacent();
                List<String> adjRoomNames = new ArrayList<>();
                for (Room r: adjRooms) {
                	adjRoomNames.add(r.getName());
                }
                adjRoomNames.add("Cancel");
                Packet roomQuery = new Packet(currentPlayer, loc, board, adjRoomNames, Packet.EventType.QUERY_DESTINATION);
                String userLocationChoice = view.renderAndRequestAction(roomQuery);
                
                //Cancel if user wants to.
                if (userLocationChoice.equals("cancel")) {
                	continue;
                }
                Room userDestination = null;
                for(Room r : adjRooms) {
                	if(r.getName().equalsIgnoreCase(userLocationChoice)) {
                		userDestination = r;
                		break;
                	}
                }                
                if(userDestination != null) {
                	MoveManager mover = new MoveManager();  	
                	mover.movePlayer(currentPlayer, userDestination);
                	hasMoved = true;
                	
                	Packet moved = new Packet(currentPlayer, userDestination, board, null, Packet.EventType.MOVED);
                	Card cardToReveal = null;
                	if(userDestination instanceof Set) {
                		cardToReveal = ((Set) userDestination).getActiveCard();
                	}
                	moved.setMoveData(userDestination, cardToReveal);
                	view.render(moved);
                } else {
                	Packet invalid = new Packet(currentPlayer, loc, board, null, Packet.EventType.INVALID_ACTION);
                	view.render(invalid);
                } */
                
            } else if (action.equals("take role") && currentPlayer.getRole() == null) {
            	TakeRoleManager takeRoleManager = new TakeRoleManager(board, view);
                takeRoleManager.TakeRole(currentPlayer);
                hasMoved = true; // Taking a role consumes the move action, but not the whole turn (player can still work if they take a role)
            } else if (action.equals("work") && currentPlayer.getRole() != null) {
                WorkManager work = new WorkManager(board, view, players);
                work.work(currentPlayer);
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

	

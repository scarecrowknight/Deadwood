import java.util.List;
// packet is an DTO object that carries data between the logic layer (primarily turn manager) and the view layer. Later it will be used to carry data to UI/Visualization layerws
// https://api-platform.com/docs/core/dto/
public class Packet{
    //defines exactly what semantic event just occurred in the logic layer
    public enum EventType {
        TURN_START,
        // Move Phase Events
        QUERY_MOVE,
        QUERY_DESTINATION,
        MOVED,
        SCENE_REVEALED,
        // Role / Audition Phase Events
        QUERY_TAKE_ROLE,
        CHOOSE_ROLE,
        TOOK_ROLE,
        ROLE_DENIED,
        // Work Phase Events
        QUERY_WORK,
        ACT_SUCCESS,
        ACT_FAIL,
        REHEARSED,
        REHEARSAL_MAXED,
        // Upgrade Phase Events
        UPGRADED,
        INVALID_ACTION,
        QUERY_UPGRADE_RANK,
        QUERY_UPGRADE_PAYMENT,
    }

    //game info 
    private Player player;
    private Room location;
    private Board board;
    private List<String> availableActions;
    private EventType lastEvent;
    
    // context fields depending on the event type
    private Room targetLocation;// Where they moved to
    private card currentCard;
    private List<role> availableRoles; //current available roles at the location
    private role targetRole;     // role they took
    private int rawRoll;         // raw die roll
    private int moneyChange;     // Deltas for currencies
    private int creditChange;
    private int rankChange;
    private boolean sceneWrapped; // Did the scene finish?
    private String denialReason;  // Why they were denied a role

    //upgrade phase varibles 
    private List<Integer> affordableRanks;
    private List<Integer> unaffordableRanks;
    private int targetRankUpgrade;
    private int dollarCost;
    private int creditCost;
    private boolean canPayDollars;
    private boolean canPayCredits;

    //move phase
    private List<Room> adjacentRooms;

    //core builder
    public Packet(Player player, Room location, Board board, List<String> availableActions, EventType lastEvent) {
        this.player = player;
        this.location = location;
        this.board = board;
        this.availableActions = availableActions;
        this.lastEvent = lastEvent;
    }
    //basic getters
    public Player getPlayer() { return player; }
    public Room getLocation() { return location; }
    public Board getBoard() { return board; }
    public List<String> getAvailableActions() { return availableActions; }
    public EventType getLastEvent() { return lastEvent; }

    //move data builder
    public void setMoveData(Room targetLocation, card currentCard) {
        this.targetLocation = targetLocation;
        this.currentCard = currentCard;
    }
    //move getters
    public Room getTargetLocation() { return targetLocation; }
    public card getCurrentCard() { return currentCard; }

    // role data builder
    public void setRoleData(role targetRole) {
        this.targetRole = targetRole;
    }
    public void setAvailableRoles(List<role> availableRoles) {
        this.availableRoles = availableRoles;
    }
    public List<role> getAvailableRoles() { return availableRoles; }

    //role outcome builder
    public void setRoleOutcomeData(role targetRole, String denialReason) {
        this.targetRole = targetRole;
        this.denialReason = denialReason;
    }
    public role getTargetRole() {return targetRole; }
    public String getDenialReason() { return denialReason; }
   
    //act builder
    public void setActData(int rawRoll, int moneyChange, int creditChange, boolean sceneWrapped) {
        this.rawRoll = rawRoll;
        this.moneyChange = moneyChange;
        this.creditChange = creditChange;
        this.sceneWrapped = sceneWrapped;
    }
    public int getRawRoll() { return rawRoll; }
    public int getMoneyChange() { return moneyChange; }
    public int getCreditChange() { return creditChange; }
    public boolean isSceneWrapped() { return sceneWrapped; }

    public void setUpgradeData(int rankChange, int moneyChange, int creditChange) {
        this.rankChange = rankChange;
        this.moneyChange = moneyChange;
        this.creditChange = creditChange;
    }
    public int getRankChange() { return rankChange; }

    //upgrade packket builder
    public void setUpgradeQueryRankData(List<Integer> affordableRanks, List<Integer> unaffordableRanks) {
        this.affordableRanks = affordableRanks;
        this.unaffordableRanks = unaffordableRanks;
    }
    public List<Integer> getAffordableRanks() { return affordableRanks; }
    public List<Integer> getUnaffordableRanks() { return unaffordableRanks; }

    //upgrade payment type builder
    public void setUpgradeQueryPaymentData(int targetRankUpgrade, int dollarCost, int creditCost, boolean canPayDollars, boolean canPayCredits) {
        this.targetRankUpgrade = targetRankUpgrade;
        this.dollarCost = dollarCost;
        this.creditCost = creditCost;
        this.canPayDollars = canPayDollars;
        this.canPayCredits = canPayCredits;
    }
    public int getTargetRankUpgrade() { return targetRankUpgrade; }
    public int getDollarCost() { return dollarCost; }
    public int getCreditCost() { return creditCost; }
    public boolean canPayDollars() { return canPayDollars; }
    public boolean canPayCredits() { return canPayCredits; }


    //move phase builder
    
    // move query builders & getters
    public void setMoveQueryData(List<Room> adjacentRooms) {
        this.adjacentRooms = adjacentRooms;
    }
    public List<Room> getAdjacentRooms() { return adjacentRooms; }
}


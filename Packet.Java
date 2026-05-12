import java.util.List;

public class Packet{
    //defines exactly what semantic event just occurred in the logic layer
    public enum EventType {
        TURN_START,
        MOVED,
        TOOK_ROLE,
        ACT_SUCCESS,
        ACT_FAIL,
        REHEARSED,
        UPGRADED,
        INVALID_ACTION
    }

    //game info 
    private Player player;
    private Room location;
    private Board board;
    private List<String> availableActions;
    private EventType lastEvent;
    
    // context fields depending on the event type
    private Room targetLocation; // Where they moved to
    private role targetRole;     // What role they took
    private int rawRoll;         // The raw die roll
    private int moneyChange;     // Deltas for currencies
    private int creditChange;
    private int rankChange;
    private boolean sceneWrapped; // Did the scene finish?

    public Packet(Player player, Room location, Board board, List<String> availableActions, EventType lastEvent) {
        this.player = player;
        this.location = location;
        this.board = board;
        this.availableActions = availableActions;
        this.lastEvent = lastEvent;
    }

    public Player getPlayer() { return player; }
    public Room getLocation() { return location; }
    public Board getBoard() { return board; }
    public List<String> getAvailableActions() { return availableActions; }
    public EventType getLastEvent() { return lastEvent; }

    public void setMoveData(Room targetLocation) {
        this.targetLocation = targetLocation;
    }
    public Room getTargetLocation() { return targetLocation; }

    public void setRoleData(role targetRole) {
        this.targetRole = targetRole;
    }
    public role getTargetRole() { return targetRole; }

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
}
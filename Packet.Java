public class Packet{
    private Player player;
    private Room location;;
    private Board board;
    private List<actionOptions> actionOptions;

    public Packet(Player player, Room location, Board board, List<actionOptions> actionOptions) {
        this.player = player;
        this.location = location;
        this.board = board;
        this.actionOptions = actionOptions;
    }
    public Packet(Player player, Room location, Board board, List<actionOptions> actionOptions) {
        this(player, location, board, null, actionOptions);
    }
    public Player getPlayer() {
        return player;
    }

    public Room getLocation() {
        return location;
    }

    public Board getBoard() {
        return board;
    }

    public List<String> getAvailableActions() {
        return availableActions;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    // --- Setters if a manager needs to modify an existing packet ---
    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }
    
    public void setAvailableActions(List<String> availableActions) {
        this.availableActions = availableActions;
    }
}
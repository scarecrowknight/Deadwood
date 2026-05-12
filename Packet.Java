public class Packet{
    private Player player;
    private room location;;
    private board board;
    private list<actionOptions> actionOptions;

    public packet (Player player, room location, board board, list<actionOptions> actionOptions) {
        this.player = player;
        this.location = location;
        this.board = board;
        this.actionOptions = actionOptions;
    }
}
import java.util.ArrayList;
import java.util.List;

public class MoveManager {
    private Board board;
    private View view;

    public MoveManager(Board board, View view) {
        this.board = board;
        this.view = view;
    }
    // move logic sequence
    public boolean reallyMove(Player player) {
        Room currentLoc = player.currentLocation();
        //retrieve accessible adjacent rooms
        ArrayList<Room> adjRooms = currentLoc.getAdjacent();
        
        //build string options list for the view menu
        List<String> roomOptions = new ArrayList<>();
        for (Room r : adjRooms) {
            roomOptions.add(r.getName());
        }
        roomOptions.add("Cancel");
        //query target destination room
        Packet destPacket = new Packet(player, currentLoc, board, roomOptions, Packet.EventType.QUERY_DESTINATION);
        destPacket.setMoveQueryData(adjRooms); // attach raw object list for rich UI rendering
        
        String chosenRoomName = view.renderAndRequestAction(destPacket).trim();
        if (chosenRoomName.equalsIgnoreCase("cancel")) {
            return false;
        }
        Room destination = null;
        for (Room r : adjRooms) {
            if (r.getName().equalsIgnoreCase(chosenRoomName)) {
                destination = r;
                break;
            }
        }
        //validate destination choice
        if (destination == null) {
            //unrecognized input typed recurse to restart destination
            return reallyMove(player);
        }
        // execute final move state updates
        player.SetLocation(destination);
        Card cardToReveal = null;
        // check if arrived at a set with an active scene card
        if (destination instanceof Set) {
            Set destSet = (Set) destination;
            if (destSet.getActiveCard() != null) {
                cardToReveal = destSet.getActiveCard();
                // if card has a setFaceUp(true) tracking method, invoke it natively here
            }
        }
        // dispatch confirmed move packet to update UI summaries
        Packet moveConfirm = new Packet(player, destination, board, null, Packet.EventType.MOVED);
        moveConfirm.setMoveData(destination, cardToReveal);
        view.render(moveConfirm);
        if (destination instanceof Set) {
        	view.printAvailableRoles((Set) destination);
        }
        return true;
	
}
}

import java.util.ArrayList;
import java.util.List;

public class MoveManager {
    private Board board;
    private GuiView view;

    public MoveManager(Board board, GuiView view) {
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
        Packet moveConfirm = new Packet(player, destination, currentLoc, board, null, Packet.EventType.MOVED);
        moveConfirm.setMoveData(destination, null); // no card data to reveal on move
        view.render(moveConfirm); // announce move to view
        if(destination instanceof  Set){
            Set destSet = (Set) destination;
            Card activeCard = destSet.getActiveCard();
            if(activeCard != null) {
                Packet revealPacket = new Packet(player, destination, board, null, Packet.EventType.SCENE_REVEALED);
                revealPacket.setMoveData(destination, activeCard);
                view.render(revealPacket);
        }
        view.printAvailableRoles(destSet);
        }
        return true;
	
}
}

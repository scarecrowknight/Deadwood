import java.util.ArrayList;
import java.util.List;

public class TakeRoleManager {
    private Board board;
    private View view;

    public TakeRoleManager(Board board, View view) {
        this.board = board;
        this.view = view;
    }
    public void TakeRole(Player currentPlayer) {
                // take role: Scans current set of unoccupied off card and on card roles. Filters
            	//            out roles player cant take. If a valid role is picked,
            	//            role is assigned to player, marked occupied, and turn ends
                Room loc = currentPlayer.currentLocation();
                Set currentSet = (Set) loc;
                List<Role> availableRoles = new ArrayList<>();
                List<String> roleNames = new ArrayList<>();
                
                view.showMessage(currentPlayer.getName() + "'s rank is:" + currentPlayer.getRank() + ".");
                // get all unoccupied off card roles the player has rank for
                if(currentSet.getOnCardroles() != null) {
                	for(Role r : currentSet.getOnCardroles()) {
                		if(!r.getOccupied() && currentPlayer.getRank()>= r.getRank()) {
                			availableRoles.add(r);
                			roleNames.add(r.getName() + " (Req. Rank " + r.getRank() + ")");
                		}
                	}
                }
                if(currentSet.getOffCardRoles() != null) {
                	for(Role r : currentSet.getOffCardRoles())
                		if(!r.getOccupied() && currentPlayer.getRank() >= r.getRank()) {
                			availableRoles.add(r);
                			roleNames.add(r.getName() + " (Req. Rank " + r.getRank() + ")");
                		}
                }
                //if no roles for players current rank
                if(availableRoles.isEmpty()) {
                	view.showMessage("Your rank is too low for that.");
                	return;
                }
                
                roleNames.add("Cancel");
                
                //Query for the user's wanted role
                Packet roleQuery = new Packet(currentPlayer, loc, board, roleNames, Packet.EventType.QUERY_TAKE_ROLE);
                String roleChoice = view.renderAndRequestAction(roleQuery);
                
                if(roleChoice.equalsIgnoreCase("cancel")) {
                	return;
                }
                
                //find the role they typed
                Role chosenR = null;
                for (Role r : availableRoles) {
                        String expectedButtonText = r.getName() + " (Req. Rank " + r.getRank() + ")";
                		if (expectedButtonText.equalsIgnoreCase(roleChoice)) {
                            chosenR = r;
                            break;
                    }
                }
                
                if(chosenR != null) {
                	currentPlayer.setRole(chosenR);
                	chosenR.setOccupied(true);
                	//role taken = turn over.
                	//turnComplete = true;
                	
                	Packet tookRole = new Packet(currentPlayer, loc, board, null, Packet.EventType.TOOK_ROLE);
                	tookRole.setRoleData(chosenR);
                	view.render(tookRole);
                } else {
                	Packet invalid = new Packet(currentPlayer, loc, board, null, Packet.EventType.INVALID_ACTION);
                	view.render(invalid);
                }
            }
        }
        
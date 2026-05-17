import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkManager {
    private Board board;
    private View view;
    private List<Player> allPlayers; // Add this

    // Update constructor to take the player list
    public WorkManager(Board board, View view, List<Player> allPlayers) {
        this.board = board;
        this.view = view;
        this.allPlayers = allPlayers;
    }

    public void work(Player player) {
        Set currentSet = (Set) player.currentLocation();
        Role currentRole = player.getRole();
        Card activeCard = currentSet.getActiveCard();

        boolean canRehearse = player.getPracticeChips() < (activeCard.getBudget() - 1);

        List<String> options = new ArrayList<>();
        options.add("Act");
        if (canRehearse) {
            options.add("Rehearse");
        }

        //query the user with DTO
        Packet workQuery = new Packet(player, currentSet, board, options, Packet.EventType.QUERY_WORK);
        String action = view.renderAndRequestAction(workQuery).toLowerCase().trim();

        if (action.equals("rehearse") && canRehearse) {
            rehearse(player, currentSet);
        } else if (action.equals("act")) {
            act(player, currentSet, activeCard, currentRole);
        } else {
            //unrecognized input packet
            Packet invalid = new Packet(player, currentSet, board, null, Packet.EventType.INVALID_ACTION);
            view.render(invalid);
            work(player);
        }
    }

    private void rehearse(Player player, Set set) {
        player.setPracticeChips(player.getPracticeChips() + 1); 
        
        Packet update = new Packet(player, set, board, null, Packet.EventType.REHEARSED);
        view.render(update);
    }

    private void act(Player player, Set set, Card card, Role role) {
        //roll dice
        int roll = (int) (Math.random() * 6) + 1;
        int total = roll + player.getPracticeChips();
        boolean success = (total >= card.getBudget());

        int moneyDelta = 0;
        int creditDelta = 0;
        boolean sceneWrapped = false;

        if (success) {
            //succsess
            set.setShotCount(set.getShotCount() - 1);
            sceneWrapped = (set.getShotCount() <= 0);

            //calculate rewards
            if (role.getStarringRoll()) { 
                creditDelta = 2;
                player.setCredits(player.getCredits() + creditDelta);
            } else { 
                moneyDelta = 1;
                creditDelta = 1;
                player.setMoney(player.getMoney() + moneyDelta);
                player.setCredits(player.getCredits() + creditDelta);
            }
            
            //outcome packet
            Packet outcome = new Packet(player, set, board, null, Packet.EventType.ACT_SUCCESS);
            outcome.setActData(roll, moneyDelta, creditDelta, sceneWrapped);
            view.render(outcome);

            // Trigger wrap if shots hit 0
            if (sceneWrapped) {
                wrapScene(set, card);
            }

        } else {
            //fail
            if (!role.getStarringRoll()) {
                moneyDelta = 1;
                player.setMoney(player.getMoney() + moneyDelta);
            }
            
            Packet outcome = new Packet(player, set, board, null, Packet.EventType.ACT_FAIL);
            //packet for failure
            outcome.setActData(roll, moneyDelta, creditDelta, false);
            view.render(outcome);
        }
    }

       private void wrapScene(Set set, Card card) {
        //need to implement
    
    }
}
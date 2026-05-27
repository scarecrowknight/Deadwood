import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkManager {
    private Board board;
    private GuiView view;
    private List<Player> allPlayers; // Add this

    // Update constructor to take the player list
    public WorkManager(Board board, GuiView view, List<Player> allPlayers) {
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
            //success
            set.setShotCount(set.getShotCount() - 1);
            sceneWrapped = (set.getShotCount() <= 0);

            //calculate rewards
            
            if (role.getStarringRole()) { 
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
            if (!role.getStarringRole()) {
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
        List<Player> playersOnSet = new ArrayList<>();
        List<Player> onCardPlayers = new ArrayList<>();
        List<Player> offCardPlayers = new ArrayList<>();

        for (Player p : allPlayers) {
            if (p.currentLocation() == set && p.getRole() != null) {
                playersOnSet.add(p);
                if (p.getRole().getStarringRole()) {
                    onCardPlayers.add(p);
                } else {
                    offCardPlayers.add(p);
                }
            }
        }

        //check for bonuses
        if (!onCardPlayers.isEmpty()) {
            
            //payouts
            int budget = card.getBudget();
            List<Integer> diceRolls = new ArrayList<>();
            
            //roll dice equal to the budget
            for (int i = 0; i < budget; i++) {
                diceRolls.add((int) (Math.random() * 6) + 1);
            }
            // Sort rolls highest to lowest
            diceRolls.sort(Collections.reverseOrder());

            List<Role> onCardRoles = new ArrayList<>(card.getRoles());
            onCardRoles.sort((r1, r2) -> Integer.compare(r2.getRank(), r1.getRank()));
            for (int i = 0; i < diceRolls.size(); i++) {
                int roll = diceRolls.get(i);
                Role roleToReceive = onCardRoles.get(i % onCardRoles.size());

                for (Player p : onCardPlayers) {
                    if (p.getRole() == roleToReceive) {
                        p.setMoney(p.getMoney() + roll);
                        break;
                    }
                }
            }

            //extras bonus
            for (Player p : offCardPlayers) {
                int bonus = p.getRole().getRank();
                p.setMoney(p.getMoney() + bonus);
            }
        }

        //clear roles and practice chips for all players on the set
        for (Player p : playersOnSet) {
            p.setRole(null);
            p.setPracticeChips(0);
        }
        //remove the card from the board
        set.setActiveCard(null);

        Packet wrapPacket = new Packet(null, set, board, null, Packet.EventType.SCENE_WRAPPED); 
        view.render(wrapPacket);
    }
}
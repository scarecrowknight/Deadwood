import java.util.ArrayList;
import java.util.List;

public class UpgradeManager {
    private Board board;
    private view view;

    public UpgradeManager(Board board, view view) {
        this.board = board;
        this.view = view;
    }
    //upgrade
    public void upgrade(Player player) {
        castingOffice office = (castingOffice) player.currentLocation();
        int currentRank = player.getRank();

        //find what ranks the player has surpassed, what they can afford viab credits and dollars, and what they canty afford
        List<Integer> affordableRanks = new ArrayList<>();
        List<Integer> unaffordableRanks = new ArrayList<>();

        for (int r = currentRank + 1; r <= 6; r++) {
            int dCost = office.dollarCost(r);
            int cCost = office.creditCost(r);
            
            // If the player holds enough of EITHER currency, the tier is affordable
            if (player.getMoney() >= dCost || player.getCredits() >= cCost) {
                affordableRanks.add(r);
            } else {
                unaffordableRanks.add(r);
            }
        }

        //build list containing the possible ranks + cancel option to be sent out via packet
        List<String> rankOptions = new ArrayList<>();
        for (int r : affordableRanks) {
            rankOptions.add(String.valueOf(r));
        }
        rankOptions.add("Cancel");

        //send packet and recieve response. 
        Packet rankAsk = new Packet(player, office, board, rankOptions, Packet.EventType.QUERY_UPGRADE_RANK);
        rankAsk.setUpgradeQueryRankData(affordableRanks, unaffordableRanks);
        
        String rankInput = view.renderAndRequestAction(rankAsk).trim();
        if (rankInput.equalsIgnoreCase("cancel")) return;

        int targetRank;
        try {
            targetRank = Integer.parseInt(rankInput);
            if (!affordableRanks.contains(targetRank)) {
                upgrade(player); //recurse if invalid string bypassed constraints.
                return;
            }
        } catch (NumberFormatException e) {
            upgrade(player);
            return;
        }

        //find payment constraints/options and build the payment options list
        int dCost = office.dollarCost(targetRank);
        int cCost = office.creditCost(targetRank);
        boolean canPayD = player.getMoney() >= dCost;
        boolean canPayC = player.getCredits() >= cCost;

        List<String> paymentOptions = new ArrayList<>();
        if (canPayD) paymentOptions.add("Dollars");
        if (canPayC) paymentOptions.add("Credits");
        paymentOptions.add("Cancel");

        //send second packet containing the payment options
        Packet paymentAsk = new Packet(player, office, board, paymentOptions, Packet.EventType.QUERY_UPGRADE_PAYMENT);
        paymentAsk.setUpgradeQueryPaymentData(targetRank, dCost, cCost, canPayD, canPayC);

        String paymentChoice = view.renderAndRequestAction(paymentAsk).toLowerCase().trim();
        if (paymentChoice.equals("cancel")) {
            //recurse to upgrade if they cancle at this step
            upgrade(player);
            return;
        }

        //finalized transaction logic
        boolean success = false;
        int spentMoney = 0;
        int spentCredits = 0;

        if (paymentChoice.equals("dollars") && canPayD) {
            player.setMoney(player.getMoney() - dCost);
            spentMoney = -dCost;
            success = true;
        } else if (paymentChoice.equals("credits") && canPayC) {
            player.setCredits(player.getCredits() - cCost);
            spentCredits = -cCost;
            success = true;
        }

        if (success) {
            int rankGain = targetRank - player.getRank();
            player.setRank(targetRank);
            
            Packet confirm = new Packet(player, office, board, null, Packet.EventType.UPGRADED);
            confirm.setUpgradeData(rankGain, spentMoney, spentCredits);
            view.render(confirm);
        } else {
            upgrade(player);
        }
    }
}

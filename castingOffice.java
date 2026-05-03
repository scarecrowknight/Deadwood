public class castingOffice extends Room {
	private String name;
	private int playerCredits;
	private int playerDollars;
	private int desiredRank;
	private int playerRank;
	int[][] RankUpgradeCost = {
		{4, 5}, // dollar/credit rank 2
		{10, 10},
		{18, 15},
		{28, 20},
		{40, 25}
};
	public castingOffice(String name, int playerRank, int playerCredits, int playerDollars, int desiredRank) {
		this.name = name;
		this.playerCredits = playerCredits;
		this.playerDollars = playerDollars;
		this.desiredRank = desiredRank;
		this.playerRank =  playerRank;

	}
	
	public int dollarCost() {
		return rankUpgradeCost = [desiredRank - 2][0];
	}
	
	public int creditCost() {
		return rankUpgradeCost = [desiredRank - 2][1];
	}
	
	public boolean isValidUpgrade(int desiredRank, int playerRank) {
		return desiredRank > playerRank;
	}
	
	
}
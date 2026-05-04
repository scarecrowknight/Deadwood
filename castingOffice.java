public class castingOffice extends Room {
	private int playerCredits;
	private int playerDollars;
	private int desiredRank;
	private int playerRank;
	int[][] rankUpgradeCost = {
		{4, 5}, // dollar/credit rank 2
		{10, 10},
		{18, 15},
		{28, 20},
		{40, 25}
};
	public castingOffice(String name, Board board, int playerRank, int playerCredits, int playerDollars, int desiredRank) {
		super(name, board);
		this.playerCredits = playerCredits;
		this.playerDollars = playerDollars;
		this.desiredRank = desiredRank;
		this.playerRank =  playerRank;

	}
	
	public int dollarCost(int desiredRank) {
		this.desiredRank = desiredRank;
		return rankUpgradeCost[desiredRank - 2][0];
	}
	
	public int creditCost(int desiredRank) {
		this.desiredRank = desiredRank;
		return rankUpgradeCost[desiredRank - 2][1];
	}
	
	public boolean isValidUpgrade(int desiredRank, int playerRank) {
		return desiredRank > playerRank;
	}
	
	
}
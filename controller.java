public class controller{
	private gameOverManager gameOverManager;
	
	public void updateGame(int day, int playerCount) {
		gameOverManager.endGame(day, playerCount);
	}
}
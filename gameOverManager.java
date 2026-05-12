public class gameOverManager{
	
	public boolean isGameOver() {
		turnManager turnManager = new turnManager();
		if (turnManager.daysLeft() <= 0)  {
			return true;
		}
		return false;
	}
	
	public void endGame(int day, int playerCount) {
			System.out.println("THE GAME IS OVER");
			System.exit(0);
}}
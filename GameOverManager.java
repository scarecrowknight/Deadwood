public class GameOverManager{
	
	public boolean isGameOver(int day, int playerCount) {
		if (playerCount < 4) {
			return day > 3;
		}
		return day > 4;
	}
	
	public void endGame(int day, int playerCount) {
		if (isGameOver(day, playerCount)) {
			System.out.println("THE GAME IS OVER"); // needs real end game logic
		}
	}
}
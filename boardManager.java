public class boardManager{
	private Board board;
	private boolean dayOver;
	
	public boardManager(Board board) {
		this.board = board;
	}
	public Board getBoard() {
		return board;
	}
	public void dealScenes() {
		// deals 10 new scenes to the board
	}
	public void replaceShots() {
		// replaces all shot counters on the board
	}
	public void resetBoard() {
		// needs logic to reset the board at the end of each day.
		if(dayOver) {
			dealScenes();
			replaceShots();
		}
	}
}
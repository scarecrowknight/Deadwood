public class moveManager{
	public boolean canMove(Player player, Room playerDestination) {
		Room currentLocation = player.currentLocation();
		return currentLocation.getAdjacent().contains(playerDestination);
	}
	public void movePlayer(Player player, Room playerDestination) {
		if(canMove(player, playerDestination)) {
			player.SetLocation(playerDestination);
		}
	}
	
}
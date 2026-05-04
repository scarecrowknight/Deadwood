public class moveManager{
	public boolean canMove(player player, Room playerDestination) {
		Room currentLocation = player.currentLocation();
		return currentLocation.getAdjacent().contains(playerDestination);
	}
	public void movePlayer(player player, Room playerDestination) {
		if(canMove(player, playerDestination)) {
			player.SetLocation(playerDestination);
		}
	}
	
}
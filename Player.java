public class Player{
	private int userNumber = 0;
    private int turnOrder = 0;
    private Role role = null;
    private int money = 0;
    private int credits = 0;
    private int rank = 1;
    private String name = "Player";
    private int practiceChips = 0;
    private Room location; 
    
    public Player(int userNumber, int turnOrder, Role role, int money,int credits, int rank, String name, int practiceChips) {
        this.userNumber = userNumber;
        this.turnOrder = turnOrder;
        this.name = name;

        this.role = role;
        this.money = money;
        this.credits = credits;
        this.rank = rank;
        this.practiceChips = practiceChips;
    }

    public Room currentLocation() {
    	return location;
    }
    public void SetLocation(Room newLocation) {
        if (this.location != null) {
            this.location.removePlayer(this);
        }
        this.location = newLocation;
        if (this.location != null) {
            this.location.addPlayer(this);
        }
    }
     public String getName() {
        return name;
    }
    public int getUserNumber() {
        return userNumber;
    }
    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public int getTurnOrder() {
        return turnOrder;
    }
    public void setTurnOrder(int turnOrder) {
        this.turnOrder = turnOrder;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }

    public int getCredits() {
        return credits;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }


    public int getPracticeChips() {
        return practiceChips;
    }
    public void setPracticeChips(int practiceChips) {
        this.practiceChips = practiceChips;
    }
//commented out until location class is implemented
   /*public void getLocation() {
        return location;
    }
    public void setLocation(location location) {
        this.location = location;
    }*/
}


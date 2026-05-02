public class player{
	private int userNumber = 0;
    private int turnOrder = 0;
    private role role = null;
    private int money = 0;
    private String name = "Player";
    private int score = 0;
    private int practiceChips = 0;
    //private location location = Trailer; /* commented out until location class is implemented */ //auto set to trailer as that is starting location no matter what

    public player(int userNumber, int turnOrder, role role, int money, String name, int score, int practiceChips) {
        this.userNumber = userNumber;
        this.turnOrder = turnOrder;
        this.name = name;

        this.role = role;
        this.money = money;
        
        this.score = score;
        this.practiceChips = practiceChips;
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

    public role getRole() {
        return role;
    }
    public void setRole(role role) {
        this.role = role;
    }

    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
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


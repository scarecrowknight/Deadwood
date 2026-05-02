public class role {
	
	private String name;
    private String quote;
    private int rank;
    private boolean occupied = false; //default state false
    private boolean staringRoll;

    //private boolean reward type? or maybe private enum reward type with starting + extra? idk if reward increase is static, check later


	public role(String name, String quote, int rank, boolean occupied, boolean staringRoll){

        this.name = name;
        this.quote = quote;
        this.rank = rank;
        this.occupied = occupied;
        this.staringRoll = staringRoll;
    }
	
	public String getName(){
        return name;
    }
    public String getQuote(){
        return quote;
    }
	public int getRank(){
        return rank;
    }
	public boolean getOccupied(){
        return occupied;
    }
	public boolean getStarringRoll(){
        return staringRoll;
    }
    public void setOccupied(boolean x){
        if (x = true){
            this.occupied = true;
        } else{
            this.occupied=false;
        }
    }
}
public class Role {
	
	private String name;
    private String quote;
    private int rank;
    private boolean occupied = false; //default state false
    private boolean staringRoll;

    //private boolean reward type? or maybe private enum reward type with starting + extra? idk if reward increase is static, check later
    //edit to last idea, unsure if reward type/amount should even be handled here or if we should deal with somewhere else since it really isnt a part of the role as much as the room/location and what else is going on (i.e. where players are and what rolls they have)


	public Role(String name, String quote, int rank, boolean occupied, boolean staringRoll){

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
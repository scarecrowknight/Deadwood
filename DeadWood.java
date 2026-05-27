import org.w3c.dom.Document;


public class DeadWood {

   private Board board;
   private SceneDeck deck;
    
    private void makeSceneDeck(){
      XMLCardParser p = new XMLCardParser();
      String filename = "cards.xml";
      SceneDeck deck = new SceneDeck();
      
      try{
         Document d = p.getDocFromFile(filename);
         p.readCards(d, deck);
      } catch(Exception e) {
         System.out.println(":c");
         e.printStackTrace();
      }
      this.deck = deck;
    }
    
    private void makeBoard(){
      XMLBoardParser p = new XMLBoardParser();
      Board b = new Board();
      String filename = "board.xml";
      try{
         Document d = p.getDocFromFile(filename);
         p.readRooms(d, b);
      } catch(Exception e) {
         System.out.println(":c");
         e.printStackTrace();
      }
      this.board = b;
    }
    
    public Board getBoard(){
      return this.board;
    }
    public SceneDeck getDeck(){
      return this.deck;
    }
    
    public DeadWood(){
      this.makeBoard();
      this.makeSceneDeck();
    }
    
    public static void main(String[] args) {
        //(Model)
    	DeadWood gameSetup = new DeadWood();
    	Board gameBoard = gameSetup.getBoard();
    
    	//(View)
    	GuiView gameView = new GuiView();
    	
    	//(Controller) 
    	boolean playerIsReady = gameView.askStart();
    	
    	if(playerIsReady) {
    		
    		gameView.showMessage("\nGame is starting...");
    		
    		GameManager gameManager = new GameManager(gameBoard, gameView);
    		
    		SceneDeck fullDeck = gameSetup.getDeck();
    		gameManager.setSceneDeck(fullDeck);
    		//player setup
    		gameManager.addPlayers();
    		gameManager.randomizeTurnOrder();
    		
    		//game starts
    		gameManager.runGame();
    	} else {
    		gameView.showMessage("You're lame anyway. Goodbye! ");
    	}
      
    }
    
}
import java.util.ArrayList;
import java.util.Collections;


public class SceneDeck{
	private ArrayList<Card> deck;
	
	public SceneDeck() {
		deck = new ArrayList<Card>();
	}

	public void shuffleDeck() {
		Collections.shuffle(deck);
	}
	
   public void add(Card card){
      deck.add(card);
   }
   
	public Card draw(){
		if (deck.isEmpty()) {
			System.out.print("The deck is empty.");
			return null;
		}
		return deck.remove(0);
	}
}
import java.util.ArrayList;
import java.util.Collections;

import javax.smartcardio.Card;

public class SceneDeck{
	private ArrayList<Card> deck;
	
	public SceneDeck() {
		deck = new ArrayList<Card>();
	}

	public void shuffleDeck() {
		Collections.shuffle(deck);
	}
	
	public Card drawCard() {
		if (deck.isEmpty()) {
			System.out.print("The deck is empty.");
			return null;
		}
		return deck.remove(0);
	}
}
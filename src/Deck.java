import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a shuffled deck of standard playing cards drawn without replacement.
 */
public class Deck {
    private final List<Card> cards;
    private int nextCardIndex;

    /**
     * Creates and shuffles a standard deck containing 52 distinct cards.
     */
    public Deck() {
        cards = new ArrayList<>();
        for (String suit : Card.validSuits()) {
            for (String rank : Card.validRanks()) {
                cards.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(cards);
        nextCardIndex = 0;
    }

    /**
     * Deals the next card without returning it to the deck.
     *
     * @return the next card in the shuffled deck
     * @throws IllegalStateException if the deck has no cards remaining
     */
    public Card drawCard() {
        if (nextCardIndex >= cards.size()) {
            throw new IllegalStateException("There are no more cards in the deck.");
        }
        Card card = cards.get(nextCardIndex);
        nextCardIndex++;
        return card;
    }

    /**
     * Counts how many cards have not yet been dealt.
     *
     * @return the number of cards remaining in the deck
     */
    public int remainingCards() {
        return cards.size() - nextCardIndex;
    }
}


import java.util.Arrays;

/**
 * Represents one card from a standard 52-card deck.
 */
public class Card {
    private static final String[] VALID_RANKS = {
            "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"
    };
    private static final String[] VALID_SUITS = {"H", "D", "C", "S"};

    private static final int ACE_LOW_VALUE = 1;
    private static final int FACE_CARD_VALUE = 10;

    private final String rank;
    private final String suit;

    /**
     * Creates a card with a valid rank and suit.
     *
     * @param rank the card rank, from A through K
     * @param suit the one-letter suit code (H, D, C, or S)
     * @throws IllegalArgumentException if the rank or suit is invalid
     */
    public Card(String rank, String suit) {
        if (!isValidRank(rank)) {
            throw new IllegalArgumentException("Invalid card rank: " + rank);
        }
        if (!isValidSuit(suit)) {
            throw new IllegalArgumentException("Invalid card suit: " + suit);
        }
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Returns the valid rank codes for constructing a deck.
     *
     * @return a copy of the supported rank codes
     */
    static String[] validRanks() {
        return VALID_RANKS.clone();
    }

    /**
     * Returns the valid suit codes for constructing a deck.
     *
     * @return a copy of the supported suit codes
     */
    static String[] validSuits() {
        return VALID_SUITS.clone();
    }

    /**
     * Checks whether a string is a standard card rank.
     *
     * @param candidate the rank to check
     * @return true if the rank is supported
     */
    private static boolean isValidRank(String candidate) {
        return Arrays.asList(VALID_RANKS).contains(candidate);
    }

    /**
     * Checks whether a string is a standard card suit.
     *
     * @param candidate the suit to check
     * @return true if the suit is supported
     */
    private static boolean isValidSuit(String candidate) {
        return Arrays.asList(VALID_SUITS).contains(candidate);
    }

    /**
     * Gets this card's rank.
     *
     * @return the rank code
     */
    public String getRank() {
        return rank;
    }

    /**
     * Gets this card's suit.
     *
     * @return the suit code
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Gets the card's minimum Blackjack value. Aces count as one here;
     * the game decides whether an Ace should be raised to eleven.
     *
     * @return the base value of this card
     */
    public int getValue() {
        if (rank.equals("A")) {
            return ACE_LOW_VALUE;
        }
        if (rank.equals("J") || rank.equals("Q") || rank.equals("K")) {
            return FACE_CARD_VALUE;
        }
        return Integer.parseInt(rank);
    }

    /**
     * Formats this card as its rank followed by its suit.
     *
     * @return the compact card code, such as KH or 3D
     */
    @Override
    public String toString() {
        return rank + suit;
    }
}

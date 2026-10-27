import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Manages one console-based game of Blackjack Solitaire.
 */
public class BlackjackSolitaire {
    private static final int SCORING_SPACE_COUNT = 16;
    private static final int DISCARD_SPACE_COUNT = 4;
    private static final int TOTAL_SPACE_COUNT = SCORING_SPACE_COUNT + DISCARD_SPACE_COUNT;
    private static final int FIRST_SPACE = 1;
    private static final int FIRST_DISCARD_SPACE = SCORING_SPACE_COUNT + FIRST_SPACE;
    private static final int NON_PLAYABLE_POSITION = 0;

    private static final int TARGET_HAND_VALUE = 21;
    private static final int ACE_VALUE_INCREASE = 10;
    private static final int BLACKJACK_CARD_COUNT = 2;
    private static final int BLACKJACK_POINTS = 10;
    private static final int OTHER_TWENTY_ONE_POINTS = 7;
    private static final int TWENTY_POINTS = 5;
    private static final int NINETEEN_POINTS = 4;
    private static final int EIGHTEEN_POINTS = 3;
    private static final int SEVENTEEN_POINTS = 2;
    private static final int LOW_HAND_POINTS = 1;
    private static final int BUST_POINTS = 0;

    // The five displayed columns are defined by the row width, not by loop limits.
    // Zero marks a non-playable display cell; numbers 1-16 are scoring positions.
    private static final int[][] BOARD_POSITIONS = {
            {1, 2, 3, 4, 5},
            {6, 7, 8, 9, 10},
            {0, 11, 12, 13, 0},
            {0, 14, 15, 16, 0}
    };

    // Board positions are one-based, so index zero is unused.
    // Discard positions 17-20 have their own array and never affect the score.
    private final Card[] boardCards = new Card[SCORING_SPACE_COUNT + FIRST_SPACE];
    private final boolean[] discardSpacesUsed = new boolean[DISCARD_SPACE_COUNT];
    private final Deck deck;
    private final Scanner scanner;
    private int scoringSpacesFilled;
    private int discardsUsed;

    /**
     * Creates a game with a fresh deck and standard input.
     */
    public BlackjackSolitaire() {
        this(new Deck(), new Scanner(System.in));
    }

    /**
     * Creates a game with supplied input and deck for testing.
     *
     * @param deck the deck from which cards will be drawn
     * @param scanner the source of the player's position choices
     */
    BlackjackSolitaire(Deck deck, Scanner scanner) {
        this.deck = Objects.requireNonNull(deck, "deck");
        this.scanner = Objects.requireNonNull(scanner, "scanner");
    }

    /**
     * Displays the four-row board with numbered empty positions and placed cards.
     */
    public void displayBoard() {
        for (int[] row : BOARD_POSITIONS) {
            StringBuilder line = new StringBuilder();
            for (int column = 0; column < row.length; column++) {
                if (column > 0) {
                    line.append('\t');
                }
                int position = row[column];
                if (position != NON_PLAYABLE_POSITION) {
                    Card card = boardCards[position];
                    line.append(card == null ? position : card.toString());
                }
            }
            System.out.println(line);
        }
    }

    /**
     * Runs one complete game, ending after all scoring positions are filled.
     */
    public void play() {
        System.out.println("Welcome to Blackjack Solitaire!");

        while (!isGameOver()) {
            displayBoard();
            System.out.println("Discards remaining: " + getDiscardsRemaining());

            Card currentCard = deck.drawCard();
            System.out.println("Current card: " + currentCard);
            Integer position = promptForValidPosition();
            if (position == null) {
                System.out.println("Input ended before the game was completed.");
                return;
            }
            placeCard(currentCard, position);
            System.out.println();
        }

        displayFinalScore();
    }

    /**
     * Reports whether every scoring position has been filled.
     *
     * @return true if the game is complete
     */
    private boolean isGameOver() {
        return scoringSpacesFilled == SCORING_SPACE_COUNT;
    }

    /**
     * Gets the number of discard positions the player has not used.
     *
     * @return the number of remaining discard positions
     */
    private int getDiscardsRemaining() {
        return DISCARD_SPACE_COUNT - discardsUsed;
    }

    /**
     * Asks the player for an unoccupied scoring or discard position.
     *
     * @return a valid position, or null if input has ended
     */
    private Integer promptForValidPosition() {
        while (true) {
            System.out.print("Choose a space (1-" + TOTAL_SPACE_COUNT + "): ");
            if (!scanner.hasNextLine()) {
                return null;
            }

            Integer position = parsePosition(scanner.nextLine());
            if (position == null) {
                System.out.println("Invalid input. Enter a number from "
                        + FIRST_SPACE + " to " + TOTAL_SPACE_COUNT + ".");
                continue;
            }

            String errorMessage = placementError(position);
            if (errorMessage == null) {
                return position;
            }
            System.out.println(errorMessage);
        }
    }

    /**
     * Parses one user input line as a numeric position.
     *
     * @param input the user's input line
     * @return the position number, or null for non-numeric input
     */
    private Integer parsePosition(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    /**
     * Finds the reason a space cannot accept a card.
     *
     * @param position the requested scoring or discard position
     * @return an error message, or null if the space is available
     */
    private String placementError(int position) {
        if (position < FIRST_SPACE || position > TOTAL_SPACE_COUNT) {
            return "Invalid space. Enter a number from "
                    + FIRST_SPACE + " to " + TOTAL_SPACE_COUNT + ".";
        }
        if (position <= SCORING_SPACE_COUNT) {
            return boardCards[position] == null
                    ? null : "That space is already occupied. Try again.";
        }
        return discardSpacesUsed[position - FIRST_DISCARD_SPACE]
                ? "That discard space is already used. Try again." : null;
    }

    /**
     * Places a dealt card at a scoring position or discards it.
     *
     * @param card the card being placed
     * @param position the already-validated position number
     */
    private void placeCard(Card card, int position) {
        if (position <= SCORING_SPACE_COUNT) {
            boardCards[position] = card;
            scoringSpacesFilled++;
        } else {
            discardSpacesUsed[position - FIRST_DISCARD_SPACE] = true;
            discardsUsed++;
            System.out.println("Card discarded.");
        }
    }

    /**
     * Announces the final result after all scoring spaces have been filled.
     */
    private void displayFinalScore() {
        displayBoard();
        System.out.println(
                "All " + SCORING_SPACE_COUNT + " scoring spaces are filled. Calculating score...");
        System.out.println("Game over! You scored " + calculateScore() + " points.");
    }

    /**
     * Adds scores for the four horizontal hands and five vertical hands.
     *
     * @return the final total across all nine hands
     * @throws IllegalStateException if the game has not finished
     */
    public int calculateScore() {
        if (!isGameOver()) {
            throw new IllegalStateException(
                    "Cannot score until all scoring spaces are filled.");
        }

        int totalPoints = 0;
        for (int[] row : BOARD_POSITIONS) {
            totalPoints += scorePositions(row);
        }
        for (int column = 0; column < BOARD_POSITIONS[0].length; column++) {
            totalPoints += scoreColumn(column);
        }
        return totalPoints;
    }

    /**
     * Scores the playable positions within a displayed row.
     *
     * @param row the board row's numbered positions
     * @return the score of the row's hand
     */
    private int scorePositions(int[] row) {
        List<Card> hand = new ArrayList<>();
        for (int position : row) {
            if (position != NON_PLAYABLE_POSITION) {
                hand.add(boardCards[position]);
            }
        }
        return scoreHand(hand);
    }

    /**
     * Scores the playable positions in a displayed column.
     *
     * @param column the zero-based display column index
     * @return the score of the column's hand
     */
    private int scoreColumn(int column) {
        List<Card> hand = new ArrayList<>();
        for (int[] row : BOARD_POSITIONS) {
            int position = row[column];
            if (position != NON_PLAYABLE_POSITION) {
                hand.add(boardCards[position]);
            }
        }
        return scoreHand(hand);
    }

    /**
     * Determines a hand's Blackjack Solitaire score, choosing Ace values
     * that make the total as high as possible without going over 21.
     *
     * @param hand the cards in one complete row or column
     * @return the points earned by that hand
     * @throws IllegalArgumentException if the hand is null or empty
     */
    static int scoreHand(List<Card> hand) {
        if (hand == null || hand.isEmpty()) {
            throw new IllegalArgumentException("A hand must contain cards.");
        }

        int total = 0;
        int acesAvailableToRaise = 0;
        for (Card card : hand) {
            total += card.getValue();
            if (card.getRank().equals("A")) {
                acesAvailableToRaise++;
            }
        }

        while (acesAvailableToRaise > 0
                && total + ACE_VALUE_INCREASE <= TARGET_HAND_VALUE) {
            total += ACE_VALUE_INCREASE;
            acesAvailableToRaise--;
        }

        if (total > TARGET_HAND_VALUE) {
            return BUST_POINTS;
        }
        if (total == TARGET_HAND_VALUE) {
            return hand.size() == BLACKJACK_CARD_COUNT
                    ? BLACKJACK_POINTS : OTHER_TWENTY_ONE_POINTS;
        }
        if (total == TARGET_HAND_VALUE - 1) {
            return TWENTY_POINTS;
        }
        if (total == TARGET_HAND_VALUE - 2) {
            return NINETEEN_POINTS;
        }
        if (total == TARGET_HAND_VALUE - 3) {
            return EIGHTEEN_POINTS;
        }
        if (total == TARGET_HAND_VALUE - 4) {
            return SEVENTEEN_POINTS;
        }
        return LOW_HAND_POINTS;
    }
}


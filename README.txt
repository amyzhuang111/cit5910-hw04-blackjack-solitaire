Blackjack Solitaire

Student: Amy Zhuang
Partner: None
Collaboration with another student: None

1. Overview

Blackjack Solitaire is a card game in Java that uses a shuffled 52-card deck. The player puts cards into 16 scoring spots and can discard up to 4 cards.

The board has 4 rows and 5 columns. The top two rows have 5 spots and the bottom two rows have 3. Each row and column is scored as a separate Blackjack hand. The goal is to get the highest total score.

2. Files

- Card.java - represents one card (rank, suit, value).
- Deck.java - builds and shuffles a 52-card deck.
- BlackjackSolitaire.java - runs the game: turns, placing cards, discards, and scoring.
- BlackjackSolitaireRunner.java - starter code with the main method that launches the game.
- BlackjackSolitaireTest.java - optional tester for checking the game logic on its own.
- README.txt - this file.

3. How to compile and run

From the src folder:
  javac Card.java Deck.java BlackjackSolitaire.java BlackjackSolitaireRunner.java
  java BlackjackSolitaireRunner

You can also run BlackjackSolitaireRunner.main from IntelliJ.

4. How to test

From the src folder:
  javac Card.java Deck.java BlackjackSolitaire.java BlackjackSolitaireTest.java
  java BlackjackSolitaireTest

The tests cover cards, building the deck, legal placements, discards, and hand scoring.

5. Version control

I used Git to track changes. The git log is included with the submission.

6. Submission

Required: BlackjackSolitaire.java, BlackjackSolitaireRunner.java, Card.java, Deck.java, and README.txt.
Optional: BlackjackSolitaireTest.java.
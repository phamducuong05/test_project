package model.core;

import model.core.enums.Rank;
import model.core.enums.Suit;
import model.player.Player;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {
    private Stack<Card> deck;

    public Deck() {
        deck = new Stack<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
    }

    public boolean isEmpty(){
        return deck.isEmpty();
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card drawCard() {
        if (deck.isEmpty()) {
            return null;
        }
        return deck.pop();
    }

    public void dealCards(List<Player> players, int handSize) {
        if (players.size() * handSize > deck.size()) {
            throw new IllegalArgumentException("Not enough cards in deck");
        }

        for (Player player : players) {
            player.receiveCard(deck.pop());
        }
    }


}

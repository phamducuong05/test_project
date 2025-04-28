package model.core.deck;

import model.core.card.Card;
import model.player.Player;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public abstract class Deck<T extends Card, P extends Player<T>> {
    private final Stack<T> deck;

    public Deck() {
        deck = new Stack<>();
        initializeDeck();
    }

    protected abstract void initializeDeck();

    public Stack<T> getDeck() {
        return deck;
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public T drawCard() {
        if (deck.isEmpty()) {
            return null;
        }
        return deck.pop();
    }

    public void dealCards(List<P> players, int handSize) {
        if (players.size() * handSize > deck.size()) {
            throw new IllegalArgumentException("Not enough cards in deck");
        }

        for (P player : players) {
            player.receiveCard(deck.pop());
        }
    }
}

package model.player;

import model.core.card.Card;

import java.util.ArrayList;
import java.util.List;

public class Player<T extends Card> {
    protected List<T> hand;
    protected List<T> selectedCards;
    protected String name;

    public Player() {
        this.hand = new ArrayList<>(); // Khởi tạo hand
        this.selectedCards = new ArrayList<>(); // Khởi tạo selectedCards
        this.name = "Unnamed Player";
    }

    public Player(String name) {
        this.hand = new ArrayList<>();
        this.selectedCards = new ArrayList<>();
        this.name = name;
    }

    public void receiveCard(T card) {
        hand.add(card);
    }

    public int handSize() {
        return hand.size();
    }

    public List<T> getHand() {
        return new ArrayList<>(this.hand);
    }

    public List<T> getSelectedCards() {
        return new ArrayList<>(this.selectedCards);
    }

    public void selectCard(T card) {
        selectedCards.add(card);
    }

    public List<T> playCard() {
        hand.removeAll(selectedCards);
        return new ArrayList<>(this.selectedCards);
    }
}
package model.player;

import model.core.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected List<Card> hand;
    protected List<Card> selectedCards;
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

    public void receiveCard(Card card) {
        hand.add(card);
    }

    public int handSize() {
        return hand.size();
    }

    public List<Card> getHand() {
        return new ArrayList<>(this.hand);
    }

    public List<Card> getSelectedCards() {
        return new ArrayList<>(this.selectedCards);
    }

    public void selectCard(Card card) {
        selectedCards.add(card);
    }

    public List<Card> playCard() {
        hand.removeAll(selectedCards);
        return new ArrayList<>(this.selectedCards);
    }



}
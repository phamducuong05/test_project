package model.player;

import model.core.card.Card;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private List<Card> hand;
    private List<Card> selectedCards;
    private String name;

    public Player() {
        this.hand = new ArrayList<>(); // Khởi tạo hand
        this.selectedCards = new ArrayList<>(); // Khởi tạo selectedCards
        this.name = "Anonymous Player";
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public void setSelectedCards(List<Card> selectedCards) {
        this.selectedCards = selectedCards;
    }



}
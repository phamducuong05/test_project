package model.core.card;

import model.core.enums.*;

public class WestCard extends Card {
    private final Suit suit;
    private final Rank rank;

    public WestCard(Suit suit, Rank rank) {
        super();
        this.suit = suit;
        this.rank = rank;
        this.setName(rank.name() + " of " + suit.name());
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank.name() + " of " + suit.name();
    }
}

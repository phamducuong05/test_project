package model.core.deck;

import model.core.card.WestCard;
import model.core.enums.Rank;
import model.core.enums.Suit;

public class WestCardDeck extends Deck<WestCard> {
    @Override
    protected void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                getDeck().add(new WestCard(suit, rank));
            }
        }
        shuffle();
    }
}

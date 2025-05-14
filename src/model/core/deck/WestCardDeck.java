package model.core.deck;

import model.core.card.WestCard;
import model.core.enums.Rank;
import model.core.enums.Suit;
import model.player.Player;

public class WestCardDeck<P extends Player<WestCard>> extends Deck<WestCard, P> {
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

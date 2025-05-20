package model.phom;

import model.core.card.WestCard;

import java.util.List;

/**
 * Classes representing different player actions in Phom game
 */
public class PhomPlayerAction {
    /**
     * Action for drawing a card from the deck
     */
    public static class DrawCardAction {
        // No additional information needed
    }
    
    /**
     * Action for eating a card from the table
     */
    public static class EatCardAction {
        private final WestCard card;
        
        public EatCardAction(WestCard card) {
            this.card = card;
        }
        
        public WestCard getCard() {
            return card;
        }
    }
    
    /**
     * Action for discarding a card
     */
    public static class DiscardCardAction {
        private final WestCard card;
        
        public DiscardCardAction(WestCard card) {
            this.card = card;
        }
        
        public WestCard getCard() {
            return card;
        }
    }
    
    /**
     * Action for creating a meld (phom)
     */
    public static class MeldAction {
        private final List<WestCard> cards;
        
        public MeldAction(List<WestCard> cards) {
            this.cards = cards;
        }
        
        public List<WestCard> getCards() {
            return cards;
        }
    }
} 
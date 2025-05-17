package model.phom;

import model.core.card.WestCard;
import java.util.*;

public class PhomBotPlayer extends PhomPlayer {
    public PhomBotPlayer(String name) {
        super(name);
    }

    @Override
    public boolean decideToEat(WestCard discardedCard, PhomGameState gameState) {
        if (discardedCard == null) return false;
        
        List<WestCard> originalHand = new ArrayList<>(this.getHand());
        
        int originalPhomCount = findCombinations().size();
        
        this.receiveCard(discardedCard);
        
        int newPhomCount = findCombinations().size();
        
        this.getHand().remove(discardedCard);
        
        return newPhomCount > originalPhomCount;
    }

    @Override
    public WestCard decideDiscard(PhomGameState gameState) {
        // Always try to discard trash cards (not part of any phoms)
        List<List<WestCard>> phoms = findCombinations();
        
        // Flatten all phoms into a single list
        List<WestCard> cardsInPhoms = new ArrayList<>();
        for (List<WestCard> phom : phoms) {
            cardsInPhoms.addAll(phom);
        }
        
        // Find cards not part of any phom (trash cards)
        List<WestCard> trashCards = new ArrayList<>();
        for (WestCard card : this.getHand()) {
            if (!isCardInList(card, cardsInPhoms)) {
                trashCards.add(card);
            }
        }
        
        // If there are trash cards, discard the one with the highest value to minimize the score
        if (!trashCards.isEmpty()) {
            trashCards.sort(Comparator.comparingInt(card -> -card.getRank().getValue())); 
            return trashCards.get(0); 
        }
        
        // If no trash cards, just discard the card with the highest value
        List<WestCard> hand = new ArrayList<>(this.getHand());
        hand.sort(Comparator.comparingInt(card -> -card.getRank().getValue())); 
        return hand.get(0); 
    }

    private boolean isCardInList(WestCard card, List<WestCard> cardList) {
        for (WestCard c : cardList) {
            if (c.equals(card)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<List<WestCard>> decideMelds(PhomGameState gameState) {
        return findCombinations();
    }

    @Override
    public Map<WestCard, List<WestCard>> decideSends(PhomGameState gameState) {
        return new HashMap<>();
    }
}

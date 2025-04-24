package model.phom;

import model.core.enums.Rank;
import model.player.Player;
import model.core.Card;

import java.util.*;

public class PhomPlayer extends Player {
    private int numberOfPhom = 0;

    public PhomPlayer(String name) {
        super(name);
    }


    public List<List<Card>> findCombinations() {
        List<List<Card>> allPhoms = new ArrayList<>();
        if (this.hand == null || this.hand.size() < 3) {
            return allPhoms;
        }
        // Find all combinations by rank
        Map<Rank, List<Card>> rankMap = new HashMap<>();
        for(Rank rank : Rank.values()) {
            for(Card card : this.hand) {
                if(rank == card.getRank()) {
                    if(rankMap.containsKey(rank)) {
                        rankMap.get(rank).add(card);
                    } else {
                        List<Card> cards = new ArrayList<>();
                        cards.add(card);
                        rankMap.put(rank, cards);
                    }
                }
            }
        }
        for(List<Card> cards : rankMap.values()) {
            if(cards.size() >= 3) {
                allPhoms.add(cards);
            }
        }
        // Find all combinations by suit
        Map<String, List<Card>> suitMap = new HashMap<>();
        for(Card card : this.hand) {
            if(suitMap.containsKey(card.getSuit().getValue())) {
                suitMap.get(card.getSuit().getValue()).add(card);
            } else {
                List<Card> cards = new ArrayList<>();
                cards.add(card);
                suitMap.put(card.getSuit().getValue(), cards);
            }
        }
        for(List<Card> cards : suitMap.values()) {
            cards.sort(Comparator.comparingInt(card -> card.getRank().getValue()));
        }

        for(List<Card> cards : suitMap.values()) {
            List<Card> cards1 = new ArrayList<>(); // Các card
            int numberOfConsecutive = 1;
            for(int i = 0; i < cards.size() - 1; i++) {
                if(cards.get(i).getRank().getValue() + 1 == cards.get(i+1).getRank().getValue()) {
                    numberOfConsecutive++;
                }
                else {
                    if(numberOfConsecutive >= 3) {
                        while(numberOfConsecutive >= 0) {
                            cards1.add(cards.get(i-numberOfConsecutive));
                            numberOfConsecutive--;
                        }
                        allPhoms.add(cards1);
                        cards1 = new ArrayList<>();
                    }
                    numberOfConsecutive = 1;
                }
            }
        }
        numberOfPhom = allPhoms.size();
        return allPhoms;
    }

    public boolean drawFromDiscard(Stack<Card> discardCards) {
        findCombinations();
        int tmp = this.numberOfPhom;
        Card newCard = discardCards.pop();
        this.receiveCard(newCard);
        findCombinations();
        if(this.numberOfPhom == tmp) {
            discardCards.push(newCard);
            this.hand.remove(newCard);
            return false;
        }
        return true;
    }

    public void removeCards(List<Card> cards) {
           this.getHand().removeAll(cards);
    }

    public int calculateScore() {
        for(List<Card> cards : findCombinations()) {
            this.removeCards(cards);
        }
        int totalScore = 0;
        for (Card card : this.getHand()) {
            totalScore += card.getRank().getValue();
        }
        return totalScore;
    }

}

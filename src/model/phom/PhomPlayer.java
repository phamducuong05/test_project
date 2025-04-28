package model.phom;

import model.core.card.WestCard;
import model.core.enums.Rank;
import model.player.Player;

import java.util.*;

public class PhomPlayer extends Player<WestCard> {
    private static final int MAX_DISCARD_CARDS = 4;
    private int numberOfPhom;
    private int numberOfDiscardCards;

    public PhomPlayer(String name) {
        super(name);
        numberOfPhom = 0;
        numberOfDiscardCards = 0;
    }


    public List<List<WestCard>> findCombinations() {
        List<List<WestCard>> allPhoms = new ArrayList<>();
        if (this.getHand() == null || this.getHand().size() < 3) {
            return allPhoms;
        }
        // Find all combinations by rank
        Map<Rank, List<WestCard>> rankMap = new HashMap<>();
        for (Rank rank : Rank.values()) {
            for (WestCard card : this.getHand()) {
                if (rank == card.getRank()) {
                    if (rankMap.containsKey(rank)) {
                        rankMap.get(rank).add(card);
                    } else {
                        List<WestCard> cards = new ArrayList<>();
                        cards.add(card);
                        rankMap.put(rank, cards);
                    }
                }
            }
        }
        for (List<WestCard> cards : rankMap.values()) {
            if (cards.size() >= 3) {
                allPhoms.add(cards);
            }
        }
        // Find all combinations by suit
        Map<String, List<WestCard>> suitMap = new HashMap<>();
        for (WestCard card : this.getHand()) {
            if (suitMap.containsKey(card.getSuit().getValue())) {
                suitMap.get(card.getSuit().getValue()).add(card);
            } else {
                List<WestCard> cards = new ArrayList<>();
                cards.add(card);
                suitMap.put(card.getSuit().getValue(), cards);
            }
        }
        for (List<WestCard> cards : suitMap.values()) {
            cards.sort(Comparator.comparingInt(card -> card.getRank().getValue()));
        }

        for (List<WestCard> cards : suitMap.values()) {
            List<WestCard> cards1 = new ArrayList<>(); // Các card
            int numberOfConsecutive = 1;
            for (int i = 0; i < cards.size() - 1; i++) {
                if (cards.get(i).getRank().getValue() + 1 == cards.get(i + 1).getRank().getValue()) {
                    numberOfConsecutive++;
                } else {
                    if (numberOfConsecutive >= 3) {
                        while (numberOfConsecutive >= 0) {
                            cards1.add(cards.get(i - numberOfConsecutive));
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

    public boolean drawFromDiscard(Stack<WestCard> discardCards) {
        findCombinations();
        int tmp = this.numberOfPhom;
        WestCard newCard = discardCards.pop();
        this.receiveCard(newCard);
        findCombinations();
        if (this.numberOfPhom == tmp) {
            discardCards.push(newCard);
            this.getHand().remove(newCard);
            return false;
        }
        return true;
    }

    public void removeCards(List<WestCard> cards) {
        this.getHand().removeAll(cards);
    }

    public int calculateScore() {
        for (List<WestCard> cards : findCombinations()) {
            this.removeCards(cards);
        }
        int totalScore = 0;
        for (WestCard card : this.getHand()) {
            totalScore += card.getRank().getValue();
        }
        return totalScore;
    }

}

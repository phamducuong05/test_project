package model.phom;
import model.core.card.WestCard;
import model.core.enums.Rank;
import model.player.Player;
import java.util.*;

public abstract class PhomPlayer extends Player<WestCard> {
    private List<WestCard> discardCards;
    private int numberOfPhom;
    private List<WestCard> eatenCards;
    private List<List<WestCard>> allPhoms;

    public PhomPlayer() {}

    public PhomPlayer(String name) {
        super(name);
        discardCards = new ArrayList<>();
        numberOfPhom = 0;
        eatenCards = new ArrayList<>();
        allPhoms = new ArrayList<>();
    }

    public List<List<WestCard>> getAllPhoms() {
        return allPhoms;
    }

    public List<WestCard> getDiscardCards() {
        return discardCards;
    }

    public List<WestCard> getEatenCards() {
        return eatenCards;
    }

    public void addDiscardCards(WestCard card) {
        if(card != null)
            this.discardCards.add(card);
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
            for( List<WestCard> cardsTemporary : findAllConsecutive(cards)) {
                allPhoms.add(cardsTemporary);
            }
        }
        numberOfPhom = allPhoms.size();
        return allPhoms;
    }

    public List<List<WestCard>> findAllConsecutive(List<WestCard> cards) {
        List<List<WestCard>> result = new ArrayList<>();
        int numberOfConsecutive = 1;
        List<WestCard> subResult = new ArrayList<>();
        subResult.add(cards.get(0));
        for(int i = 0; i < cards.size() - 1; i++) {
            if(cards.get(i).getRank().getValue() + 1 == cards.get(i + 1).getRank().getValue()) {
                numberOfConsecutive++;
                subResult.add(cards.get(i + 1));
            }
            else{
                if(numberOfConsecutive >= 3) {
                    result.add(subResult);
                }
                subResult = new ArrayList<>();
                subResult.add(cards.get(i + 1));
                numberOfConsecutive = 1;
            }
        }
        if(numberOfConsecutive >= 3) {
            result.add(subResult);
        }
        return result;
    }

    public int getNumberOfPhom() {
        return numberOfPhom;
    }

    public int calculateScore() {
        int score = 0;
        for (List<WestCard> phom : findCombinations()) {
            this.getHand().removeAll(phom);
        }
        for (WestCard card : this.getHand()) {
            score += card.getRank().getValue();
        }
        return score;
    }

    public abstract boolean decideToEat(WestCard discardedCard);
    public abstract WestCard decideDiscard();
    public abstract Map<WestCard, List<WestCard>> decideSends(PhomGameState gameState);
}

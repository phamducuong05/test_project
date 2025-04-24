package games;

import models.Card;
import models.enums.Suit;

import java.util.Comparator;
import java.util.List;

public class TienLenMienBacGameLogic {
    public TienLenMienBacGameLogic() {
    }

    public boolean isSameColor(Card c1, Card c2){
        boolean allRed = (c1.getSuit() == Suit.HEARTS && c2.getSuit() == Suit.DIAMONDS)
                || (c1.getSuit() == Suit.DIAMONDS && c2.getSuit() == Suit.HEARTS);
        boolean allBlack = (c1.getSuit() == Suit.CLUBS && c2.getSuit() == Suit.SPADES)
                || (c1.getSuit() == Suit.SPADES && c2.getSuit() == Suit.CLUBS);
        return allRed || allBlack;
    }

    public boolean isPair(List<Card> selectedCards) {
        return (selectedCards.size() == 2 && selectedCards.get(0).getRank() == selectedCards.get(1).getRank())
                && isSameColor(selectedCards.get(0), selectedCards.get(1));
    }

    public boolean isThreeOfKind(List<Card> selectedCards) {
        return selectedCards.size() == 3
                && selectedCards.get(0).getRank() == selectedCards.get(1).getRank()
                && selectedCards.get(1).getRank() == selectedCards.get(2).getRank();
    }

    public boolean isFourOfKind(List<Card> selectedCards){
        return selectedCards.size() == 4
                && selectedCards.get(0).getRank() == selectedCards.get(1).getRank()
                && selectedCards.get(1).getRank() == selectedCards.get(2).getRank()
                && selectedCards.get(2).getRank() == selectedCards.get(3).getRank();
    }

    public boolean isSequence(List<Card> selectedCards){
        if(selectedCards.size() < 3) return false;
        selectedCards.sort(Comparator.comparing(Card::getRank).thenComparing(Card::getSuit));
        for(int i = 1; i < selectedCards.size(); i++){
            if ((selectedCards.get(i).getRank().getValue() != selectedCards.get(i-1).getRank().getValue() + 1)
                || (selectedCards.get(i).getSuit() != selectedCards.get(i-1).getSuit()))
                return false;
        }
        return true;
    }

}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import model.core.card.WestCard;
import model.core.deck.WestCardDeck;
import model.core.enums.Rank;
import model.core.enums.Suit;
import model.game.TienLenMienBacGameLogic;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        TienLenMienBacGameLogic logic2 = new TienLenMienBacGameLogic();
        WestCardDeck deck = new WestCardDeck();
        WestCardDeck hi = new WestCardDeck();

        WestCard c1 = new WestCard(Suit.HEARTS, Rank.TWO);
        WestCard c11 = new WestCard(Suit.DIAMONDS, Rank.TWO);
        WestCard c13 = new WestCard(Suit.CLUBS, Rank.TWO);
        WestCard c12 = new WestCard(Suit.SPADES, Rank.TWO);
        WestCard c2 = new WestCard(Suit.DIAMONDS, Rank.THREE);
        WestCard c21 = new WestCard(Suit.HEARTS, Rank.THREE);
        WestCard c22 = new WestCard(Suit.CLUBS, Rank.THREE);
        WestCard c23 = new WestCard(Suit.SPADES, Rank.THREE);
        WestCard c31 = new WestCard(Suit.HEARTS, Rank.FOUR);
        WestCard c32 = new WestCard(Suit.DIAMONDS, Rank.FOUR);
        WestCard c33 = new WestCard(Suit.SPADES, Rank.FOUR);
        WestCard c34 = new WestCard(Suit.CLUBS, Rank.FOUR);
        WestCard c41 = new WestCard(Suit.HEARTS, Rank.FIVE);
        WestCard c5 = new WestCard(Suit.HEARTS, Rank.SIX);


        List<WestCard> cardsOnTable = new ArrayList<>(Arrays.asList(c13, c12));
        List<WestCard> cardsSelected = new ArrayList<>(Arrays.asList(c1, c11));

        cardsOnTable.sort(Comparator.comparing(WestCard::getRank).thenComparing(WestCard::getSuit));
        cardsSelected.sort(Comparator.comparing(WestCard::getRank).thenComparing(WestCard::getSuit));
        System.out.println(cardsOnTable);
        System.out.println(cardsSelected);
        System.out.println(logic2.isCounter(cardsOnTable, cardsSelected));
    }
}
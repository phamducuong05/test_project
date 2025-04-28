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
        WestCard c2 = new WestCard(Suit.DIAMONDS, Rank.THREE);
        WestCard c3 = new WestCard(Suit.SPADES, Rank.FOUR);
        WestCard c4 = new WestCard(Suit.CLUBS, Rank.FIVE);
        WestCard c5 = new WestCard(Suit.HEARTS, Rank.ACE);

        List<WestCard> cards = new ArrayList<>(Arrays.asList(c1, c2, c3, c4, c5));
        cards.sort(Comparator.comparing(WestCard::getRank).thenComparing(WestCard::getSuit));
        System.out.println(cards);
//        System.out.println(logic2.compareCards(c2, c5));

    }
}
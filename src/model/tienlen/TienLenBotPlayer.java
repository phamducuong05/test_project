package model.tienlen;

import model.core.card.WestCard;
import model.core.card.WestCardComparator;
import model.core.enums.Rank;
import model.game.TienLenMienBacGameLogic;
import model.player.Player;

import java.util.*;

public class TienLenBotPlayer extends TienLenPlayer {
    private final TienLenMienBacGameLogic gameLogic = new TienLenMienBacGameLogic();

    public TienLenBotPlayer(String name) {
        super(name);
    }

    public boolean isPlaying() {
        return getState() == State.PLAYING;
    }

    public void sortHand(){
        getHand().sort(Comparator.comparing((WestCard c) -> c.getRank().ordinal()).thenComparing((WestCard c) -> c.getSuit().ordinal()));
    }

    /**
     * Tự động đánh bài.
     *
     * Tìm nước đi phù hợp để chặn những quân trên bàn.
     * Nếu có nước phù hợp thì sẽ setSelectedCards và playCard
     * Nếu không có thì skipTurn
     *
     * @param cardsOnTable: Danh sách các quân bài đang ở trên bàn
     */

    public List<WestCard> autoPlay(List<WestCard> cardsOnTable) {
        Helper helper = new Helper();
        List<WestCard> selected = new ArrayList<>();
        // Analyze the current table
        boolean tableIsNone = cardsOnTable.isEmpty();
        boolean tableIsSingle = (cardsOnTable.size() == 1);
        boolean tableIsPair = gameLogic.isPair(cardsOnTable);
        boolean tableIsThree = gameLogic.isThreeOfKind(cardsOnTable);
        boolean tableIsFour = gameLogic.isFourOfKind(cardsOnTable);
        boolean tableIsSequence = gameLogic.isSequence(cardsOnTable);

        cardsOnTable.sort(Comparator.comparing((WestCard c) -> c.getRank().ordinal()).thenComparing((WestCard c) -> c.getSuit().ordinal()));
        sortHand();

        if (tableIsNone) {
            WestCard randomCard = getHand().get(new Random().nextInt(handSize()));
            selected = List.of(randomCard);
        } else if (tableIsSingle) {
            WestCard singleCard = helper.findSingleCard(getHand(), cardsOnTable);
            if (singleCard != null) {
                selected = List.of(singleCard);
            } else if (cardsOnTable.getFirst().getRank() == Rank.TWO) {
                selected = helper.findFour(getHand(), cardsOnTable);
            }
        } else if (tableIsPair) {
            selected = helper.findPair(getHand(), cardsOnTable);
        } else if (tableIsThree) {
            selected = helper.findThree(getHand(), cardsOnTable);
        } else if (tableIsFour) {
            selected = helper.findFour(getHand(), cardsOnTable);
        } else if (tableIsSequence) {
            selected = helper.findSequence(getHand(), cardsOnTable);
        } else {
            System.out.println("Cái đéo gì đây");
        }

        if (selected == null || selected.isEmpty()) {
            skipTurn();
            return new ArrayList<>();
        } else {
            setSelectedCards(new ArrayList<>(selected));
            List<WestCard> played = playCard();
            setSelectedCards(new ArrayList<>());
            return played;
        }
    }

    private static class Helper{
        private final WestCardComparator comparator = new WestCardComparator();
        private final TienLenMienBacGameLogic gameLogic = new TienLenMienBacGameLogic();
        public WestCard findSingleCard(List<WestCard> hand, List<WestCard> cardsOnTable){
            WestCard topCard = cardsOnTable.getFirst();

            for (WestCard card : hand) {
                // Nếu là lá 2 → chỉ cần mạnh hơn (cùng rank, chất cao hơn)
                if (topCard.getRank() == Rank.TWO) {
                    if (card.getRank() == Rank.TWO && comparator.compare(card, topCard) > 0) {
                        return card;
                    }
                } else {
                    // Bài khác → cùng chất và mạnh hơn
                    if (card.getSuit() == topCard.getSuit() && comparator.compare(card, topCard) > 0) {
                        return card;
                    }
                }
            }
            return null;
        }

        public List<WestCard> findPair(List<WestCard> hand, List<WestCard> cardsOnTable) {
            if(hand.size() < 2){
                return null;
            }
            for (int i = 0; i < hand.size() - 1; i++) {
                List<WestCard> selectedCards = new ArrayList<>(Arrays.asList(hand.get(i), hand.get(i + 1)));
                if (gameLogic.isPair(selectedCards)
                        && gameLogic.isSameColor(selectedCards.getFirst(), cardsOnTable.getFirst())
                        && comparator.compare(selectedCards.getFirst(), cardsOnTable.getFirst()) > 0) {
                    return selectedCards;
                }
            }
            return new ArrayList<>();
        }

        public List<WestCard> findThree(List<WestCard> hand, List<WestCard> cardsOnTable) {
            if(hand.size() < 3){
                return new ArrayList<>();
            }
            for (int i = 0; i < hand.size() - 2; i++) {
                List<WestCard> selectedCards = Arrays.asList(hand.get(i), hand.get(i + 1), hand.get(i + 2));
                if (gameLogic.isThreeOfKind(selectedCards)
                        && gameLogic.isSameColor(selectedCards.getFirst(), cardsOnTable.getFirst())
                        && comparator.compare(selectedCards.getFirst(), cardsOnTable.getFirst()) > 0) {
                    return selectedCards;
                }
            }
            return new ArrayList<>();
        }

        public List<WestCard> findFour(List<WestCard> hand, List<WestCard> cardsOnTable) {
            if(hand.size() < 4){
                return new ArrayList<>();
            }
            for (int i = 0; i < hand.size() - 3; i++) {
                List<WestCard> selectedCards = Arrays.asList(hand.get(i), hand.get(i + 1), hand.get(i + 2), hand.get(i + 3));
                if (gameLogic.isFourOfKind(selectedCards)
                        && gameLogic.isSameColor(selectedCards.getFirst(), cardsOnTable.getFirst())
                        && comparator.compare(selectedCards.getFirst(), cardsOnTable.getFirst()) > 0) {
                    return selectedCards;
                }
            }
            return new ArrayList<>();
        }

        public List<WestCard> findSequence(List<WestCard> hand, List<WestCard> cardsOnTable) {
            int length = cardsOnTable.size();
            if (hand.size() < length) {
                return new ArrayList<>();
            }
            for (int i = 0; i <= hand.size() - length; i++) {
                List<WestCard> selectedCards = hand.subList(i, i + length);
                if (gameLogic.isSequence(selectedCards)
                        && comparator.compare(selectedCards.getFirst(), cardsOnTable.getFirst()) > 0) {
                    return new ArrayList<>(selectedCards);
                }
            }
            return new ArrayList<>();
        }
    }
}

package model.game;

import model.core.card.WestCard;
import model.core.enums.Rank;
import model.core.enums.Suit;
import model.tienlen.TienLenPlayer;

import java.util.Comparator;
import java.util.List;

public class TienLenMienBacGameLogic extends Game<WestCard, TienLenPlayer> {
    private List<WestCard> cardsOnTable;

    @Override
    public void playTurn() {

    }

    @Override
    public TienLenPlayer getFirstPlayer(List<TienLenPlayer> players) {
        for (TienLenPlayer player : players) {
            for (WestCard card : player.getHand()) {
                if (card.getRank() == Rank.THREE && card.getSuit() == Suit.SPADES) {
                    return player;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isValidMove(TienLenPlayer player) {
        List<WestCard> selectedCards = player.getSelectedCards();
        if (selectedCards.isEmpty()) nextTurn();
        if (selectedCards.size() == 1) {

        }
        return !player.getHand().isEmpty();
    }

    @Override
    public boolean endGame() {
        for (TienLenPlayer player : players) {
            if (player.getHand().isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void nextTurn() {
        currentPlayer = getCurrentPlayer();
        currentPlayer = getPlayers().get((getPlayers().indexOf(currentPlayer) + 1) % getPlayers().size());
    }

    public void playCards(List<WestCard> cards) {
        if (isValidCombination(cards)) {
            cardsOnTable = currentPlayer.getSelectedCards();
            currentPlayer.setSelectedCards(currentPlayer.playCard());
            System.out.println(currentPlayer.getName() + " played: " + formatCards(cards));
        } else {
            System.out.println("Invalid card combination!");
        }
    }


    private boolean isValidCombination(List<WestCard> selectedCards) {
        if (isPair(selectedCards)) return true;
        if (isThreeOfKind(selectedCards)) return true;
        if (isFourOfKind(selectedCards)) return true;
        if (isSequence(selectedCards)) return true;
        return !selectedCards.isEmpty();
    }

    private String formatCards(List<WestCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (WestCard card : cards) {
            sb.append(card.toString()).append(" ");
        }
        return sb.toString().trim();
    }

    public List<WestCard> getCardsOnTable() {
        return cardsOnTable;
    }

    public boolean isSameColor(WestCard c1, WestCard c2) {
        boolean allRed = (c1.getSuit() == Suit.HEARTS && c2.getSuit() == Suit.DIAMONDS)
                || (c1.getSuit() == Suit.DIAMONDS && c2.getSuit() == Suit.HEARTS);
        boolean allBlack = (c1.getSuit() == Suit.CLUBS && c2.getSuit() == Suit.SPADES)
                || (c1.getSuit() == Suit.SPADES && c2.getSuit() == Suit.CLUBS);
        return allRed || allBlack;
    }

    public boolean isPair(List<WestCard> selectedCards) {
        return (selectedCards.size() == 2 && selectedCards.get(0).getRank() == selectedCards.get(1).getRank())
                && isSameColor(selectedCards.get(0), selectedCards.get(1));
    }

    public boolean isThreeOfKind(List<WestCard> selectedCards) {
        return selectedCards.size() == 3
                && selectedCards.get(0).getRank() == selectedCards.get(1).getRank()
                && selectedCards.get(1).getRank() == selectedCards.get(2).getRank();
    }

    public boolean isFourOfKind(List<WestCard> selectedCards) {
        return selectedCards.size() == 4
                && selectedCards.get(0).getRank() == selectedCards.get(1).getRank()
                && selectedCards.get(1).getRank() == selectedCards.get(2).getRank()
                && selectedCards.get(2).getRank() == selectedCards.get(3).getRank();
    }

    public boolean isSequence(List<WestCard> selectedCards) {
        if (selectedCards.size() < 3) return false;
        selectedCards.sort(Comparator.comparing(WestCard::getRank).thenComparing(WestCard::getSuit));
        for (int i = 1; i < selectedCards.size(); i++) {
            if ((selectedCards.get(i).getRank().getValue() != selectedCards.get(i - 1).getRank().getValue() + 1)
                    || (selectedCards.get(i).getSuit() != selectedCards.get(i - 1).getSuit()))
                return false;
        }
        return true;
    }
}
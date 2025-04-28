package model.game;

import model.core.card.WestCard;
import model.core.card.Card;
import model.core.deck.WestCardDeck;
import model.core.enums.*;
import model.player.Player;
import model.tienlen.TienLenPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TienLenMienBacGameLogic extends Game {
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
    public boolean isValidMove(Player player) {

        return !player.getHand().isEmpty();
    }

    @Override
    public boolean endGame() {
        for (Player player : players){
            if (player.getHand().isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void nextTurn() {
        currentPlayer = getCurrentPlayer();
        currentPlayer = getPlayers().get((getPlayers().indexOf(currentPlayer) + 1) % getPlayers().size());
    }

    private int compareCards(Card card1, Card card2) {
        int rankComparison = card1.getRank().ordinal() - card2.getRank().ordinal();
        if (rankComparison != 0) {
            return rankComparison;
        }
        return card1.getSuit().ordinal() - card2.getSuit().ordinal();
    }
    
    public void playCards(List<Card> cards) {
        if (isValidCombination(cards)) {
            cardsOnTable = currentPlayer.getSelectedCards();
            currentPlayer.setSelectedCards(currentPlayer.playCard());
            System.out.println(currentPlayer.getName() + " played: " + formatCards(cards));
        } else {
            System.out.println("Invalid card combination!");
        }
    }
    
    private boolean isValidCombination(List<Card> selectedCards) {
        if (isPair(selectedCards)) return true;
        if (isThreeOfKind(selectedCards)) return true;
        if (isFourOfKind(selectedCards)) return true;
        if (isSequence(selectedCards)) return true;
        return !selectedCards.isEmpty();
    }
    
    private String formatCards(List<Card> cards) {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.toString()).append(" ");
        }
        return sb.toString().trim();
    }
    
    public List<Card> getCardsOnTable() {
        return cardsOnTable;
    }

    public boolean isSameColor(Card c1, Card c2) {
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
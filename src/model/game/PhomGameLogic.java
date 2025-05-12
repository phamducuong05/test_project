package model.game;

import model.core.card.WestCard;
import model.phom.PhomPlayer;
import model.phom.PhomBotPlayer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import model.core.deck.Deck;
import model.core.enums.Suit;
import model.core.enums.Rank;
import model.phom.PhomGameState;
import java.util.Collections;

public class PhomGameLogic extends Game<WestCard, PhomPlayer> {
    private WestCard cardsOnTable;
    private PhomPlayer winnerPlayer;
    private List<List<WestCard>> MeldedCards; // Phom đã hạ
    private int discardTurnCounter; // Đếm số lượt ĐÁNH BÀI của người đi đầu
    private PhomPlayer gameEndPlayer; // Người chơi bắt đầu ván game
    private boolean isFinalMeldingPhase; // Cờ báo hiệu đang trong giai đoạn hạ bài cuối


    public PhomGameLogic() {
    }

    public PhomGameLogic(Deck<WestCard, PhomPlayer> deck, List<PhomPlayer> players, int numberOfCards) {
        super(deck, players, numberOfCards);
        cardsOnTable = null;
        this.discardTurnCounter = 0; // Khởi tạo bộ đếm
        this.isFinalMeldingPhase = false; // Khởi tạo cờ
    }

    @Override
    public void startGame() {
        if (deck == null) {
            System.err.println("Lỗi: Bộ bài (deck) chưa được khởi tạo!");
            return;
        }
        deck.shuffle();
        deck.dealCards(players, numberOfCards);
        players.getFirst().receiveCard(deck.drawCard());
        currentPlayer = getFirstPlayer(players);
        this.gameEndPlayer = players.getLast();
        if (players == null || players.isEmpty()) {
            System.err.println("Lỗi: Không có người chơi nào!");
            return;
        }
        if (numberOfCards <= 0) {
            System.err.println("Lỗi: Số lá bài chia không hợp lệ!");
            return;
        }
        System.out.println("Game started! Dealing... " + numberOfCards + " cards to " + players.size() + " players.");
    }

    @Override
    public PhomPlayer getFirstPlayer(List<PhomPlayer> players) {
        return players.getFirst();
    }

    // bổ sung ù ở playturn
    @Override
    public void playTurn() {
        System.out.print("Current: " + getCurrentPlayer().getName() + " | ");
        if(currentPlayer instanceof PhomBotPlayer){
            if(cardsOnTable == null){
                cardsOnTable = ((PhomBotPlayer) currentPlayer).autoPlay();
                PhomPlayer nextPlayer = getPlayers().get((getPlayers().indexOf(currentPlayer) + 1) % getPlayers().size());
                nextPlayer.addDiscardCards(cardsOnTable);
                System.out.print("Play: " + cardsOnTable + " | Cards left: " + currentPlayer.handSize());
                if(!endGame())
                    nextTurn();

            }
            else if(!canFormPhom(currentPlayer, cardsOnTable)){
                System.out.print("Cannot eat, draw 1 card");
                if(!deck.isEmpty())
                  currentPlayer.receiveCard(deck.drawCard());
                else
                  System.out.print("Out of cards");
                cardsOnTable = ((PhomBotPlayer) currentPlayer).autoPlay();
                PhomPlayer nextPlayer = getPlayers().get((getPlayers().indexOf(currentPlayer) + 1) % getPlayers().size());
                nextPlayer.addDiscardCards(cardsOnTable);
                System.out.print("Play: " + cardsOnTable + " | Cards left: " + currentPlayer.handSize());
                if(!endGame())
                    nextTurn();

            }
            else{
                cardsOnTable = ((PhomBotPlayer) currentPlayer).autoPlay();
                System.out.print("Play: " + cardsOnTable + " | Cards left: " + currentPlayer.handSize());
                PhomPlayer nextPlayer = getPlayers().get((getPlayers().indexOf(currentPlayer) + 1) % getPlayers().size());
                nextPlayer.addDiscardCards(cardsOnTable);
                // Chuyển bài đã đánh ở dưới bàn và sang lượt tiếp theo
                if(!endGame())
                    nextTurn();

            }

        }
        else{
            if(canFormPhom(currentPlayer, cardsOnTable)) {
                // UI
            }
            else {
                // UI
            }
            // UI prompt play card
        }
        if(currentPlayer == this.gameEndPlayer) {
            this.discardTurnCounter++;
        }
        return;
    }

    @Override
    public boolean isValidMove(PhomPlayer player) {
        return player.getSelectedCards().size() == 1;
    }

    @Override
    public boolean endGame() {
        // end game khi hết bài bốc
        int minScore = Integer.MAX_VALUE;
        if(deck.isEmpty()){
            return true;
        }
        // end game khi có người ù
        for (PhomPlayer player : players) {
            if(player.calculateScore() == 0) {
                winnerPlayer = player;
                return true;
            }
        }

        if (discardTurnCounter >= 4) { // Đủ 4 lượt đánh của người đi đầu
            return true;
        }

        return false;
    }

    @Override
    public void nextTurn() {
        currentPlayer = getCurrentPlayer();
        currentPlayer = getPlayers().get((getPlayers().indexOf(currentPlayer) + 1) % getPlayers().size());
    }

    public boolean canFormPhom(PhomPlayer player, WestCard card){
        int numOfPhom = player.findCombinations().size();
        player.receiveCard(card);
        int tmp = numOfPhom;
        numOfPhom = player.findCombinations().size();
        player.getHand().remove(card);
        if(numOfPhom == tmp)
          return false;
        else
          return true;
    }

    public boolean isValidCombination(List<WestCard> cards) {
        if(cards.getFirst().getSuit() != cards.getLast().getSuit()) {
            int tmp = cards.getFirst().getRank().getValue();
            for(WestCard card : cards) {
                if(card.getRank().getValue() != tmp) {
                    return false;
                }
            }
            return true;
        }

        else {
            for(int i=0; i < cards.size() - 1; i++) {
                if(cards.get(i).getRank() .getValue()+1 != cards.get(i+1).getRank().getValue()) {
                    return false;
                }
            }
            return true;
        }
    }

    public void determineWinnerByScore() {
        int minScore = Integer.MAX_VALUE;
        PhomPlayer potentialWinner = null;
        List<PhomPlayer> winners = new ArrayList<>();

        System.out.println("Điểm số cuối cùng:");
        for (PhomPlayer player : players) {
            int score = player.calculateScore();
            System.out.println("- " + player.getName() + ": " + score + " điểm");
            if (score < minScore) {
                minScore = score;
                winners.clear();
                winners.add(player);
                potentialWinner = player;
            } else if (score == minScore) {
                winners.add(player);
            }

        }

        if (winners.size() == 1) {
            this.winnerPlayer = potentialWinner;
        } else {
            for (PhomPlayer winner : winners) {
                System.out.print(winner.getName() + " ");
            }
            System.out.println();
            this.winnerPlayer = null;
        }
    }

    private void initiateFinalMeldingPhase() {
        for(PhomPlayer player : players) {
            for(List<WestCard> cards : player.findCombinations()) {
                MeldedCards.add(cards);
            }
        }
    }

    public PhomGameState getCurrentGameState() {
        List<PhomPlayer> currentPlayers = Collections.unmodifiableList(new ArrayList<>(this.players));
        PhomPlayer activePlayer = this.currentPlayer;
        List<List<WestCard>> currentMeldedCards = Collections.unmodifiableList(new ArrayList<>(MeldedCards));
        return new PhomGameState(
                currentPlayers,
                activePlayer,
                currentMeldedCards,
                deck.size()
        );
    }


}

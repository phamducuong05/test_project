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
import java.util.Map;

public class PhomGameLogic extends Game<WestCard, PhomPlayer> {
    private WestCard cardsOnTable;
    private PhomPlayer winnerPlayer;
    private List<List<WestCard>> MeldedCards; // Phom đã hạ
    private boolean isFinalMeldingPhase; // Cờ báo hiệu đang trong giai đoạn hạ bài cuối


    public PhomGameLogic() {
    }

    public PhomGameLogic(Deck<WestCard, PhomPlayer> deck, List<PhomPlayer> players, int numberOfCards) {
        super(deck, players, numberOfCards);
        cardsOnTable = null;
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


    public void botDiscardCard() {
        PhomBotPlayer botPlayer = (PhomBotPlayer) currentPlayer;
        botPlayer.getHand().remove(botPlayer.decideDiscard());
        botPlayer.addDiscardCards(botPlayer.decideDiscard());
        cardsOnTable = botPlayer.decideDiscard();
    }


    public void botEatCard() {
        currentPlayer.getEatenCards().add(cardsOnTable);
        PhomPlayer previousPlayer = getPlayers().get((getPlayers().indexOf(currentPlayer) - 1) % getPlayers().size());
        previousPlayer.getDiscardCards().remove(cardsOnTable);
    }

    public void botSendCard() {
        PhomBotPlayer botPlayer = (PhomBotPlayer) currentPlayer;
        Map<WestCard, List<WestCard>> cardsToSend = botPlayer.decideSends(this.getCurrentGameState());

        if (!cardsToSend.isEmpty()) {
            // Lặp qua các lá bài mà bot muốn gửi
            for (Map.Entry<WestCard, List<WestCard>> entry : cardsToSend.entrySet()) {
                WestCard cardToSend = entry.getKey();
                List<WestCard> meldToSendTo = entry.getValue(); // Phỏm mà bot muốn gửi vào

                // Xác định người chơi sở hữu phỏm này
                PhomPlayer recipient = null;
                for (PhomPlayer player : this.getPlayers()) {
                    if (player != currentPlayer && player.getAllPhoms().contains(meldToSendTo)) {
                        recipient = player;
                        break;
                    }
                }

                if (recipient != null) {
                    sendCardToMeld(currentPlayer, recipient, cardToSend, meldToSendTo);
                    break; // Bot có thể quyết định chỉ gửi một lá mỗi lượt gửi bài
                } else {
                    System.out.println("Fail to send");
                }
            }
        } else {
            return;
        }
    }

    public void sendCardToMeld(PhomPlayer sender, PhomPlayer recipient, WestCard cardToSend, List<WestCard> meldToSendTo) {
        sender.getHand().remove(cardToSend);
        meldToSendTo.add(cardToSend);
    }



    public void humanDiscardCard(WestCard card) {
        currentPlayer.addDiscardCards(card);
        currentPlayer.getHand().remove(card);
        cardsOnTable = card;
        currentPlayer.setNumOfTurn(currentPlayer.getNumOfTurn() + 1);
    }

    public void playerDrawCard() {
        if(!deck.isEmpty()) {
            WestCard card = deck.drawCard();
            currentPlayer.receiveCard(card);
        }
    }


    public void humanEatCard() {
        currentPlayer.getEatenCards().add(cardsOnTable);
        PhomPlayer previousPlayer = getPlayers().get((getPlayers().indexOf(currentPlayer) - 1) % getPlayers().size());
        previousPlayer.getDiscardCards().remove(cardsOnTable);
    }

    @Override
    public boolean isValidMove(List<WestCard> cards) {
        return cards.size() == 1;
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
        return false;
    }

    public void playerMeldCard() {
        for(List<WestCard> meld : currentPlayer.findCombinations()) {
            currentPlayer.getHand().removeAll(meld);
            currentPlayer.getAllPhoms().add(meld);
            this.MeldedCards.add(meld);
        }
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



    public PhomGameState getCurrentGameState() {
        List<PhomPlayer> currentPlayers = Collections.unmodifiableList(new ArrayList<>(this.players));
        PhomPlayer activePlayer = this.currentPlayer;
        List<List<WestCard>> currentMeldedCards = Collections.unmodifiableList(new ArrayList<>(MeldedCards));
        return new PhomGameState(
                currentPlayers,
                activePlayer,
                currentMeldedCards,
                endGame(),
                winnerPlayer,
                cardsOnTable
        );
    }

    public WestCard getCardsOnTable() {
        return cardsOnTable;
    }


}

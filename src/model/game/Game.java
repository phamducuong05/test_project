package model.game;

import model.core.Deck;
import model.player.Player;

import java.util.List;

public abstract class Game {
    protected Deck deck;
    protected List<Player> players;
    protected Player currentPlayer;
    protected boolean gameState; // false = started, true = ended
    protected int numberOfCards;

    protected Game() {
    }

    protected Game(Deck deck, List<Player> players, int numberOfCards) {
        this.deck = deck;
        this.players = players;
        this.numberOfCards = numberOfCards;
        this.gameState = false;
    }


    public void startGame() {
        if (deck == null) {
            System.err.println("Lỗi: Bộ bài (deck) chưa được khởi tạo!");
            return;
        }
        deck.shuffle();
        deck.dealCards(players, numberOfCards);
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


    public abstract void playTurn();

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameState;
    }

    public abstract Player getFirstPlayer(List<Player> players);

    public abstract boolean isValidMove(Player player);

    public abstract void endGame();
    public abstract void nextTurn();

    public int getNumberOfCards() {
        return this.numberOfCards;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public List<Player> getPlayers() {
        return this.players;
    }
}

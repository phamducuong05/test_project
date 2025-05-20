package model.phom;

import model.core.card.WestCard;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class PhomGameState {
    private final List<PhomPlayer> players;
    private final PhomPlayer currentPlayer;
    private final List<List<WestCard>> allPlayerMelds;
    private final boolean isGameOver;
    private final PhomPlayer winner;

    public PhomGameState(List<PhomPlayer> players, PhomPlayer currentPlayer, List<List<WestCard>> allPlayerMelds, boolean isGameOver, PhomPlayer winner) {
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.allPlayerMelds = allPlayerMelds;
        this.isGameOver = isGameOver;
        this.winner = winner;
    }
    

    public List<PhomPlayer> getPlayers() {
        return players;
    }
    

    public PhomPlayer getCurrentPlayer() {
        return currentPlayer;
    }


    public List<List<WestCard>> getAllPlayerMelds() {
        return allPlayerMelds;
    }
    

    public boolean isGameOver() {
        return isGameOver;
    }
    

    public PhomPlayer getWinner() {
        return winner;
    }
}

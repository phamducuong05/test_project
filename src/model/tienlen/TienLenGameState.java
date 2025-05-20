package model.tienlen;

import model.core.card.WestCard;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Class representing the state of a TienLen game
 */
public class TienLenGameState {
    private final List<TienLenPlayer> players;
    private final TienLenPlayer currentPlayer;
    private final List<WestCard> lastPlayedCards;
    private final TienLenPlayer lastPlayer;
    private final Map<TienLenPlayer, Integer> playerScores;
    private final boolean isGameOver;
    private final TienLenPlayer winner;
    
    /**
     * Constructor for TienLenGameState
     * 
     * @param players The list of players
     * @param currentPlayer The current player
     * @param lastPlayedCards The last played cards
     * @param lastPlayer The player who played the last cards
     * @param playerScores The scores of each player
     * @param isGameOver Whether the game is over
     * @param winner The winner of the game (null if game is not over)
     */
    public TienLenGameState(List<TienLenPlayer> players, TienLenPlayer currentPlayer, 
                          List<WestCard> lastPlayedCards, TienLenPlayer lastPlayer, 
                          Map<TienLenPlayer, Integer> playerScores,
                          boolean isGameOver, TienLenPlayer winner) {
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.lastPlayedCards = lastPlayedCards;
        this.lastPlayer = lastPlayer;
        this.playerScores = playerScores != null ? playerScores : new HashMap<>();
        this.isGameOver = isGameOver;
        this.winner = winner;
    }
    
    /**
     * Get the list of players
     * 
     * @return The list of players
     */
    public List<TienLenPlayer> getPlayers() {
        return players;
    }
    
    /**
     * Get the current player
     * 
     * @return The current player
     */
    public TienLenPlayer getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Get the last played cards
     * 
     * @return The last played cards
     */
    public List<WestCard> getLastPlayedCards() {
        return lastPlayedCards;
    }
    
    /**
     * Get the player who played the last cards
     * 
     * @return The player who played the last cards
     */
    public TienLenPlayer getLastPlayer() {
        return lastPlayer;
    }
    
    /**
     * Get the scores of each player
     * 
     * @return A map from players to their scores
     */
    public Map<TienLenPlayer, Integer> getPlayerScores() {
        return playerScores;
    }
    
    /**
     * Get the score of a specific player
     * 
     * @param player The player
     * @return The player's score, or 0 if not found
     */
    public int getPlayerScore(TienLenPlayer player) {
        return playerScores.getOrDefault(player, 0);
    }
    
    /**
     * Check if the game is over
     * 
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return isGameOver;
    }
    
    /**
     * Get the winner of the game
     * 
     * @return The winner, or null if game is not over
     */
    public TienLenPlayer getWinner() {
        return winner;
    }
}

package model.phom;

import model.core.card.WestCard;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Class representing the state of a Phom game
 */
public class PhomGameState {
    private final List<PhomPlayer> players;
    private final PhomPlayer currentPlayer;
    private final List<WestCard> cardsOnTable;
    private final List<List<WestCard>> allPlayerMelds;
    private final boolean isGameOver;
    private final PhomPlayer winner;
    
    /**
     * Constructor for PhomGameState
     * 
     * @param players The list of players
     * @param currentPlayer The current player
     * @param cardsOnTable The cards on the table
     * @param playerMelds The melds (phoms) formed by each player
     * @param isGameOver Whether the game is over
     * @param winner The winner of the game (null if game is not over)
     */
    public PhomGameState(List<PhomPlayer> players, PhomPlayer currentPlayer,
                         List<WestCard> cardsOnTable,
                         Map<PhomPlayer, List<List<WestCard>>> playerMelds, List<List<WestCard>> allPlayerMelds,
                         boolean isGameOver, PhomPlayer winner) {
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.cardsOnTable = cardsOnTable;
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
    

    public List<WestCard> getCardsOnTable() {
        return cardsOnTable;
    }

    public WestCard getTopCardOnTable() {
        return cardsOnTable != null && !cardsOnTable.isEmpty() ? 
               cardsOnTable.get(cardsOnTable.size() - 1) : null;
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

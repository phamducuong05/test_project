package controller;

import model.core.card.Card;
import model.game.Game;
import model.player.Player;

import java.util.List;

/**
 * Abstract LogicController that serves as an intermediary between ViewController and Model (Game)
 * This controller handles game logic without knowledge of JavaFX UI components
 */
public abstract class LogicController<T extends Card, P extends Player<T>, G extends Game<T, P>> {
    protected G gameLogic;
    protected boolean isGameRunning;
    
    public LogicController(G gameLogic) {
        this.gameLogic = gameLogic;
        this.isGameRunning = false;
    }

    
    /**
     * Process a player's move
     * 
     * @param player The player making the move
     * @param move The move object (can be specialized in subclasses)
     */
    protected abstract void processPlayerMove(P player, Object move);
    
    /**
     * Move to the next player's turn
     */
    protected abstract void nextTurn();
    
    /**
     * Check if the current player is a bot, and if so, play its turn automatically
     */
    protected abstract void checkAndPlayBotTurnIfNeeded();
    
    /**
     * Update the game state and notify any views
     */
    protected abstract void updateGameState();
    
    /**
     * Called when the game starts
     */

    /**
     * Get the current player
     * 
     * @return The current player
     */
    public P getCurrentPlayer() {
        return gameLogic.getCurrentPlayer();
    }
    
    /**
     * Check if the game is running
     * 
     * @return true if the game is running, false otherwise
     */
    public boolean isGameRunning() {
        return isGameRunning;
    }
    
    /**
     * Check if the game is over
     * 
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameLogic.isGameOver();
    }
    
    /**
     * Get the game logic
     * 
     * @return The game logic
     */
    public G getGameLogic() {
        return gameLogic;
    }
} 
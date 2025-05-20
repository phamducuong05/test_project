package controller;

import model.core.card.WestCard;
import model.game.TienLenMienBacGameLogic;
import model.tienlen.TienLenGameState;
import model.tienlen.TienLenPlayer;
import model.tienlen.TienLenBotPlayer;

import java.util.List;

/**
 * LogicController implementation for TienLen game
 * Handles game logic and coordinates between model and view
 */
public class TienLenLogicController extends LogicController<WestCard, TienLenPlayer, TienLenMienBacGameLogic> {
    
    // Reference to the view controller (without JavaFX dependencies)
    private TienLenViewController viewController;
    
    public TienLenLogicController(TienLenMienBacGameLogic gameLogic) {
        super(gameLogic);
    }
    
    /**
     * Set the view controller
     * 
     * @param viewController The view controller to use
     */
    public void setViewController(TienLenViewController viewController) {
        this.viewController = viewController;
    }
    
    @Override
    protected void processPlayerMove(TienLenPlayer player, Object move) {
        if (!isGameRunning()) {
            return;
        }
        
        // In TienLen, the main move is playing cards
        if (move instanceof List) {
            @SuppressWarnings("unchecked")
            List<WestCard> cardsToPlay = (List<WestCard>) move;
            
            // Validate the move
            if (isValidCardPlay(player, cardsToPlay)) {
                // Update player's hand
                player.getHand().removeAll(cardsToPlay);
                
                // Update game state
                gameLogic.playCards(cardsToPlay);
                
                // Update view
                updateGameState();
                
                // Check if player has won (no cards left)
                if (player.getHand().isEmpty()) {
                    endGame();
                } else {
                    // If valid move, player can play again if they want
                    if (viewController != null) {
                        viewController.promptPlayerForAction(player, gameLogic.getCurrentGameState());
                    }
                }
            } else {
                // Invalid move, prompt player to try again
                if (viewController != null) {
                    viewController.showInvalidMoveMessage();
                    viewController.promptPlayerForAction(player, gameLogic.getCurrentGameState());
                }
            }
        } else if (move instanceof String && "PASS".equals(move)) {
            // Player passes turn
            nextTurn();
        }
    }
    
    /**
     * Validate if a card play is valid according to TienLen rules
     * 
     * @param player The player making the move
     * @param cards The cards being played
     * @return true if the move is valid, false otherwise
     */
    private boolean isValidCardPlay(TienLenPlayer player, List<WestCard> cards) {
        // Ensure player owns all cards
        if (!player.getHand().containsAll(cards)) {
            return false;
        }
        
        TienLenGameState gameState = gameLogic.getCurrentGameState();
        
        // First play in a round is always valid
        List<WestCard> lastPlayed = gameState.getLastPlayedCards();
        if (lastPlayed == null || lastPlayed.isEmpty()) {
            return true;
        }
        
        // In subsequent plays, check if current play beats the previous play
        // This is a simplified implementation of TienLen rules
        // Would need to be expanded based on detailed game rules
        
        // Check if same number of cards
        if (cards.size() != lastPlayed.size()) {
            return false;
        }
        
        // In real implementation, would check card combinations and values here
        // For now, just a basic check that the highest card beats the previous highest card
        WestCard highestNew = findHighestCard(cards);
        WestCard highestLast = findHighestCard(lastPlayed);
        
        return highestNew.getRank().ordinal() > highestLast.getRank().ordinal();
    }
    
    /**
     * Find the highest card in a list of cards
     * 
     * @param cards The list of cards
     * @return The highest card
     */
    private WestCard findHighestCard(List<WestCard> cards) {
        WestCard highest = cards.get(0);
        for (WestCard card : cards) {
            if (card.getRank().ordinal() > highest.getRank().ordinal()) {
                highest = card;
            }
        }
        return highest;
    }
    
    @Override
    protected void nextTurn() {
        gameLogic.nextTurn();
        updateGameState();
        checkAndPlayBotTurnIfNeeded();
    }
    
    @Override
    protected void checkAndPlayBotTurnIfNeeded() {
        TienLenPlayer currentPlayer = gameLogic.getCurrentPlayer();
        
        // If current player is a bot, play its turn automatically
        if (currentPlayer instanceof TienLenBotPlayer) {
            TienLenBotPlayer bot = (TienLenBotPlayer) currentPlayer;
            TienLenGameState gameState = gameLogic.getCurrentGameState();
            
            // Get bot decision
            List<WestCard> botMove = bot.decideCardsToPlay(gameState);
            
            if (botMove != null && !botMove.isEmpty()) {
                // Bot decides to play cards
                processPlayerMove(bot, botMove);
            } else {
                // Bot decides to pass
                processPlayerMove(bot, "PASS");
            }
        } else {
            // Human player's turn - prompt for action via the view controller
            if (viewController != null) {
                viewController.promptPlayerForAction(currentPlayer, gameLogic.getCurrentGameState());
            }
        }
    }
    
    @Override
    protected void updateGameState() {
        TienLenGameState gameState = gameLogic.getCurrentGameState();
        
        // Notify view of game state change
        if (viewController != null) {
            viewController.updateView(gameState);
        }
        
        // Check if game is over
        if (gameLogic.isGameOver()) {
            endGame();
        }
    }
    
    @Override
    protected void onGameStart() {
        if (viewController != null) {
            viewController.onGameStarted(gameLogic.getCurrentGameState());
        }
        checkAndPlayBotTurnIfNeeded();
    }
    
    @Override
    protected void onGamePause() {
        if (viewController != null) {
            viewController.onGamePaused();
        }
    }
    
    @Override
    protected void onGameResume() {
        if (viewController != null) {
            viewController.onGameResumed();
        }
    }
    
    @Override
    protected void onGameEnd() {
        // Determine winner and final scores
        TienLenPlayer winner = findWinner();
        
        if (viewController != null) {
            viewController.onGameEnded(gameLogic.getCurrentGameState(), winner);
        }
    }
    
    /**
     * Find the winner of the game based on current game state
     * 
     * @return The winning player
     */
    private TienLenPlayer findWinner() {
        TienLenGameState gameState = gameLogic.getCurrentGameState();
        
        // If game state already has a winner, return it
        if (gameState.getWinner() != null) {
            return gameState.getWinner();
        }
        
        // Otherwise, determine winner based on game rules
        // In TienLen, the first player to get rid of all cards wins
        for (TienLenPlayer player : gameState.getPlayers()) {
            if (player.getHand().isEmpty()) {
                return player;
            }
        }
        
        // No winner yet
        return null;
    }
    
    // API methods for ViewController to call
    
    /**
     * Handle player request to play cards
     * 
     * @param player The player playing the cards
     * @param cards The cards to play
     */
    public void playerRequestsPlayCards(TienLenPlayer player, List<WestCard> cards) {
        processPlayerMove(player, cards);
    }
    
    /**
     * Handle player request to pass turn
     * 
     * @param player The player passing
     */
    public void playerRequestsPass(TienLenPlayer player) {
        processPlayerMove(player, "PASS");
    }
} 
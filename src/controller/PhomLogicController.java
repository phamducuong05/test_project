package controller;

import model.core.card.WestCard;
import model.game.PhomGameLogic;
import model.phom.PhomGameState;
import model.phom.PhomPlayer;
import model.phom.PhomBotPlayer;
import model.phom.PhomPlayerAction;
import view.PhomGameViewController;

import java.util.List;

/**
 * LogicController implementation for Phom game
 * Handles game logic and coordinates between model and view
 */
public class PhomLogicController extends LogicController<WestCard, PhomPlayer, PhomGameLogic> {
    
    // Reference to the view controller (without JavaFX dependencies)
    private PhomGameViewController viewController;
    
    public PhomLogicController(PhomGameLogic gameLogic) {
        super(gameLogic);
    }
    
    /**
     * Set the view controller
     * 
     * @param viewController The view controller to use
     */
    public void setViewController(PhomGameViewController viewController) {
        this.viewController = viewController;
    }
    
    @Override
    protected void processPlayerMove(PhomPlayer player, Object move) {
        if (!isGameRunning()) {
            return;
        }
        
        // Handle different types of moves based on the action type
        if (move instanceof PhomPlayerAction.DrawCardAction) {
            // Player draws a card from the deck
            WestCard drawnCard = gameLogic.getDeck().drawCard();
            player.receiveCard(drawnCard);
            
            // After drawing, player must discard a card
            if (viewController != null) {
                viewController.promptPlayerToDiscard(player, gameLogic.getCurrentGameState());
            }
            
        } else if (move instanceof PhomPlayerAction.EatCardAction) {
            // Player eats a card from the table
            PhomPlayerAction.EatCardAction eatAction = (PhomPlayerAction.EatCardAction) move;
            WestCard cardToEat = eatAction.getCard();
            
            // Implement eat card logic here
            player.receiveCard(cardToEat);
            
            // Use the game's state to remove the card from the table
            PhomGameState gameState = gameLogic.getCurrentGameState();
            List<WestCard> cardsOnTable = gameState.getCardsOnTable();
            cardsOnTable.remove(cardToEat);
            
            // After eating, player must discard a card
            if (viewController != null) {
                viewController.promptPlayerToDiscard(player, gameLogic.getCurrentGameState());
            }
            
        } else if (move instanceof PhomPlayerAction.DiscardCardAction) {
            // Player discards a card
            PhomPlayerAction.DiscardCardAction discardAction = (PhomPlayerAction.DiscardCardAction) move;
            WestCard cardToDiscard = discardAction.getCard();
            // Implement discard logic here
            gameLogic.humanDiscardCard(cardToDiscard);
            // Add to cards on table in the game state
            PhomGameState gameState = gameLogic.getCurrentGameState();
            // update hand and updateDiscardPile
            // Move to next turn after discard
            nextTurn();
            
        } else if (move instanceof PhomPlayerAction.MeldAction) {
            // Player creates a meld (phom)
            PhomPlayerAction.MeldAction meldAction = (PhomPlayerAction.MeldAction) move;
            List<WestCard> cardsToMeld = meldAction.getCards();
            
            // Implement meld logic here
            // Remove cards from player's hand
            player.getHand().removeAll(cardsToMeld);
            
            // Add meld to player's melds in the game state
            PhomGameState gameState = gameLogic.getCurrentGameState();
            gameState.getPlayerMelds(player).add(cardsToMeld);
            
            // Update game state after melding
            updateGameState();
        }
    }

    public void handleDeal() {
        gameLogic.startGame();
        viewController.updatePlayerHands(gameLogic.getCurrentGameState());
        viewController.promptPlayerToDiscard(gameLogic.getCurrentGameState().getCurrentPlayer(), gameLogic.getCurrentGameState());
        // Trong hàm này ta sẽ bảo view hiển thị nút đánh bài
    }
    
    @Override
    protected void nextTurn() {
        // Check xem người đánh lượt vừa rồi có đủ 4 quân ở trên chồng bài đánh chưa
        // Nếu đủ rồi thì cho hạ phỏm

        gameLogic.nextTurn();

        // viewController update hands and view controller update discard pile
        checkAndPlayBotTurnIfNeeded();
    }
    
    @Override
    protected void checkAndPlayBotTurnIfNeeded() {
        PhomPlayer currentPlayer = gameLogic.getCurrentPlayer();
        
        // If current player is a bot, play its turn automatically ở đây ta
        // sẽ chia ra các hàm ở trong phần logic để làm và sau đó
        // sẽ update view ở trong controller này luôn ứng với từng hành động
        // Đầu tiên check xem có ăn được không, nếu ăn được thì update displayHand
        // và update display discardPile
        // Sau khi check có ăn được không thì sẽ đến hành động bốc bài và đánh bài
        // Sau hành động đánh bài lại gọi đến update Hand và update discardPile
        // rồi lại gọi DiscardPile
        // Nếu sau khi chạy nextTurn và trong hàm checkAndPlayBot thấy người chơi hiện tại
        // không phải là Bot thì phải update PlayerHand và check xem người chơi có ăn được
        // hay không, nếu ăn được thì phải prompt eat
        // nếu không ăn được thì phải prompt draw và prompt playCard

        if (currentPlayer instanceof PhomBotPlayer) {
            PhomBotPlayer bot = (PhomBotPlayer) currentPlayer;
            PhomGameState gameState = gameLogic.getCurrentGameState();


            
            // First check if bot can/wants to eat the top card on table
            WestCard topCard = gameState.getTopCardOnTable();
            if (topCard != null && bot.shouldEatCard(topCard, gameState)) {
                // Bot decides to eat card
                processPlayerMove(bot, new PhomPlayerAction.EatCardAction(topCard));
            } else {
                // Bot draws a card
                processPlayerMove(bot, new PhomPlayerAction.DrawCardAction());
            }
            
            // Bot decides which melds to form (if any)
            List<List<WestCard>> decidedMelds = bot.decidePossibleMelds(gameState);
            for (List<WestCard> meld : decidedMelds) {
                processPlayerMove(bot, new PhomPlayerAction.MeldAction(meld));
            }
            
            // Bot decides which card to discard
            WestCard cardToDiscard = bot.decideCardToDiscard(gameState);
            processPlayerMove(bot, new PhomPlayerAction.DiscardCardAction(cardToDiscard));
        } else {
            // Human player's turn - prompt for action via the view controller
            if (viewController != null) {
                viewController.promptPlayerForAction(currentPlayer, gameLogic.getCurrentGameState());
            }
        }
    }
    
    @Override
    protected void updateGameState() {
        PhomGameState gameState = gameLogic.getCurrentGameState();
        
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
        PhomPlayer winner = findWinner();
        
        if (viewController != null) {
            viewController.onGameEnded(gameLogic.getCurrentGameState(), winner);
        }
    }
    
    /**
     * Find the winner of the game based on current game state
     * 
     * @return The winning player
     */
    private PhomPlayer findWinner() {
        PhomGameState gameState = gameLogic.getCurrentGameState();
        
        // If game state already has a winner, return it
        if (gameState.getWinner() != null) {
            return gameState.getWinner();
        }
        
        // Otherwise, determine winner based on game rules
        // This is a simplified implementation
        PhomPlayer winner = null;
        int bestScore = Integer.MAX_VALUE;
        
        for (PhomPlayer player : gameState.getPlayers()) {
            int unmeldedCards = player.getHand().size();
            
            // The player with the fewest unmelded cards wins
            if (unmeldedCards < bestScore) {
                bestScore = unmeldedCards;
                winner = player;
            }
        }
        
        return winner;
    }
    
    // API methods for ViewController to call
    

    public void playerRequestsDraw(PhomPlayer player) {
        processPlayerMove(player, new PhomPlayerAction.DrawCardAction());
    }
    

    public void playerRequestsEat(PhomPlayer player, WestCard card) {
        processPlayerMove(player, new PhomPlayerAction.EatCardAction(card));
    }
    

    public void playerRequestsDiscard(PhomPlayer player, List<WestCard> cards) {
        if(gameLogic.isValidMove(cards)) {
            WestCard card = cards.getFirst();
            processPlayerMove(player, new PhomPlayerAction.DiscardCardAction(card));
        }
        else {
            System.out.println("Error"); // Ở đây UI/UX sẽ thông báo lỗi ra màn hình
        }

    }
    

    public void playerRequestsMeld(PhomPlayer player, List<WestCard> cards) {
        processPlayerMove(player, new PhomPlayerAction.MeldAction(cards));
    }
} 
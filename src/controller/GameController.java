package controller;

import model.core.card.Card;
import model.game.Game;
import model.player.Player;


public abstract class GameController<T extends Card, P extends Player<T>, G extends Game<T, P>> {
    
    protected G gameLogic;
    protected boolean isGameRunning;
    
    
    public GameController(G gameLogic) {
        this.gameLogic = gameLogic;
        this.isGameRunning = false;
    }
   
    public void startGame() {
        gameLogic.startGame();
        isGameRunning = true;
        onGameStart();
    }
    
    
    public void pauseGame() {
        isGameRunning = false;
        onGamePause();
    }
    
    
    public void resumeGame() {
        isGameRunning = true;
        onGameResume();
    }
    
    
    public void endGame() {
        isGameRunning = false;
        gameLogic.endGame();
        onGameEnd();
    }
    
    
    public abstract boolean processPlayerMove(P player, Object move);
    
   
    public abstract void nextTurn();
    
    
    protected abstract void onGameStart();
    
   
    protected abstract void onGamePause();
    
    
    protected abstract void onGameResume();
    
   
    protected abstract void onGameEnd();
    
    
    public abstract void updateGameState();
    
    
    public P getCurrentPlayer() {
        return gameLogic.getCurrentPlayer();
    }
    
    
    public boolean isGameRunning() {
        return isGameRunning;
    }
    
   
    public boolean isGameOver() {
        return gameLogic.isGameOver();
    }
} 
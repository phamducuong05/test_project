package model.tienlen;

import model.core.card.WestCard;
import model.player.Player;

public class TienLenPlayer extends Player<WestCard> {
    private int playerNum;
    private int playerRank;
    private playerState state;

    public TienLenPlayer(String name) {
        super(name);
    }

    public boolean isPlaying() {
        return getState() == playerState.Playing;
    }

    public playerState getState() {
        return state;
    }

    public void setState(playerState state) {
        this.state = state;
    }

    public void skipTurn() {
        this.state = playerState.Waiting;
    }

    public enum playerState {
        Playing,
        Waiting
    }
}

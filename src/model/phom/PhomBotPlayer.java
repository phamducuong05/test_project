package model.phom;

import model.core.card.WestCard;
import java.util.List;
import java.util.Map;

public class PhomBotPlayer extends PhomPlayer{
    public PhomBotPlayer(String name) {
        super(name);
    }

    public WestCard autoPlay() {
        return null;
    }

    @Override
    public boolean decideToEat(WestCard discardedCard, PhomGameState gameState) {
        // Logic AI của Bot để quyết định ăn hay bốc
        return true; // or false
    }

    @Override
    public WestCard decideDiscard(PhomGameState gameState) {
        // Logic AI của Bot để chọn lá bài đánh đi từ this.getHand()
        return null;
    }

    @Override
    public List<List<WestCard>> decideMelds(PhomGameState gameState) {
        return null;
    }

    @Override
    public Map<WestCard, List<WestCard>> decideSends(PhomGameState gameState) {
        return null;
    }

}

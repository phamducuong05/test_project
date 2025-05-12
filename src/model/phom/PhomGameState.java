package model.phom;
import model.core.card.WestCard;
import java.util.List;
import java.util.Map;

public record PhomGameState(
        List<PhomPlayer> players,            // Danh sách người chơi (thông tin cơ bản)
        PhomPlayer currentPlayer,          // Người chơi hiện tại
        List<List<WestCard>> meldedCards,
        int currentDeckSize
) {
}

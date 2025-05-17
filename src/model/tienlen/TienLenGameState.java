package model.tienlen;

import model.core.card.WestCard;
import java.util.List;

public record TienLenGameState(
        List<TienLenPlayer> players,        // Danh sách người chơi (thông tin cơ bản)
        TienLenPlayer currentPlayer,       // Người chơi hiện tại
        List<WestCard> cardsOnTable){
}

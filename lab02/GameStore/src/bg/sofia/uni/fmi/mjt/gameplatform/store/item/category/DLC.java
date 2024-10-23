package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DLC extends AbstractStoreItem implements StoreItem{

    public Game dlcGame;

    public void setGame(Game game) {
        dlcGame = game; //is this enough???
    }

    public Game getGame() {
        return dlcGame;
    }

    public DLC(String title, BigDecimal price, LocalDateTime releaseDate, Game game) {
        setTitle(title);
        setPrice(price);
        setReleaseDate(releaseDate);
        setGame(game);
    }

}

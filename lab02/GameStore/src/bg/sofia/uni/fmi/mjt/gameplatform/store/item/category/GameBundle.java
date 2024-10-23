package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

public class GameBundle extends AbstractStoreItem implements StoreItem {

    private Game[] games;

    public void setGames(Game[] games) {
        this.games = Arrays.copyOf(games, games.length);
    }

    public Game[] getGames() {
        return games;
    }

    public GameBundle(String title, BigDecimal price, LocalDateTime releaseDate, Game[] games) {
        setTitle(title);
        setPrice(price);
        setReleaseDate(releaseDate);
    }
}

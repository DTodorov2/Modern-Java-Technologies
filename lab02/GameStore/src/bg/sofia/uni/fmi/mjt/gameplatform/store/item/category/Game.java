package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Game extends AbstractStoreItem implements StoreItem  {

    private String gameGenre;

    public Game(String title, BigDecimal price, LocalDateTime releaseDate, String genre) {
        setGenre(genre);
        setTitle(title);
        setPrice(price);
        setReleaseDate(releaseDate);
    }

    public void setGenre(String genre) {
        if (genre.isEmpty()) {
            genre = "Invalid";
        }
        gameGenre = genre;
    }

    public String getGameGenre() {
        return gameGenre;
    }
}

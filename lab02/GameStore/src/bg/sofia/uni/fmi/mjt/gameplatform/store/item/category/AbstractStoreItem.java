package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public abstract class AbstractStoreItem implements StoreItem {

    private String gameTitle;
    private BigDecimal gamePrice;
    private LocalDateTime gameReleaseDate;
    private double gameRating = 0;
    private int counter = 0;

    @Override
    public String getTitle() {
        return gameTitle;
    }

    @Override
    public BigDecimal getPrice() {
        return gamePrice.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public double getRating() {
        return gameRating;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return gameReleaseDate;
    }

    @Override
    public void setTitle(String title) {
        if (title.isEmpty()) {
            title = "Invalid";
        }
        gameTitle = title;
    }

    @Override
    public void setPrice(BigDecimal price) {
        if (price.doubleValue() < 0) {
            price = BigDecimal.ZERO;
        }
        gamePrice = price;
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        gameReleaseDate = releaseDate;
    }

    @Override
    public void rate(double rating) {
        counter++;
        gameRating = (gameRating * (counter - 1) + rating) / counter;
    }
}

package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class RatingItemFilter implements ItemFilter {

    private double minRating;

    public RatingItemFilter(double rating) {
        if (rating < 0) {
            rating = 0;
        }
        minRating = rating;
    }

    @Override
    public boolean matches(StoreItem item) {
        return item.getRating() >= minRating;
    }
}

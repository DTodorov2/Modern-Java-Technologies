package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import java.time.LocalDateTime;

public class ReleaseDateItemFilter implements ItemFilter{

    private LocalDateTime lowerBound, upperBound;

    public ReleaseDateItemFilter(LocalDateTime lowerBound, LocalDateTime upperBound) {
        if (lowerBound.isAfter(upperBound)) {
            LocalDateTime temp = upperBound;
            upperBound = lowerBound;
            lowerBound = temp;
        }
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
    }

    public void setLowerBound(LocalDateTime lowerBound) {
        if (lowerBound == null) {
            lowerBound = LocalDateTime.now();
        }
        this.lowerBound = lowerBound;
    }

    public void setUpperBound(LocalDateTime upperBound) {
        if (upperBound == null) {
            upperBound = LocalDateTime.now();
        }
        this.upperBound = upperBound;
    }

    @Override
    public boolean matches(StoreItem item) {
        LocalDateTime itemReleaseDate = item.getReleaseDate();
        return (itemReleaseDate.isAfter(lowerBound) && itemReleaseDate.isBefore(upperBound)) ||
                itemReleaseDate.isEqual(lowerBound) || itemReleaseDate.isEqual(upperBound);
    }
}

package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;

public class PriceItemFilter implements ItemFilter {

    private BigDecimal lowerBound, upperBound;

    public BigDecimal getLowerBound() {
        return lowerBound;
    }

    public BigDecimal getUpperBound() {
        return upperBound;
    }

    public void setLowerBound(BigDecimal lowerBound) {
        if (lowerBound == null || lowerBound.doubleValue() < 0) {
            lowerBound = BigDecimal.ZERO;
        }

        this.lowerBound = lowerBound;
    }

    public void setUpperBound(BigDecimal upperBound) {
        if (upperBound == null) {
            upperBound = BigDecimal.ZERO;
        }

        this.upperBound = upperBound;
    }

    public PriceItemFilter(BigDecimal lowerBound, BigDecimal upperBound) {
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
    }

    @Override
    public boolean matches(StoreItem item) {
        BigDecimal itemPrice = item.getPrice();
        return itemPrice.compareTo(lowerBound) > 0 && itemPrice.compareTo(upperBound) < 0;
    }
}

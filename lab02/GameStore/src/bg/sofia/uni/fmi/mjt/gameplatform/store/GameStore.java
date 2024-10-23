package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.math.BigDecimal;
import java.util.Arrays;

public class GameStore implements StoreAPI{

    private StoreItem[] items;
    private final PromoCode[] promoCodes = new PromoCode[2];

    private void setPromoCodes() {
        promoCodes[0] = new PromoCode(0,"VAN40", 0.4);
        promoCodes[1] = new PromoCode(1,"100YO", 1);
    }

    public GameStore(StoreItem[] availableItems) {
        setItems(availableItems);
        setPromoCodes();
    }

    public void setItems(StoreItem[] availableItems) {
        //trqq li da proverqvam dali available items e prazen
        items = availableItems;
    }

    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {
        int itemsInStore = items.length;
        StoreItem[] filteredItems = new StoreItem[itemsInStore];
        int indFilteredItems = 0;

        for (StoreItem item : items) {
            boolean passTheFilters = true;
            for (ItemFilter filter : itemFilters) {
                if (!filter.matches(item)) {
                    passTheFilters = false;
                    break;
                }
            }
            if (passTheFilters) {
                filteredItems[indFilteredItems++] = item;
            }
        }
        //return the array with exact length
        return Arrays.copyOf(filteredItems, indFilteredItems);
    }

    @Override
    public void applyDiscount(String promoCode) {
        if (promoCode.isBlank() || !promoCode.matches("VAN40|100YO")) {
            return;
        }

        int codeInd = 0;
        for (PromoCode code : promoCodes) {
            if (code.getCode().equals(promoCode)) {
                if (code.isUsed()) {
                    return;
                }
                else {
                    codeInd = code.getId();
                    code.setUsed(true);
                    break;
                }
            }
        }

        for (StoreItem item : items) {
            BigDecimal currItemPrice = item.getPrice();
            BigDecimal remainingPricePercentage = BigDecimal.valueOf(1 - promoCodes[codeInd].getDiscount());
            item.setPrice(currItemPrice.multiply(remainingPricePercentage));
        }
    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if (rating >= 1 && rating <= 5) {
            item.rate(rating);
            return true;
        }
        return false;
    }
}

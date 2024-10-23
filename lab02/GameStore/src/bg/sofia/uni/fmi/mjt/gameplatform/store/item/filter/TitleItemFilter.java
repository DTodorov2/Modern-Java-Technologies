package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class TitleItemFilter implements ItemFilter {

    private String title;
    private boolean caseSensitive;

    public void setTitle(String title) {
        if (title.isEmpty() || title.isBlank()) {
            title = "Invalid";
        }

        this.title = title;
    }

    public void setCaseSensitive(boolean flag) {
        caseSensitive = flag;
    }

    public TitleItemFilter(String title, boolean caseSensitive) {
        setCaseSensitive(caseSensitive);
        setTitle(title);
    }

    @Override
    public boolean matches(StoreItem item) {
        String itemTitle = item.getTitle();
        if (!caseSensitive) {
            itemTitle = itemTitle.toLowerCase();
            return itemTitle.contains(title.toLowerCase());
        }
        return itemTitle.contains(title);
    }
}

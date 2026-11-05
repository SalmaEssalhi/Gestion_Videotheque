package ma.ensa.videotheque.util;

import java.util.List;

public class Page<T> {
    private final List<T> items;
    private final int page, taille;
    private final long total;

    public Page(List<T> items, int page, int taille, long total) {
        this.items = items; this.page = page; this.taille = taille; this.total = total;
    }
    public List<T> getItems() { return items; }
    public int getPage() { return page; }
    public int getTaille() { return taille; }
    public long getTotal() { return total; }
    public int getTotalPages() { return (int) Math.max(1, Math.ceil((double) total / taille)); }
}
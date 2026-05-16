package com.zetaplugins.lifestealz.caches;

import com.zetaplugins.lifestealz.LifeStealZ;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Cache<T> {
    private final Set<T> cachedData;
    private final LifeStealZ plugin;

    public Cache(LifeStealZ plugin) {
        this.plugin = plugin;
        // [Folia Support] Changed from HashSet to ConcurrentHashMap.newKeySet()
        // Folia runs operations asynchronously across regions. Using a concurrent collection 
        // prevents ConcurrentModificationException when reading/writing data simultaneously.
        this.cachedData = ConcurrentHashMap.newKeySet();
        reloadCache();
    }

    /**
     * Reload the cache from the database
     */
    public abstract void reloadCache();

    /**
     * Get a set of all cached data
     */
    public Set<T> getCachedData() {
        // [Folia Support] Return a new concurrent set to maintain thread-safety
        Set<T> copy = ConcurrentHashMap.newKeySet();
        copy.addAll(cachedData);
        return copy;
    }

    /**
     * Add an item to the cache
     * @param item The item to add
     */
    public void addItem(T item) {
        cachedData.add(item);
    }

    /**
     * Remove an item from the cache
     * @param item The item to remove
     */
    public void removeItem(T item) {
        cachedData.remove(item);
    }

    /**
     * Add all items to the cache
     * @param items The items to add
     */
    public void addAllItems(Set<T> items) {
        cachedData.addAll(items);
    }

    /**
     * Clear the cache
     */
    public void clearCache() {
        cachedData.clear();
    }

    protected LifeStealZ getPlugin() {
        return plugin;
    }
}
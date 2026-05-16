package com.zetaplugins.lifestealz.caches;

import com.zetaplugins.lifestealz.LifeStealZ;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class OfflinePlayerCache extends Cache<String> {

    /**
     * A cache for offline players to avoid unnecessary database queries on tab completion
     */
    public OfflinePlayerCache(LifeStealZ plugin) {
        super(plugin);
    }

    /**
     * Reload the cache from the database
     */
    @Override
    public void reloadCache() {
        clearCache();
        // [Folia Support] Changed from HashSet to ConcurrentHashMap.newKeySet() to ensure thread safety
        Set<String> offlinePlayerNames = ConcurrentHashMap.newKeySet();
        offlinePlayerNames.addAll(getPlugin().getStorage().getPlayerNames());
        addAllItems(offlinePlayerNames);
    }
}
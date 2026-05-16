package com.zetaplugins.lifestealz.caches;

import com.zetaplugins.lifestealz.LifeStealZ;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class EliminatedPlayersCache extends Cache<String> {
    /**
     * A cache for eliminated players to avoid unnecessary database queries on tab completion
     */
    public EliminatedPlayersCache(LifeStealZ plugin) {
        super(plugin);
    }

    /**
     * Reload the cache from the database
     */
    @Override
    public void reloadCache() {
        clearCache();
        // [Folia Support] Changed from HashSet to ConcurrentHashMap.newKeySet() to ensure thread safety
        Set<String> eliminatedPlayerNames = ConcurrentHashMap.newKeySet();
        eliminatedPlayerNames.addAll(getPlugin().getStorage().getEliminatedPlayerNames());
        addAllItems(eliminatedPlayerNames);
    }

    /**
     * Get a set of all eliminated players
     * @return A set of all eliminated players
     */
    public Set<String> getEliminatedPlayers() {
        return getCachedData();
    }

    /**
     * Add a player to the cache
     * @param username The username of the player to add
     */
    public void addEliminatedPlayer(String username) {
        addItem(username);
    }

    /**
     * Remove a player from the cache
     * @param username The username of the player to remove
     */
    public void removeEliminatedPlayer(String username) {
        removeItem(username);
    }
}
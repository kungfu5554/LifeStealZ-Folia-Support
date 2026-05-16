package com.zetaplugins.lifestealz.util.revive;

import org.bukkit.Location;
/* Old code:
import org.bukkit.scheduler.BukkitTask;
*/
// Folia Update: Use Folia's ScheduledTask instead of BukkitTask
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

import java.util.UUID;

/**
 * Represents a revive task for a player at a specific location.
 *
 * @param location        The location of the beacon where the revive task is taking place.
 * @param task            The Bukkit task that manages the revive process.
 * @param reviver         The UUID of the player reviving
 * @param target
 * @param start           The timestamp when the revive process started (unix epoch in seconds).
 * @param durationSeconds The duration of the revive process in seconds.
 */
/* Old code:
public record ReviveTask(Location location, BukkitTask task, UUID reviver, UUID target, long start, int durationSeconds) {}
*/
// Folia Update: Replaced BukkitTask with ScheduledTask for Folia compatibility
public record ReviveTask(Location location, ScheduledTask task, UUID reviver, UUID target, long start, int durationSeconds) {}
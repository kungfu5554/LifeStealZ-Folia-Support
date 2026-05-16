package com.zetaplugins.lifestealz.util;

import org.bukkit.scheduler.BukkitTask;
import com.zetaplugins.lifestealz.LifeStealZ;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all running async tasks
 */
public final class AsyncTaskManager {
    
    // [Folia Support] Changed generic type to Object to accept both BukkitTask and Folia's ScheduledTask
    private final List<Object> runningTasks = new ArrayList<>();

    /**
     * Add a task to the list of running tasks
     * @param task The task to add (Accepts BukkitTask or ScheduledTask)
     */
    public void addTask(Object task) {
        runningTasks.add(task);
    }

    /**
     * Cancel all running tasks
     */
    public void cancelAllTasks() {
        for (Object taskObj : runningTasks) {
            if (taskObj == null) continue;
            
            // [Folia Support] Safely cancel tasks depending on the server software
            if (LifeStealZ.isFolia()) {
                try {
                    io.papermc.paper.threadedregions.scheduler.ScheduledTask foliaTask = 
                        (io.papermc.paper.threadedregions.scheduler.ScheduledTask) taskObj;
                    foliaTask.cancel();
                } catch (NoClassDefFoundError | ClassCastException ignored) {
                    // Ignored fallback
                }
            } else {
                if (taskObj instanceof BukkitTask) {
                    BukkitTask bukkitTask = (BukkitTask) taskObj;
                    if (!bukkitTask.isCancelled()) bukkitTask.cancel();
                }
            }
        }
        runningTasks.clear();
    }
}
package io.obadiah.lb.util;

import io.obadiah.lb.LightBlocks;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public interface Listener extends org.bukkit.event.Listener {

    default void startListening() {
        Bukkit.getPluginManager().registerEvents(this, LightBlocks.get());
    }

    default void stopListening() {
        HandlerList.unregisterAll(this);
    }
}

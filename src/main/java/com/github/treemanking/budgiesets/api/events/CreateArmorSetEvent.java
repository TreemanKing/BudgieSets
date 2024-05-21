package com.github.treemanking.budgiesets.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateArmorSetEvent extends Event {

    private final HandlerList HANDLERS = new HandlerList();
    private final String ARMOR_SET_NAME;
    private final Plugin PLUGIN;


    public CreateArmorSetEvent(String armorSetName, Plugin plugin) {
        ARMOR_SET_NAME = armorSetName;
        PLUGIN = plugin;
    }

    public String getArmorSetName() {
        return ARMOR_SET_NAME;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public HandlerList getHandlerList() {
        return HANDLERS;
    }
}

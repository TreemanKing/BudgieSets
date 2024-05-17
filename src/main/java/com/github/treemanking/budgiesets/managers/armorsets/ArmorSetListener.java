package com.github.treemanking.budgiesets.managers.armorsets;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.armorsets.utilities.ArmorSetUtilities;
import com.github.treemanking.budgiesets.managers.configuration.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class ArmorSetListener implements Listener, ArmorSetUtilities {

    private final String armorSetName;
    private final HashMap<UUID, EquipStatus> playerEquipStatusHashMap = new HashMap<>();
    private final BudgieSets plugin;

    public ArmorSetListener(String armorSetName, FileConfiguration armorSetConfig, BudgieSets plugin) {
        this.armorSetName = armorSetName;
        this.plugin = plugin;

        // Registers new EventManager which registers all events for the specific armor-set
        EventManager eventManager = new EventManager();
        eventManager.registerArmorEvents(armorSetConfig, plugin, this.playerEquipStatusHashMap);
    }

    // Events

    // Handles armor set equipping/unequipped. This has nothing to do with registration of the events
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerEquip(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        EquipStatus currentStatus = playerEquipStatusHashMap.getOrDefault(playerId, EquipStatus.NULL);

        if (isWearingFullSet(player, armorSetName) && currentStatus.equals(EquipStatus.NOT_EQUIPPED)) {
            playerEquipStatusHashMap.put(playerId, EquipStatus.EQUIPPED);
            player.sendMessage(ChatColor.GREEN + "You are now wearing the " + armorSetName + " set.");
        } else if (!isWearingFullSet(player, armorSetName) && currentStatus.equals(EquipStatus.EQUIPPED)) {
            player.sendMessage(ChatColor.RED + "You are now not wearing the " + armorSetName + " set and will lose all bonuses.");
            playerEquipStatusHashMap.put(playerId, EquipStatus.NOT_EQUIPPED);
        } else if (isWearingFullSet(player, armorSetName) && currentStatus.equals(EquipStatus.NULL)) { // When a player joins the server, it will only trigger this event.
            playerEquipStatusHashMap.put(playerId, EquipStatus.EQUIPPED);

            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                // Your code to be executed after 5 seconds goes here
                // For example, you can send a delayed message to the player
                player.sendMessage(ChatColor.GREEN + "You are now wearing the " + armorSetName + " set.");
            }, 60L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Add them to with the status NULL as it is unknown whether the player is wearing armour
        // When player logs in, it equips the armor four times hence a NULL status is needed
        playerEquipStatusHashMap.put(event.getPlayer().getUniqueId(), EquipStatus.NULL);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        // Remove the player from the map when the player leaves the server
        playerEquipStatusHashMap.remove(event.getPlayer().getUniqueId());
    }
}

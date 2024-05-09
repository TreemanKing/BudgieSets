package net.tree.budgiesets.eventlisteners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.managers.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.UUID;

public class ArmorSetListener implements Listener {

    private final String armorSetName;
    private final HashMap<UUID, EquipStatus> playerEquipStatusHashMap = new HashMap<>();
    private final BudgieSets plugin;

    public ArmorSetListener(String armorSetName, FileConfiguration armorSetConfig, BudgieSets plugin) {
        this.armorSetName = armorSetName;
        this.plugin = plugin;

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

        if (isWearingFullSet(player) && currentStatus.equals(EquipStatus.NOT_EQUIPPED)) {
            playerEquipStatusHashMap.put(playerId, EquipStatus.EQUIPPED);
            player.sendMessage(ChatColor.GREEN + "You are now wearing the " + armorSetName + " set.");

        } else if (!isWearingFullSet(player) && currentStatus.equals(EquipStatus.EQUIPPED)) {
            player.sendMessage(ChatColor.RED + "You are now not wearing the " + armorSetName + " set and will lose all bonuses.");
            playerEquipStatusHashMap.put(playerId, EquipStatus.NOT_EQUIPPED);

        } else if (isWearingFullSet(player) && currentStatus.equals(EquipStatus.NULL)) { // When a player joins the server, it will only trigger this event.
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
        playerEquipStatusHashMap.put(event.getPlayer().getUniqueId(), EquipStatus.NULL);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        playerEquipStatusHashMap.remove(event.getPlayer().getUniqueId());
    }

    //EVENT-UTILITIES

    private boolean isWearingFullSet(Player player) {
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        // Check if the player is actually wearing the armor
        for (ItemStack armorPiece : armorContents) {
            if (armorPiece == null || armorPiece.getType().equals(Material.AIR)) {
                return false;
            }
        }

        // Check if all four armor pieces have the specified NBT tag
        boolean hasNbtTag = true;
        for (ItemStack armorPiece : armorContents) {
            if (!hasCustomNbtTag(armorPiece, armorSetName)) {
                hasNbtTag = false;
                break;
            }
        }

        return hasNbtTag;
    }

    private boolean hasCustomNbtTag(ItemStack itemStack, String setName) {
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasTag("armorSet") && nbtItem.getString("armorSet").equals(setName);
    }

    public enum EquipStatus {
        EQUIPPED,
        NOT_EQUIPPED,
        /**
         * For when someone joins, give them a status of "nothing"
         */
        NULL
    }


}

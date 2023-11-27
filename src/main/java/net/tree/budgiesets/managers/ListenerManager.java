package net.tree.budgiesets.managers;

import com.jeff_media.armorequipevent.ArmorEquipEvent;
import net.tree.budgiesets.eventlisteners.ArmorSetEquip;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ListenerManager {

    private ListenerManager() {}

    // Method to register all listeners
    public static void registerListeners(JavaPlugin plugin) {
        ArmorEquipEvent.registerListener(plugin);
        // Add more listener registrations as needed
    }


}

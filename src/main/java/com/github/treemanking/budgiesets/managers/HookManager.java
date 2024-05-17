package com.github.treemanking.budgiesets.managers;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The HookManager class is responsible for checking the availability of
 * required dependencies such as Paper and PlaceholderAPI and managing
 * hooks for these dependencies.
 */
public class HookManager {

    /**
     * Indicates whether PlaceholderAPI is enabled on the server.
     */
    public static boolean placeholderAPIEnabled;

    /**
     * Constructs a HookManager to check and manage hooks for required dependencies.
     *
     * @param plugin the JavaPlugin instance of the BudgieSets plugin
     */
    public HookManager(JavaPlugin plugin) {
        isPaperEnabled(plugin);
        isPlaceholderAPIEnabled(plugin);
    }

    /**
     * Checks if Paper is enabled by verifying the presence of a Paper-specific class.
     * Disables the plugin if Paper is not found.
     *
     * @param plugin the JavaPlugin instance of the BudgieSets plugin
     */
    private void isPaperEnabled(JavaPlugin plugin) {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
        } catch (ClassNotFoundException ignored) {
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    /**
     * Checks if PlaceholderAPI is enabled by verifying the presence of the PlaceholderAPIPlugin class.
     * Logs a warning and sets a flag if PlaceholderAPI is not found.
     *
     * @param plugin the JavaPlugin instance of the BudgieSets plugin
     */
    private void isPlaceholderAPIEnabled(JavaPlugin plugin) {
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPIPlugin");
            placeholderAPIEnabled = true;
        } catch (ClassNotFoundException ignored) {
            plugin.getLogger().warning("PlaceholderAPI is missing, conditions will not work!");
            placeholderAPIEnabled = false;
        }
    }
}

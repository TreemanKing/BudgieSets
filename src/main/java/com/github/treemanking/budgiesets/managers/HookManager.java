package com.github.treemanking.budgiesets.managers;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The HookManager interface is responsible for checking the availability of
 * required dependencies such as Paper and PlaceholderAPI and managing
 * hooks for these dependencies.
 */
public interface HookManager {

    /**
     * A static inner class to hold state.
     */
    class HookState {
        private static boolean placeholderAPIEnabled = false;
    }

    /**
     * Constructs a HookManager to check and manage hooks for required dependencies.
     *
     * @param plugin the JavaPlugin instance of the BudgieSets plugin
     */
    default void checkHooks(JavaPlugin plugin) {
        setPlaceholderAPIEnabled(isPlaceholderAPIAvailable(plugin));
        isPaperEnabled(plugin);
    }

    /**
     * Checks if Paper is enabled by verifying the presence of a Paper-specific class.
     * Disables the plugin if Paper is not found.
     *
     * @param plugin the JavaPlugin instance of the BudgieSets plugin
     */
    default void isPaperEnabled(JavaPlugin plugin) {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
        } catch (ClassNotFoundException ignored) {
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    /**
     * Checks if PlaceholderAPI is available by verifying the presence of the PlaceholderAPIPlugin class.
     * Logs a warning if PlaceholderAPI is not found.
     *
     * @param plugin the JavaPlugin instance of the BudgieSets plugin
     * @return true if PlaceholderAPI is available, false otherwise
     */
    default boolean isPlaceholderAPIAvailable(JavaPlugin plugin) {
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPIPlugin");
            plugin.getLogger().info("PlaceholderAPI Hooked!");
            return true;
        } catch (ClassNotFoundException ignored) {
            plugin.getLogger().warning("PlaceholderAPI is missing, conditions will not work!");
            return false;
        }
    }

    /**
     * Indicates whether PlaceholderAPI is enabled on the server.
     *
     * @return true if PlaceholderAPI is enabled, false otherwise
     */
    default boolean isPlaceholderAPIEnabled() {
        return HookState.placeholderAPIEnabled;
    }

    /**
     * Sets the state indicating whether PlaceholderAPI is enabled.
     *
     * @param enabled true if PlaceholderAPI is enabled, false otherwise
     */
    default void setPlaceholderAPIEnabled(boolean enabled) {
        HookState.placeholderAPIEnabled = enabled;
    }

}

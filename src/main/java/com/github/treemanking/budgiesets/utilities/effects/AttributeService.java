package com.github.treemanking.budgiesets.utilities.effects;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Tracks and applies the attribute modifiers granted by armor sets.
 *
 * <p>The backing map and the scheduler are private: callers interact through the operations
 * below rather than mutating shared state directly.</p>
 */
public final class AttributeService {

    /**
     * A single-threaded scheduled executor service used to schedule tasks for attribute removal.
     */
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    /**
     * Attribute modifiers applied by this plugin, keyed by player UUID.
     */
    private static final Map<UUID, List<AttributeModifier>> TRACKED_MODIFIERS = new HashMap<>();

    private AttributeService() {
        throw new AssertionError("AttributeService is a static service and must not be instantiated");
    }

    /**
     * Applies a specified attribute modifier to a player and tracks it so it can be removed again.
     *
     * @param player    the player to whom the attribute effect will be applied
     * @param attribute the attribute to be modified
     * @param operation the operation that will modify the attribute (add, multiply, etc.)
     * @param amount    the amount to add or remove
     * @param time      how long the attribute modification will last, in seconds
     */
    public static void applyAttribute(@NotNull Player player, @NotNull Attribute attribute,
                                      AttributeModifier.Operation operation, Double amount, Integer time) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance == null) return;

        NamespacedKey key = new NamespacedKey("budgiesets", "modifier_" + UUID.randomUUID());
        AttributeModifier attributeModifier = new AttributeModifier(key, amount, operation, EquipmentSlotGroup.ANY);
        attributeInstance.addModifier(attributeModifier);

        TRACKED_MODIFIERS.computeIfAbsent(player.getUniqueId(), uuid -> new ArrayList<>()).add(attributeModifier);

        if (time != null && time > 0) {
            EXECUTOR.schedule(() -> removeAllAttributes(player), time, TimeUnit.SECONDS);
        }
    }

    /**
     * Removes all tracked attribute modifiers from a player and stops tracking them.
     *
     * @param player the player from whom all attribute modifiers will be removed
     */
    public static void removeAllAttributes(@NotNull Player player) {
        List<AttributeModifier> modifiers = TRACKED_MODIFIERS.remove(player.getUniqueId());
        if (modifiers == null) return;

        for (AttributeModifier modifier : modifiers) {
            for (Attribute attribute : Registry.ATTRIBUTE) {
                AttributeInstance instance = player.getAttribute(attribute);
                if (instance != null) {
                    instance.removeModifier(modifier);
                }
            }
        }
    }

    /**
     * Removes every tracked attribute modifier from every online player. Intended for plugin shutdown.
     */
    public static void removeAllTrackedAttributes() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            removeAllAttributes(player);
        }
        TRACKED_MODIFIERS.clear();
    }

    /**
     * Shuts down the scheduler backing timed attribute removal.
     *
     * <p>Without this the executor thread survives a plugin reload and leaks.</p>
     */
    public static void shutdown() {
        EXECUTOR.shutdownNow();
    }
}


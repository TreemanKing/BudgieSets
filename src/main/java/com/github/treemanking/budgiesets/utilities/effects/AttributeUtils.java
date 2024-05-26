package com.github.treemanking.budgiesets.utilities.effects;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface AttributeUtils {

    /**
     * A single-threaded scheduled executor service used to schedule tasks for attribute removal.
     */
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    /**
     * A map storing AttributeModifier instances for players identified by their unique UUIDs.
     *
     * <p>The key of the map is a {@link UUID} which uniquely identifies a player.
     * The value is a {@link List} of {@link AttributeModifier} which represents the attribute modifiers applied to the player.</p>
     *
     * <p>This map can be used to track active attribute modifiers for players in a game,
     * enabling the application, removal, or querying of attribute effects based on player UUIDs.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * // Adding an attribute modifier to a player
     * UUID playerUUID = player.getUniqueId();
     * AttributeModifier modifier = new AttributeModifier();
     * attributePlayerMap.put(playerUUID, List.of(modifier));
     *
     * // Retrieving a modifier for a player
     * List<AttributeModifier> activeModifiers = attributePlayerMap.get(playerUUID);
     * }</pre>
     */
    Map<UUID, List<AttributeModifier>> attributePlayerMap = new HashMap<>();

    /**
     * Gets the map storing attribute modifiers for players.
     *
     * @return the map of attribute modifiers
     */
    default Map<UUID, List<AttributeModifier>> getAttributePlayerMap() {
        return attributePlayerMap;
    }

    /**
     * Applies a specified attribute effect to a player.
     *
     * <p>This method takes in a player, the {@link org.bukkit.attribute.Attribute}, the
     * {@link org.bukkit.attribute.AttributeModifier.Operation} of the modification of the attribute,
     * the amount to add or remove from the attribute, and the duration it will stay enabled for. It
     * applies the attribute to the player and adds it to the active attribute map. If the attribute
     * name is invalid, the method will return without applying the effect.</p>
     *
     * @param player    the player to whom the attribute effect will be applied
     * @param attribute the attribute to be modified
     * @param operation the operation that will modify the attribute (add, multiply, etc.)
     * @param amount    the amount to add or remove
     * @param time      how long the attribute modification will last, in seconds
     */
    default void applyAttribute(@NotNull Player player, @NotNull Attribute attribute, AttributeModifier.Operation operation, Double amount, Integer time) {

        if (player.getAttribute(attribute) == null) return;

        AttributeModifier attributeModifier = new AttributeModifier("BudgieSets", amount, operation);
        player.getAttribute(attribute).addModifier(attributeModifier);

        UUID playerUUID = player.getUniqueId();
        getAttributePlayerMap().computeIfAbsent(playerUUID, k -> new ArrayList<>()).add(attributeModifier);

        if (time != null && time > 0) {
            executor.schedule(() -> removeAllAttributes(player), time, TimeUnit.SECONDS);
        }
    }

    /**
     * Removes all attribute modifiers from a player and from the attributePlayerMap.
     *
     * <p>This method removes all attribute modifiers from the player's attributes and also
     * updates the attributePlayerMap to reflect the removal. If the player's list of attribute
     * modifiers becomes empty, the player's entry is removed from the map.</p>
     *
     * @param player the player from whom all attribute modifiers will be removed
     */
    default void removeAllAttributes(@NotNull Player player) {
        UUID playerUUID = player.getUniqueId();
        List<AttributeModifier> modifiers = getAttributePlayerMap().get(playerUUID);

        if (modifiers != null) {
            for (AttributeModifier modifier : modifiers) {
                for (Attribute attribute : Attribute.values()) {
                    if (player.getAttribute(attribute) != null) {
                        player.getAttribute(attribute).removeModifier(modifier);
                    }
                }
            }
            getAttributePlayerMap().remove(playerUUID);
        }
    }
}

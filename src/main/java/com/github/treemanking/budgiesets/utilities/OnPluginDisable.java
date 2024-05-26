package com.github.treemanking.budgiesets.utilities;

import com.github.treemanking.budgiesets.utilities.effects.AttributeUtils;
import com.github.treemanking.budgiesets.utilities.effects.PotionEffectsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface OnPluginDisable extends PotionEffectsUtils, AttributeUtils {

    default void removeAllPermPotionEffects() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!getPotionEffects().containsKey(player.getUniqueId())) continue;
            removePotionEffects(player);
        }
    }

    default void removeAllPlayersAttributes() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!getAttributePlayerMap().containsKey(player.getUniqueId())) continue;
            removeAllAttributes(player);
        }
     }
}

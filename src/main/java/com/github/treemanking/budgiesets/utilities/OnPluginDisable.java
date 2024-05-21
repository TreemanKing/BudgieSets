package com.github.treemanking.budgiesets.utilities;

import com.github.treemanking.budgiesets.utilities.effects.PotionEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface OnPluginDisable extends PotionEffects {

    default void removeAllPermPotionEffects() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!getPotionEffects().containsKey(player.getUniqueId())) continue;
            removePotionEffects(player);
        }
    }
}

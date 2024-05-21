package com.github.treemanking.budgiesets;

import com.github.treemanking.budgiesets.managers.CommandManager;
import com.github.treemanking.budgiesets.managers.HookManager;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetManager;
import com.github.treemanking.budgiesets.managers.configuration.ConfigurationManager;
import com.github.treemanking.budgiesets.utilities.OnPluginDisable;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class BudgieSets extends JavaPlugin implements HookManager, OnPluginDisable {

    public static BudgieSets budgieSets;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .shouldHookPaperReload(true)
                .useLatestNMSVersion(false)
                .dispatcherFile(null)
                .silentLogs(true)
                .usePluginNamespace());
    }

    @Override
    public void onEnable() {
        budgieSets = this;

        CommandAPI.onEnable();
        checkHooks(this);

        ConfigurationManager configManager = new ConfigurationManager(this);
        new ArmorSetManager(this, configManager);
        new CommandManager(this);
    }

    @Override
    public void onDisable() {

        CommandAPI.onDisable();

        for (Player player : getServer().getOnlinePlayers()) {
            if (!getPotionEffects().containsKey(player.getUniqueId())) continue;
            removePotionEffects(player);
        }
    }

    public static BudgieSets getBudgieSets() {
        return budgieSets;
    }
}

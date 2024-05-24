package com.github.treemanking.budgiesets.events.processors;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.events.EventProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.managers.configuration.EffectsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ElytraBoostProcessor implements EventProcessor {
    @Override
    public void process(String armorSetName, Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        plugin.getServer().getPluginManager().registerEvents(new ElytraBoostProcessor.ElytraBoostListener(armorSetName, effectsMap, playerEquipStatusHashMap), plugin);
    }

    private class ElytraBoostListener implements Listener {
        private final Map<?, ?> effectsMap;
        private final Map<UUID, Long> cooldownMap = new HashMap<>();

        private final HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatus;
        private final String armorSetName;

        public ElytraBoostListener(String armorSetName, Map<?, ?> event, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
            this.effectsMap = event;
            this.playerEquipStatus = playerEquipStatusHashMap;
            this.armorSetName = armorSetName;
        }

        @EventHandler
        private void onFishingHook(PlayerElytraBoostEvent event) {
            Player player = event.getPlayer();

            if (!playerEquipStatus.containsKey(player.getUniqueId())) return;
            ArmorSetListener.EquipStatus currentStatus = playerEquipStatus.get(player.getUniqueId());
            if (checkMap(effectsMap, player, cooldownMap)) {
                new EffectsManager(effectsMap, player, currentStatus, event);
            }
        }

        @Override
        public int hashCode() {
            return armorSetName.hashCode();
        }
    }
}

package com.github.treemanking.budgiesets.events.processors;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.github.treemanking.budgiesets.managers.configuration.EffectsManager;
import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.events.EventProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EffectStaticProcessor implements EventProcessor {
    @Override
    public void process(Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        plugin.getServer().getPluginManager().registerEvents(new EffectStaticListener(effectsMap, playerEquipStatusHashMap), plugin);
    }

    private static class EffectStaticListener implements Listener {

        private final Map<?, ?> effectsMap;
        private final HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatus;

        public EffectStaticListener(Map<?, ?> event, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
            this.effectsMap = event;
            this.playerEquipStatus = playerEquipStatusHashMap;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        private void onArmorChange(PlayerArmorChangeEvent armorChangeEvent) {
            Player player = armorChangeEvent.getPlayer();
            if (!playerEquipStatus.containsKey(player.getUniqueId())) return;
            ArmorSetListener.EquipStatus currentStatus = playerEquipStatus.get(player.getUniqueId());
            new EffectsManager(effectsMap, player, currentStatus, armorChangeEvent);
        }

    }
}




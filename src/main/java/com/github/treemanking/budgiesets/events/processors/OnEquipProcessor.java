package com.github.treemanking.budgiesets.events.processors;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
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

public class OnEquipProcessor implements EventProcessor {
    @Override
    public void process(String armorSetName, Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        plugin.getServer().getPluginManager().registerEvents(new EffectStaticListener(armorSetName, effectsMap, playerEquipStatusHashMap), plugin);
    }

    private class EffectStaticListener implements Listener {

        private final Map<?, ?> effectsMap;
        private final Map<UUID, Long> cooldownMap = new HashMap<>();
        private final HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatus;
        private final String armorSetName;

        public EffectStaticListener(String armorSetName, Map<?, ?> event, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
            this.effectsMap = event;
            this.playerEquipStatus = playerEquipStatusHashMap;
            this.armorSetName = armorSetName;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        private void onArmorChange(PlayerArmorChangeEvent armorChangeEvent) {
            Player player = armorChangeEvent.getPlayer();
            if (!playerEquipStatus.containsKey(player.getUniqueId())) return;
            ArmorSetListener.EquipStatus currentStatus = playerEquipStatus.get(player.getUniqueId());
            if (checkMap(effectsMap, player, cooldownMap)) {
                effectManager.processEffectsMap(effectsMap, player, currentStatus, armorChangeEvent);
            }
        }

        @Override
        public int hashCode() {
            return armorSetName.hashCode();
        }
    }
}




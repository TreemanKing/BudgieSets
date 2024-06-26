package com.github.treemanking.budgiesets.events.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.events.EventProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class HookProcessor implements EventProcessor {
    @Override
    public void process(String armorSetName, Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        plugin.getServer().getPluginManager().registerEvents(new HookProcessor.HookListener(armorSetName, effectsMap, playerEquipStatusHashMap), plugin);
    }

    private class HookListener implements Listener {

        private final Map<?, ?> effectsMap;
        private final Map<UUID, Long> cooldownMap = new HashMap<>();

        private final HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatus;
        private final String armorSetName;

        public HookListener(String armorSetName, Map<?, ?> event, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
            this.effectsMap = event;
            this.playerEquipStatus = playerEquipStatusHashMap;
            this.armorSetName = armorSetName;
        }

        @EventHandler
        private void onFishingHook(PlayerFishEvent fishEvent) {
            Player player = fishEvent.getPlayer();
            Entity entity = null;

            if (!playerEquipStatus.containsKey(player.getUniqueId())) return;
            ArmorSetListener.EquipStatus currentStatus = playerEquipStatus.get(player.getUniqueId());

            // Get Target
            if (Objects.equals(getConfigValue(effectsMap, EFFECT_TARGET, String.class, "Player").toLowerCase(), "Hook".toLowerCase())) {
                entity = fishEvent.getHook();
            } else {
                entity = fishEvent.getPlayer();
            }

            if (checkMap(effectsMap, player, cooldownMap)) {
                effectManager.processEffectsMap(effectsMap, entity, currentStatus, fishEvent);
            }
        }

        @Override
        public int hashCode() {
            return armorSetName.hashCode();
        }
    }
}

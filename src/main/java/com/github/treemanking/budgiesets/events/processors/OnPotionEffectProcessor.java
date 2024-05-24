package com.github.treemanking.budgiesets.events.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.events.EventProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.managers.armorsets.utilities.ArmorSetUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;

import java.util.*;

public class OnPotionEffectProcessor implements EventProcessor {

    @Override
    public void process(String armorSetName, Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        plugin.getServer().getPluginManager().registerEvents(new OnPotionEffectListener(armorSetName, effectsMap, playerEquipStatusHashMap), plugin);
    }

    private class OnPotionEffectListener implements Listener {
        private final Map<?, ?> effectsMap;
        private final Map<UUID, Long> cooldownMap = new HashMap<>();
        private final HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatus;
        private final List<?> potionEffects;
        private final String armorSetName;


        public OnPotionEffectListener(String armorSetName, Map<?, ?> event, HashMap<UUID, ArmorSetUtilities.EquipStatus> playerEquipStatusHashMap) {
            this.effectsMap = event;
            this.playerEquipStatus = playerEquipStatusHashMap;
            this.potionEffects = (event.containsKey(TYPE_KEY) && event.get(TYPE_KEY) instanceof List) ? (List<?>) event.get(TYPE_KEY) : null;
            this.armorSetName = armorSetName;
        }

        @EventHandler
        private void onPlayerPotionEffect(EntityPotionEffectEvent event) {
            if (!(event.getEntity() instanceof Player player)) return;
            if (event.getNewEffect() == null) return;
            if (!playerEquipStatus.containsKey(player.getUniqueId())) return;

            ArmorSetListener.EquipStatus currentStatus = playerEquipStatus.get(player.getUniqueId());

            if (potionEffects != null) {
                if (potionEffects.contains(event.getNewEffect().getType().getName())) {
                    if (checkMap(effectsMap, player, cooldownMap)) {
                        effectManager.processEffectsMap(effectsMap, player, currentStatus, event);
                    }
                }
            } else {
                if (checkMap(effectsMap, player, cooldownMap)) {
                    effectManager.processEffectsMap(effectsMap, player, currentStatus, event);
                }
            }
        }

        @Override
        public int hashCode() {
            return armorSetName.hashCode();
        }
    }
}


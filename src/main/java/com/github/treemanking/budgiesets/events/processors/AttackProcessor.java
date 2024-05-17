package com.github.treemanking.budgiesets.events.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.events.EventProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.managers.configuration.EffectsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AttackProcessor implements EventProcessor {

    @Override
    public void process(Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        plugin.getServer().getPluginManager().registerEvents(new AttackProcessor.AttackListener(effectsMap, playerEquipStatusHashMap), plugin);
    }

    private static class AttackListener implements Listener {

        private final Map<?, ?> effectsMap;
        private final HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatus;

        public AttackListener(Map<?, ?> event, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
            this.effectsMap = event;
            this.playerEquipStatus = playerEquipStatusHashMap;
        }

        @EventHandler
        private void onPlayerAttack(EntityDamageByEntityEvent damageByEntityEvent) {
            if (!(damageByEntityEvent.getEntity() instanceof Player)) return;

            Player player = (Player) damageByEntityEvent.getDamager();

            if (!playerEquipStatus.containsKey(player.getUniqueId())) return;

            ArmorSetListener.EquipStatus currentStatus = playerEquipStatus.get(player.getUniqueId());
            new EffectsManager(effectsMap, player, currentStatus, damageByEntityEvent);
        }

    }

}

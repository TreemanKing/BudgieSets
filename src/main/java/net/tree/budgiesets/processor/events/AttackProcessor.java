package net.tree.budgiesets.processor.events;

import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.managers.EffectsManager;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AttackProcessor implements EventProcessor {

    @Override
    public void process(Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
       // plugin.getServer().getPluginManager().registerEvents(new AttackListener(effectsMap, playerEquipStatusHashMap), plugin);
    }
    /*
    private static class AttackListener implements Listener {

        private final Map<?, ?> effectsMap;

        public AttackListener(Map<?, ?> event, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
            this.effectsMap = event;
        }

        @EventHandler
        private void onPlayerAttack(EntityDamageByEntityEvent damageEvent) {
            new EffectsManager(effectsMap, player);
        }

    }

     */
}

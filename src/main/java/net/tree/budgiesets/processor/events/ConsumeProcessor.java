package net.tree.budgiesets.processor.events;

import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.managers.EffectsManager;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import net.tree.budgiesets.processor.interfaces.utils.EventSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConsumeProcessor implements EventProcessor {
    @Override
    public void process(Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        plugin.getServer().getPluginManager().registerEvents(new ConsumeListener(effectsMap, playerEquipStatusHashMap), plugin);
    }

    private static class ConsumeListener implements Listener, EventSettings {

        private final Map<?, ?> effectsMap;
        private final HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatus;

        public ConsumeListener(Map<?, ?> event, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
            this.effectsMap = event;
            this.playerEquipStatus = playerEquipStatusHashMap;
        }

        @EventHandler
        private void onPlayerConsume(PlayerItemConsumeEvent consumeEvent) {
            Player player = consumeEvent.getPlayer();

            if (!playerEquipStatus.containsKey(player.getUniqueId())) return;
            ArmorSetListener.EquipStatus currentStatus = playerEquipStatus.get(player.getUniqueId());
            new EffectsManager(effectsMap, player, currentStatus);
            consumeEvent.setCancelled(checkCancelled(effectsMap));
        }

    }
}


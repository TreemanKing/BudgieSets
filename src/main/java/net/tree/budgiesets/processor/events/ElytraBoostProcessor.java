package net.tree.budgiesets.processor.events;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.managers.EffectsManager;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ElytraBoostProcessor implements EventProcessor {
    @Override
    public void process(Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        plugin.getServer().getPluginManager().registerEvents(new ElytraBoostProcessor.ElytraBoostListener(effectsMap, playerEquipStatusHashMap), plugin);
    }

    private static class ElytraBoostListener implements Listener {
        private final Map<?, ?> effectsMap;
        private final HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatus;

        public ElytraBoostListener(Map<?, ?> event, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
            this.effectsMap = event;
            this.playerEquipStatus = playerEquipStatusHashMap;
        }

        @EventHandler
        private void onFishingHook(PlayerElytraBoostEvent playerElytraBoostEvent) {
            Player player = playerElytraBoostEvent.getPlayer();

            if (!playerEquipStatus.containsKey(player.getUniqueId())) return;
            ArmorSetListener.EquipStatus currentStatus = playerEquipStatus.get(player.getUniqueId());
            new EffectsManager(effectsMap, player, currentStatus, playerElytraBoostEvent);
        }
    }
}

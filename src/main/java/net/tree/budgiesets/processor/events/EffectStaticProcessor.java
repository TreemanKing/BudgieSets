package net.tree.budgiesets.processor.events;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.managers.EffectsManager;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
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




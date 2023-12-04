package net.tree.budgiesets.processor.events;

import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.managers.EffectsManager;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import java.util.Map;

public class ConsumeProcessor implements EventProcessor {
    @Override
    public void process(Map<?, ?> effectsMap, Player player, BudgieSets plugin) {
        plugin.getServer().getPluginManager().registerEvents(new ConsumeListener(player, effectsMap), plugin);
    }

    private static class ConsumeListener implements Listener {

        private final Player player;
        private final Map<?, ?> effectsMap;

        public ConsumeListener(Player player, Map<?, ?> event) {
            this.player = player;
            this.effectsMap = event;
        }

        @EventHandler
        private void onPlayerConsume(PlayerItemConsumeEvent consumeEvent) {
            new EffectsManager(effectsMap, player);
        }

    }
}

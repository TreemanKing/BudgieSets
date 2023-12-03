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
    public void process(Map<?, ?> event, Player player, BudgieSets plugin) {
        new ConsumeListener(player, event);
        plugin.getServer().getPluginManager().registerEvents(new ConsumeListener(player, event), plugin);
    }

    private static class ConsumeListener implements Listener {

        private final Player player;
        private final Map<?, ?> event;

        public ConsumeListener(Player player, Map<?, ?> event) {
            this.player = player;
            this.event = event;
        }

        @EventHandler
        private void onPlayerConsume(PlayerItemConsumeEvent consumeEvent) {
            new EffectsManager(event, player);
        }

    }
}

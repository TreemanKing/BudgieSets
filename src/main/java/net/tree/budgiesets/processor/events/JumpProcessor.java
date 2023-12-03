package net.tree.budgiesets.processor.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.managers.EffectsManager;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.Map;

public class JumpProcessor implements EventProcessor {

    @Override
    public void process(Map<?, ?> event, Player player, BudgieSets plugin) {
        new JumpListener(player, event);
        plugin.getServer().getPluginManager().registerEvents(new JumpProcessor.JumpListener(player, event), plugin);
    }

    private static class JumpListener implements Listener {

        private final Player player;
        private final Map<?, ?> event;

        public JumpListener(Player player, Map<?, ?> event) {
            this.player = player;
            this.event = event;
        }

        @EventHandler
        private void onPlayerJump(PlayerJumpEvent jumpEvent) {
            new EffectsManager(event, player);
        }

    }
}

package net.tree.budgiesets.processor.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.managers.EffectsManager;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import net.tree.budgiesets.processor.interfaces.EventUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.Map;

public class JumpProcessor implements EventProcessor, Listener {

    @Override
    public void process(Map<?, ?> effectsMap, Player player, BudgieSets plugin) {
        plugin.getServer().getPluginManager().registerEvents(new JumpListener(player, effectsMap), plugin);
    }

    public static class JumpListener implements Listener, EventUtilities {

        private final Player player;
        private final Map<?, ?> effectsMap;

        public JumpListener(Player player, Map<?, ?> event) {
            this.player = player;
            this.effectsMap = event;
        }

        @EventHandler
        private void onPlayerJump(PlayerJumpEvent jumpEvent) {
            new EffectsManager(effectsMap, player);
            jumpEvent.setCancelled(checkCancelled(effectsMap));
        }

    }
}

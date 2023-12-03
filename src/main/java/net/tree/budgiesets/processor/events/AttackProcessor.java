package net.tree.budgiesets.processor.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.managers.EffectsManager;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class AttackProcessor implements EventProcessor {

    @Override
    public void process(Map<?, ?> event, Player player, BudgieSets plugin) {
        new AttackListener(player, event);
        plugin.getServer().getPluginManager().registerEvents(new AttackListener(player, event), plugin);
    }

    private static class AttackListener implements Listener {

        private final Player player;
        private final Map<?, ?> event;

        public AttackListener(Player player, Map<?, ?> event) {
            this.player = player;
            this.event = event;
        }

        @EventHandler
        private void onPlayerAttack(PrePlayerAttackEntityEvent jumpEvent) {
            new EffectsManager(event, player);
        }

    }
}

package net.tree.budgiesets.processor.events;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.managers.EffectsManager;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class AttackProcessor implements EventProcessor {

    @Override
    public void process(Map<?, ?> effectsMap, Player player, BudgieSets plugin) {
        plugin.getServer().getPluginManager().registerEvents(new AttackListener(player, effectsMap), plugin);
    }

    private static class AttackListener implements Listener {

        private final Player player;
        private final Map<?, ?> effectsMap;

        public AttackListener(Player player, Map<?, ?> event) {
            this.player = player;
            this.effectsMap = event;
        }

        @EventHandler
        private void onPlayerAttack(EntityDamageByEntityEvent damageByEntityEvent) {
            new EffectsManager(effectsMap, player);
        }

    }
}

package net.tree.budgiesets.processor.events;

import net.tree.budgiesets.processor.interfaces.EventProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Map;

public class EatProcessor implements EventProcessor, Listener {
    @Override
    public void process(Map<?, ?> event, Player player) {

    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {

    }
}

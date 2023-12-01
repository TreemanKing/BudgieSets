package net.tree.budgiesets.processor.interfaces;

import org.bukkit.entity.Player;

import java.util.Map;

public interface EffectProcessor {
    void process(Map<String, Object> effect, Player player);
}

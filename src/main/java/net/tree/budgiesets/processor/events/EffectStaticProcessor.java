package net.tree.budgiesets.processor.events;

import net.tree.budgiesets.processor.EventProcessor;
import net.tree.budgiesets.utilities.effects.PermPotion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Map;

public class EffectStaticProcessor implements EventProcessor {
    @Override
    public void process(Map<String, Object> event, Player player) {
        if (event.containsKey("Effects")) {
            List<Map<String, Object>> effects = (List<Map<String, Object>>) event.get("Effects");
            processEffects(effects, player);
        } else {
            // Log a warning or handle the case where "Effects" key is missing
            Bukkit.getLogger().warning("Effects key not found for an event.");
        }
    }

    @Override
    public String getEventType() {
        return "EFFECT_STATIC";
    }

    private void processEffects(List<Map<String, Object>> effects, Player player) {
        if (effects != null) {
            for (Map<String, Object> effect : effects) {
                applyEffect(effect, player);
            }
        } else {
            // Log a warning or handle the case where "Effects" list is null
            Bukkit.getLogger().warning("Effects list not found or is null for an event.");
        }
    }

    private void applyEffect(Map<String, Object> effect, Player player) {
        // Extract effect properties and apply to the player
        String type = (String) effect.get("Type");
        int amplifier = (int) effect.get("Amplifier");
        boolean ambient = (boolean) effect.get("Ambient");
        boolean particles = (boolean) effect.get("Particles");
        List<String> conditions = (List<String>) effect.get("Conditions");

        // Assuming you have a method to apply PermPotion effects
        if (checkConditions(conditions, player)) {
            PermPotion.applyPotionEffect(player, type, amplifier, ambient, particles);
        }
    }
}

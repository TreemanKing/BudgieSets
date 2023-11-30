package net.tree.budgiesets.managers;

import net.tree.budgiesets.utilities.effects.PermPotion;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class EventManager {

    public static void applyEffectsOnEquip(Player player, FileConfiguration fileConfiguration) {
        @NotNull List<Map<?, ?>> data = fileConfiguration.getMapList("Events");

        if (data != null) {
            processEvents(data, player);
        } else {
            System.out.println("Data is null.");
        }
    }

    private static void processEvents(@NotNull List<Map<?, ?>> events, Player player) {
        for (Map<?, ?> event : events) {
            processEvent((Map<String, Object>) event, player);
        }
    }

    private static void processEvent(Map<String, Object> event, Player player) {
        if (event != null) {
            Map<String, Object> effectStatic = (Map<String, Object>) event.get("EFFECT_STATIC");
            if (effectStatic != null) {
                List<Map<String, Object>> effects = (List<Map<String, Object>>) effectStatic.get("Effects");
                processEffects(effects, player);
            } else {
                System.out.println("EFFECT_STATIC key not found or is null for an event.");
            }
        }
    }

    private static void processEffect(Map<String, Object> effect, Player player) {
        if (effect != null) {
            // Extract effect properties and apply to the player
            String type = (String) effect.get("Type");
            int amplifier = (int) effect.get("Amplifier");
            boolean ambient = (boolean) effect.get("Ambient");
            boolean particles = (boolean) effect.get("Particles");

            PermPotion.applyPotionEffect(player, type, amplifier, ambient, particles);
        }
    }

    private static void processEffects(List<Map<String, Object>> effects, Player player) {
        if (effects != null) {
            for (Map<String, Object> effect : effects) {
                processEffect(effect, player);
            }
        } else {
            System.out.println("Effects key not found or is null for an event.");
        }
    }
}


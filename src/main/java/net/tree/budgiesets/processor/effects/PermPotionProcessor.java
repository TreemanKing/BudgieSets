package net.tree.budgiesets.processor.effects;

import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import net.tree.budgiesets.utilities.PermPotion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PermPotionProcessor implements EffectProcessor {

    @Override
    public void processEffect(List<?> potions, Player player) {
        for (Object potion : potions) {
            if (potion instanceof Map<?, ?>) {
                Map<?, ?> potionMap = (Map<?, ?>) potion;

                // Extract and apply PermPotion effects here
                String type = (String) potionMap.get("Type");
                int amplifier = (int) potionMap.get("Amplifier");
                boolean ambient = (boolean) potionMap.get("Ambient");
                boolean particles = (boolean) potionMap.get("Particles");

                // Assuming you have a method to apply PermPotion effects
                applyPotionEffect(player, type, amplifier, ambient, particles);
            }
        }
    }

    private static final Map<UUID, List<PotionEffect>> activeEffects = new HashMap<>();

    private void applyPotionEffect(@NotNull Player player, @NotNull String effectName, int amplifier, boolean ambient, boolean particles) {
        PotionEffectType effectType = PotionEffectType.getByName(effectName.toUpperCase());

        if (effectType != null) {
            // Apply the permanent potion effect
            PotionEffect effect = new PotionEffect(effectType, PotionEffect.INFINITE_DURATION, amplifier, ambient, particles);

            // Check if the player already has effects
            if (activeEffects.containsKey(player.getUniqueId())) {
                activeEffects.get(player.getUniqueId()).add(effect);
            } else {
                // If not, create a new list with the current effect
                List<PotionEffect> effects = new ArrayList<>();
                effects.add(effect);
                activeEffects.put(player.getUniqueId(), effects);
            }

            player.addPotionEffect(effect);
        } else {
            Bukkit.getLogger().warning("Invalid potion effect name: " + effectName);
        }
    }

    public static void removePotionEffects(Player player) {
        UUID playerId = player.getUniqueId();
        List<PotionEffect> playerEffects = activeEffects.get(playerId);

        if (playerEffects != null) {
            // Create a copy of the effects list to avoid ConcurrentModificationException
            List<PotionEffect> effectsCopy = new ArrayList<>(playerEffects);

            player.clearActivePotionEffects();
            // Remove custom potion effects for the player
            for (PotionEffect effect : effectsCopy) {
                player.removePotionEffect(effect.getType());
            }

            // Remove the player entry from the activeEffects map
            activeEffects.remove(playerId);
        }
    }
}

package net.tree.budgiesets.managers;

import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.processor.factory.EffectProcessorFactory;
import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EffectsManager {

    public EffectsManager (Map<?, ?> event, Player player, ArmorSetListener.EquipStatus equipStatus) {
        processEffectsMap(event, player, equipStatus);
    }

    private void processEffectsMap(Map<?, ?> event, Player player, ArmorSetListener.EquipStatus equipStatus) {
        if (event.containsKey("Effects")) {
            List<Map<?, ?>> effects = (List<Map<?, ?>>) event.get("Effects");
            processEffects(effects, player, equipStatus);
        } else {
            throw new IllegalArgumentException("Effects key not found for an event.");
        }
    }

    private void processEffects(List<Map<?, ?>> effectsMap, Player player, ArmorSetListener.EquipStatus equipStatus) {
        if (effectsMap != null) {
            for (Map<?, ?> effect : effectsMap) {
                processEffect(effect, player, equipStatus);
            }
        } else {
            throw new IllegalArgumentException("Effects list not found or is null for an event.");
        }
    }

    private void processEffect(Map<?, ?> effectMap, Player player, ArmorSetListener.EquipStatus equipStatus) {
        if (effectMap != null) {
            Set<? extends Map.Entry<?, ?>> entrySet = effectMap.entrySet();
            if (entrySet.size() == 1) {
                Map.Entry<?, ?> entry = entrySet.iterator().next();
                String effectType = (String) entry.getKey();
                EffectProcessor processor = EffectProcessorFactory.createProcessor(effectType);
                if (processor != null) {
                    Object value = entry.getValue();
                    if (value instanceof List) {
                        processor.processEffect((List<?>) value, player, equipStatus);
                    } else {
                        Bukkit.getLogger().warning("Invalid effect structure found: " + effectMap);
                    }
                } else {
                    Bukkit.getLogger().warning("Invalid effect type: " + effectType);
                }
            } else {
                Bukkit.getLogger().warning("Invalid effect structure found: " + effectMap);
            }
        }
    }
}

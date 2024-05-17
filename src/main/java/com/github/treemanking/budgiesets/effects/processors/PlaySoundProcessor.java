package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Map;

public class PlaySoundProcessor implements EffectProcessor {

    private final String SOUND_KEY = "Sound";
    private final String VOLUME_KEY = "Volume";
    private final String PITCH_KEY = "Pitch";

    @Override
    public void processEffect(List<?> sounds, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object sound : sounds) {
            if (sound instanceof Map<?, ?> soundMap) {
                if (validateSoundConfig(soundMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    Sound soundType = Enum.valueOf(Sound.class, (String) soundMap.get(SOUND_KEY));
                    int volume = (int) soundMap.get(VOLUME_KEY);
                    int pitch = (int) soundMap.get(PITCH_KEY);

                    List<String> conditions = (List<String>) soundMap.get("Conditions");

                    if (checkConditions(conditions, player)) {
                        playSound(player, soundType, volume, pitch);
                    }


                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid sound configuration: " + soundMap);
                }

            }
        }
    }

    private void playSound(Player player, Sound sound, int volume, int pitch) {
        player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
    }

    private boolean validateSoundConfig(Map<?, ?> soundMap) {
        return soundMap.containsKey(SOUND_KEY)
                && soundMap.containsKey(VOLUME_KEY)
                && soundMap.containsKey(PITCH_KEY)
                && isValidSoundEnum((String) soundMap.get(SOUND_KEY))
                && soundMap.get(VOLUME_KEY) instanceof Integer
                && soundMap.get(PITCH_KEY) instanceof Integer;
    }

    private boolean isValidSoundEnum(String type) {
        try {
            Enum.valueOf(Sound.class, type);
            return true;
        } catch (IllegalArgumentException exception) {
            BudgieSets.getBudgieSets().getLogger().warning(type + "Not a valid sound.");
        }
        return false;
    }
}

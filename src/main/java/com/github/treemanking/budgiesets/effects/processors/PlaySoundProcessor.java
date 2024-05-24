package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Map;

/**
 * A class to process sound effects for armor set effects.
 */
public class PlaySoundProcessor implements EffectProcessor {

    /**
     * Processes sound effects based on the provided configuration.
     *
     * @param sounds      A list of sound configurations.
     * @param player      The player to hear the sounds.
     * @param equipStatus The equip status of the player's armor.
     * @param event       The event triggering the effect.
     */
    @Override
    public void processEffect(List<?> sounds, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object sound : sounds) {
            if (sound instanceof Map<?, ?> soundMap) {
                if (validateSoundConfig(soundMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)
                            || equipStatus.equals(ArmorSetListener.EquipStatus.NULL)) return;

                    Sound soundType = Enum.valueOf(Sound.class, getConfigValue(soundMap, SOUND_KEY, String.class, ""));
                    float volume = getConfigValue(soundMap, VOLUME_KEY, Float.class, 1.0f);
                    float pitch = getConfigValue(soundMap, PITCH_KEY, Float.class, 1.0f);

                    if (volume > 0 && pitch >= 0) {
                        playSound(player, soundType, volume, pitch);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid Sound configuration:" + soundMap);
                }
            }
        }
    }

    /**
     * Plays the specified sound to the player.
     *
     * @param player The player to hear the sound.
     * @param sound  The type of sound to play.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    private void playSound(Player player, Sound sound, float volume, float pitch) {
        player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Validates the sound configuration map.
     *
     * @param soundMap The sound configuration map to validate.
     * @return True if the configuration is valid, otherwise false.
     */
    private boolean validateSoundConfig(Map<?, ?> soundMap) {
        return soundMap.containsKey(SOUND_KEY)
                && soundMap.containsKey(VOLUME_KEY)
                && soundMap.containsKey(PITCH_KEY)
                && isValidSoundEnum(getConfigValue(soundMap, SOUND_KEY, String.class, ""));
    }

    /**
     * Checks if the provided sound type is a valid Sound enum constant.
     *
     * @param type The name of the sound type.
     * @return True if the sound type is valid, otherwise false.
     */
    private boolean isValidSoundEnum(String type) {
        try {
            Sound.valueOf(type);
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }
}

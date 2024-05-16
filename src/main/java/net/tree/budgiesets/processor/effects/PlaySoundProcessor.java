package net.tree.budgiesets.processor.effects;

import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Map;

public class PlaySoundProcessor implements EffectProcessor {

    private final String SOUND_KEY = "Sound";
    private static String VOLUME_KEY = "Volume";
    private static String PITCH_KEY = "Pitch";

    @Override
    public void processEffect(List<?> sounds, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object sound : sounds) {
            if (sound instanceof Map<?, ?>) {
                Map<?, ?> soundMap = (Map<?, ?>) sound;
                if (validateSoundConfig(soundMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    String sound2 = (String) soundMap.get(SOUND_KEY);
                    //sound.
                    int volume = (int) soundMap.get(VOLUME_KEY);
                    int pitch = (int) soundMap.get(PITCH_KEY);

                    List<String> conditions = (List<String>) soundMap.get("Conditions");

                    if (checkConditions(conditions, player)) {
                        //playSound(player, sound);
                    }


                } else {
                    // Log an error or inform the user about the invalid configuration
                    Bukkit.getLogger().warning("Invalid hunger configuration: " + soundMap);
                }

            }
        }
    }

    private void playSound(Player player, Sound sound, int volume, int pitch) {
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 1, 5);
    }

    private boolean validateSoundConfig(Map<?, ?> potionMap) {
        /*return potionMap.containsKey(AMPLIFIER_KEY)
                && potionMap.containsKey(AMBIENT_KEY)
                && potionMap.containsKey(PARTICLES_KEY)
                && potionMap.get(TYPE_KEY) instanceof String
                && potionMap.get(AMPLIFIER_KEY) instanceof Integer
                && potionMap.get(AMBIENT_KEY) instanceof Boolean
                && potionMap.get(PARTICLES_KEY) instanceof Boolean;*/
        return false;
    }
}

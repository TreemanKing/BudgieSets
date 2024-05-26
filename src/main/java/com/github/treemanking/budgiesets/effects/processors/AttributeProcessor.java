package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.PlayerEffectProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.utilities.effects.AttributeUtils;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * A class to process attributes for armor set effects
 */
public class AttributeProcessor implements PlayerEffectProcessor, AttributeUtils {

    /**
     * Processes potion effects based on the provided configuration.
     *
     * @param attributes     A list of potion configurations.
     * @param player      The player to apply the effects to.
     * @param equipStatus The equip status of the player's armor.
     * @param event       The event triggering the effect.
     */
    @Override
    public void processEffect(List<?> attributes, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object attribute : attributes) {
            if (attribute instanceof Map<?, ?> attributeMap) {
                if (validateAttributeMap(attributeMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.EQUIPPED)) {

                        Attribute attribut3 = Enum.valueOf(Attribute.class, getConfigValue(attributeMap, ATTRIBUTE_KEY, String.class));
                        AttributeModifier.Operation operation = Enum.valueOf(AttributeModifier.Operation.class, getConfigValue(attributeMap, OPERATION_KEY, String.class, "ADD_NUMBER"));
                        Double amount = getConfigValue(attributeMap, AMOUNT_KEY, Double.class);
                        Integer time = getConfigValue(attributeMap, TIME_KEY, Integer.class);

                        applyAttribute(player, attribut3, operation, amount, time);
                    } else {
                        removeAllAttributes(player);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid attribute configuration: " + attributeMap);
                }
            }
        }
    }

    private boolean validateAttributeMap(Map <?,?> map) {
        return false;
    }

    /**
     * Checks if the provided sound type is a valid Sound enum constant.
     *
     * @param type The name of the sound type.
     * @return True if the sound type is valid, otherwise false.
     */
    private boolean isValidAttributeEnum(String type) {
        try {
            Attribute.valueOf(type);
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }
}

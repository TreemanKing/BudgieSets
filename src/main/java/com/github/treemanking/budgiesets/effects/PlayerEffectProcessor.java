package com.github.treemanking.budgiesets.effects;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

public interface PlayerEffectProcessor extends EffectProcessor {

    /**
     * Processes a given effect on an entity effect based on their equip status and the associated event.
     *
     * @param effect the list of effects to process
     * @param entity the entity on whom the effect is to be processed
     * @param equipStatus the equip status of the player's armor set
     * @param event the event triggering the effect
     */
    @Override
    default void processEffect(List<?> effect, Entity entity, ArmorSetListener.EquipStatus equipStatus, Event event) {
        if (entity instanceof Player) {
            processEffect(effect, (Player) entity, equipStatus, event);
        } else {
            BudgieSets.getBudgieSets().getLogger().warning("Effect cannot be used in conjunction with " + entity.getType());
        }
    }

    void processEffect(List<?> actionBars, Player player, ArmorSetListener.EquipStatus equipStatus, Event event);

}

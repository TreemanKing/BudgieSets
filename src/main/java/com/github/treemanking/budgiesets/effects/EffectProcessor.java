package com.github.treemanking.budgiesets.effects;

import com.github.treemanking.budgiesets.utilities.EquipStatus;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import java.util.List;

/**
 * The EffectProcessor interface defines the contract for processing a configured effect
 * against an entity, based on their equipped armor set and the triggering event.
 *
 * <p>Implementations are resolved at runtime by
 * {@link com.github.treemanking.budgiesets.effects.EffectProcessorFactory}. Shared helper
 * behaviour lives in the static utilities
 * ({@code ConfigUtils}, {@code ColorUtils}, {@code PotionEffectService}) rather than being
 * inherited from this interface.</p>
 */
public interface EffectProcessor {

    /**
     * Processes a given effect on an entity effect based on their equip status and the associated event.
     *
     * @param effect the list of effects to process
     * @param entity the entity on whom the effect is to be processed
     * @param equipStatus the equip status of the player's armor set
     * @param event the event triggering the effect
     */
    void processEffect(List<?> effect, Entity entity, EquipStatus equipStatus, Event event);

}
